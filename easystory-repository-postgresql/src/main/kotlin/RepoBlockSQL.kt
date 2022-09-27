package ru.otus.otuskotlin.easystory.repository.postgresql

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.Op
import ru.otus.otuskotlin.easystory.common.helpers.errorConcurrency
import ru.otus.otuskotlin.easystory.common.models.ESBlock
import ru.otus.otuskotlin.easystory.common.models.ESBlockId
import ru.otus.otuskotlin.easystory.common.models.ESBlockLock
import ru.otus.otuskotlin.easystory.common.models.ESError
import ru.otus.otuskotlin.easystory.common.repository.*
import java.sql.SQLException
import java.util.UUID

class RepoBlockSQL(
    url: String = "jdbc:postgresql://localhost:6432/esdb",
    user: String = "es",
    password: String = "es-pass",
    schema: String = "es",
    initObjects: Collection<ESBlock> = emptyList(),
    private  val newBlockLock: ESBlockLock = ESBlockLock(UUID.randomUUID().toString())
) : IBlockRepository {
    private val db by lazy { SqlConnector(url, user, password, schema).connect(BlockTable) }

    // like synchronized
    private val mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(item: ESBlock): DBBlockResponse {
        return safeTransaction({
            val response = BlockTable.insert {
                if (item.id != ESBlockId.NONE)
                    it[id] = item.id.asString()
                else
                    it[id] = generateUuid()
                it[title] = item.title
                it[author] = item.author
                it[content] = item.content
                it[created] = item.creationDate
                it[updated] = item.updatedDate
                it[lock] = item.lock.asString()
            }

            DBBlockResponse(BlockTable.from(response), true)
        }, {
            DBBlockResponse(
                result = null, isSuccess = false, errors = listOf(ESError(message = message ?: localizedMessage))
            )
        })
    }

    override suspend fun createBlock(request: DBBlockRequest): DBBlockResponse {
        val block = request.block.copy(lock = ESBlockLock(UUID.randomUUID().toString()), id = ESBlockId(generateUuid()))
        println("repo createBlock ${block}")
        return mutex.withLock {
            save(block)
        }
    }

    override suspend fun readBlock(request: DBBlockIdRequest): DBBlockResponse {
        return safeTransaction({
            val result = BlockTable.select(BlockTable.id.eq(request.id.asString())).single()
            DBBlockResponse(BlockTable.from(result), true)
        }, {
            val error = when (this) {
                is NoSuchElementException -> ESError(field = "id", message = "Not found")
                is IllegalArgumentException -> ESError(message = "More than one element with the same id")
                else -> ESError(message = localizedMessage)
            }
            DBBlockResponse(result = null, isSuccess = false, errors = listOf(error))
        })
    }

    override suspend fun updateBlock(request: DBBlockRequest): DBBlockResponse {
        val key = request.block.id.takeIf { it != ESBlockId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = request.block.lock.takeIf { it != ESBlockLock.NONE }?.asString()
        val newBlock = request.block.copy(lock = newBlockLock)

        // TODO: Mutex could be removed
        return mutex.withLock {
            safeTransaction({
                val localBlock = BlockTable.select { BlockTable.id.eq(key) }.singleOrNull()?.let {
                    BlockTable.from(it)
                } ?: return@safeTransaction resultErrorNotFound

                return@safeTransaction when (oldLock) {
                    null, localBlock.lock.asString() -> updateDB(newBlock)
                    else -> resultErrorConcurrent
                }
            }, {
                DBBlockResponse(
                    isSuccess = false,
                    result = null,
                    errors = listOf(
                        ESError(
                            field = "id",
                            message = "Not found"
                        )
                    )
                )
            })
        }
    }

    private fun updateDB(newBlock: ESBlock): DBBlockResponse {
        // TODO: maybe implement with alias if possible val blTb = BlockTable.alias("blTb")
        BlockTable.update({ BlockTable.id.eq(newBlock.id.asString()) }) {
            it[title] = newBlock.title
            it[author] = newBlock.author
            it[content] = newBlock.content
            it[created] = newBlock.creationDate
            it[updated] = newBlock.updatedDate
            it[lock] = newBlock.lock.asString()
        }
        val result = BlockTable.select { BlockTable.id.eq(newBlock.id.asString()) }.single()

        return DBBlockResponse(result = BlockTable.from(result), isSuccess = true)
    }

    override suspend fun deleteBlock(request: DBBlockIdRequest): DBBlockResponse {
        val key = request.id.takeIf { it != ESBlockId.NONE }?.asString() ?: return resultErrorEmptyId

        // TODO: Mutex could be removed
        return mutex
            .withLock {
                safeTransaction({
                    val localBlock = BlockTable.select { BlockTable.id.eq(key) }.single().let { BlockTable.from(it) }
                    if (localBlock.lock == request.lock) {
                        BlockTable.deleteWhere { BlockTable.id.eq(request.id.asString()) }
                        DBBlockResponse(result = localBlock, isSuccess = true)
                    } else {
                        resultErrorConcurrent
                    }
                }, {
                    DBBlockResponse(
                        result = null, isSuccess = false, errors = listOf(ESError(field = "id", message = "Not found"))
                    )
                })
            }
    }

    override suspend fun searchBlock(request: DBBlockFilterRequest): DBBlocksResponse {
        return safeTransaction({
            val results = BlockTable.select {
                if (request.filter.searchString.isBlank()) Op.TRUE
                else ((BlockTable.title like "%${request.filter.searchString}%")
                        or (BlockTable.author like "%${request.filter.searchString}%")
                        or (BlockTable.content like "%${request.filter.searchString}%"))
            }

            DBBlocksResponse(
                result = results.map { BlockTable.from(it) },
                isSuccess = true
            )
        }, {
            DBBlocksResponse(result = emptyList(), isSuccess = false, errors = listOf(ESError(message = localizedMessage)))
        })
    }

    private fun <T> safeTransaction(statement: Transaction.() -> T, handleException: Throwable.() -> T): T {
        return try {
            transaction(db, statement)
        } catch (e: SQLException) {
            e.printStackTrace()
            throw e
        } catch (e: Throwable) {
            return handleException(e)
        }
    }

    companion object {
        val resultErrorEmptyId = DBBlockResponse(
            result = null, isSuccess = false, errors = listOf(
                ESError(
                    field = "id",
                    message = "Id must not be null or blank",
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
            isSuccess = false,
            result = null,
            errors = listOf(
                errorConcurrency(
                    violationCode = "changed",
                    description = "Object has changed during request handling"
                )
            )
        )
    }
}