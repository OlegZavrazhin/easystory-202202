package ru.otus.otuskotlin.easystory.repository.inmemory

import com.benasher44.uuid.uuid4
import ru.otus.otuskotlin.easystory.common.models.ESBlock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.easystory.common.helpers.errorConcurrency
import ru.otus.otuskotlin.easystory.common.models.ESBlockId
import ru.otus.otuskotlin.easystory.common.models.ESBlockLock
import ru.otus.otuskotlin.easystory.common.models.ESError
import ru.otus.otuskotlin.easystory.common.repository.*
import ru.otus.otuskotlin.easystory.repository.inmemory.models.BlockEntity

class BlockRepoInMemory(
    initObjects: List<ESBlock> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUUID: () -> String = { uuid4().toString() }
) : IBlockRepository {
    private val cache = Cache.Builder()
        .expireAfterWrite(ttl)
        .build<String, BlockEntity>()
    private val mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(block: ESBlock) {
        val entity = BlockEntity(block)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    private fun getKey(request: DBBlockIdRequest): String? {
        return request.id.takeIf { it != ESBlockId.NONE }?.asString()
    }

    override suspend fun createBlock(request: DBBlockRequest): DBBlockResponse {
        val key = randomUUID()
        val block = request.block.copy(id = ESBlockId(key), lock = ESBlockLock(randomUUID()))
        val entity = BlockEntity(block)
        mutex.withLock {
            cache.put(key, entity)
        }
        return DBBlockResponse(
            result = block,
            isSuccess = true
        )
    }

    override suspend fun readBlock(request: DBBlockIdRequest): DBBlockResponse {
        val key = getKey(request) ?: return resultErrorEmptyId
        return cache.get(key)?.let {
            DBBlockResponse(
                result = it.toInternal(),
                isSuccess = true
            )
        } ?: resultErrorNotFound
    }

    override suspend fun updateBlock(request: DBBlockRequest): DBBlockResponse {
        val key = request.block.id.takeIf { it != ESBlockId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = request.block.lock.takeIf { it != ESBlockLock.NONE }?.asString()
        val newBlock = request.block.copy(lock = ESBlockLock(randomUUID()))
        val entity = BlockEntity(newBlock)
        mutex.withLock {
            val local = cache.get(key)
            when {
                local == null -> return resultErrorNotFound
                local.lock == null || local.lock == oldLock -> cache.put(key, entity)
                else -> return resultErrorConcurrent
            }
        }
        return DBBlockResponse(
            result = newBlock,
            isSuccess = true
        )
    }

    override suspend fun deleteBlock(request: DBBlockIdRequest): DBBlockResponse {
        val key = getKey(request) ?: return resultErrorEmptyId
        mutex.withLock {
            val local = cache.get(key) ?: return resultErrorNotFound
            if (local.lock == request.lock.asString()) {
                cache.invalidate(key)
                return DBBlockResponse(
                    result = local.toInternal(),
                    isSuccess = true,
                    errors = emptyList()
                )
            } else {
                return resultErrorConcurrent
            }
        }
    }

    override suspend fun searchBlock(request: DBBlockFilterRequest): DBBlocksResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                request.filter.searchString.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                            || entry.value.author?.contains(it) ?: false
                            || entry.value.content?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()

        return DBBlocksResponse(
            result = result, isSuccess = true, errors = listOf()
        )
    }

    companion object {
        val resultErrorEmptyId = DBBlockResponse(
            result = null, isSuccess = false, errors = listOf(
                ESError(
                    field = "id",
                    message = "Id is null or blank"
                )
            )
        )
        val resultErrorNotFound = DBBlockResponse(
            isSuccess = false,
            result = null,
            errors = listOf(
                ESError(
                    field = "id",
                    message = "Not found"
                )
            )
        )
        val resultErrorConcurrent = DBBlockResponse(
            result = null, isSuccess = false, errors = listOf(
                errorConcurrency(
                    violationCode = "changed",
                    description = "Object has changed during request handling"
                )
            )
        )
    }


}