package ru.otus.otuskotlin.easystory.inmemory

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.easystory.api.jackson.v1.apiRequestSerialize
import ru.otus.otuskotlin.easystory.api.jackson.v1.apiResponseDeserialize
import ru.otus.otuskotlin.easystory.api.v1.models.*
import ru.otus.otuskotlin.easystory.common.models.ESBlock
import ru.otus.otuskotlin.easystory.common.models.ESBlockId
import ru.otus.otuskotlin.easystory.common.models.ESBlockLock
import ru.otus.otuskotlin.easystory.common.models.ESSettings
import ru.otus.otuskotlin.easystory.config.AuthConfig
import ru.otus.otuskotlin.easystory.helpers.addAuth
import ru.otus.otuskotlin.easystory.module
import ru.otus.otuskotlin.easystory.repository.inmemory.BlockRepoInMemory
import kotlin.test.Test
import kotlin.test.assertEquals

class InMemory {
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidNew = "10000000-0000-0000-0000-000000000002"
    private val uuidSup = "10000000-0000-0000-0000-000000000003"

    private val initBlock = ESBlock(
        id = ESBlockId(uuidOld),
        title = "title",
        author = "author",
        content = "content",
        lock = ESBlockLock(uuidOld)
    )

    private val initBlockSupply = ESBlock(
        id = ESBlockId(uuidSup),
        title = "title1",
        author = "author1",
        content = "content1",
        lock = ESBlockLock(uuidSup)
    )

    @Test
    fun create() = testApplication {
        application {
            val repo by lazy { BlockRepoInMemory(randomUUID = { uuidNew }) }
            val settings by lazy {
                ESSettings(repoProd = repo)
            }
            module(settings, authConfig = AuthConfig.TEST)
        }
        val requestObj = BlockCreateRequest(
            requestId = "0923840238",
            block = BlockToAddOrUpdate(
                title = "FairyTail",
                author = "author",
                content = "content"

            )
        )
        val response = client.post("/block/create") {
            contentType(ContentType.Application.Json)
            addAuth()
            setBody(apiRequestSerialize(requestObj))
        }
        println("in memory create() response: ${response}")
        val responseObj = apiResponseDeserialize<BlockCreateResponse>(response.bodyAsText())
        println("in memory create() responseObj: ${responseObj}")
        assertEquals(200, response.status.value)
        assertEquals(uuidNew, responseObj.block?.id)
        assertEquals(uuidNew, responseObj.block?.lock)
    }

    @Test
    fun read() = testApplication {
        application {
            val repo by lazy { BlockRepoInMemory(randomUUID = { uuidNew }, initObjects = listOf(initBlock)) }
            val settings by lazy {
                ESSettings(repoProd = repo)
            }
            module(settings, authConfig = AuthConfig.TEST)
        }
        val requestObj = BlockReadRequest(
            requestId = "166667",
            block = BaseBlockIdRequestBlock(uuidOld)
        )
        val response = client.post("/block/read") {
            contentType(ContentType.Application.Json)
            addAuth()
            setBody(apiRequestSerialize(requestObj))
        }
        val responseObj = apiResponseDeserialize<BlockReadResponse>(response.bodyAsText())
        println("in memory read() responseObj: ${responseObj}")
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.block?.id)
    }

    @Test
    fun update() = testApplication {
        application {
            val repo by lazy { BlockRepoInMemory(randomUUID = { uuidNew }, initObjects = listOf(initBlock)) }
            val settings by lazy {
                ESSettings(repoProd = repo)
            }
            module(settings, authConfig = AuthConfig.TEST)
        }

        val requestObj = BlockUpdateRequest(
            requestId = "166667",
            block = BlockToUpdate(
                id = uuidOld,
                title = "New title",
                author = "New author",
                content = "New content",
            )
        )

        val response = client.post("/block/update") {
            contentType(ContentType.Application.Json)
            addAuth()
            setBody(apiRequestSerialize(requestObj))
        }

        val responseObj = apiResponseDeserialize<BlockUpdateResponse>(response.bodyAsText())
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.block?.id)
    }

    @Test
    fun delete() = testApplication {
        application {
            val repo by lazy { BlockRepoInMemory(randomUUID = { uuidNew }, initObjects = listOf(initBlock)) }
            val settings by lazy {
                ESSettings(repoProd = repo)
            }
            module(settings, authConfig = AuthConfig.TEST)
        }

        val requestObj = BlockDeleteRequest(
            requestId = "166667",
            block = BaseBlockIdRequestWithLockBlock(id = uuidOld, lock = uuidNew) // TODO: lock is new
        )

        val response = client.post("/block/delete") {
            contentType(ContentType.Application.Json)
            addAuth()
            setBody(apiRequestSerialize(requestObj))
        }

        val responseObj = apiResponseDeserialize<BlockDeleteResponse>(response.bodyAsText())
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.block?.id)
        assertEquals(uuidOld, responseObj.block?.lock) // TODO: lock is old
    }

    @Test
    fun search() = testApplication {
        application {
            val repo by lazy { BlockRepoInMemory(randomUUID = { uuidNew }, initObjects = listOf(initBlock)) }
            val settings by lazy {
                ESSettings(repoProd = repo)
            }
            module(settings, authConfig = AuthConfig.TEST)
        }

        val requestObj = BlockSearchRequest(
            requestId = "166667", filter = BlockFilter()
        )

        val response = client.post("/block/search") {
            contentType(ContentType.Application.Json)
            addAuth()
            setBody(apiRequestSerialize(requestObj))
        }
        val responseObj = apiResponseDeserialize<BlockSearchResponse>(response.bodyAsText())
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.blocks?.first()?.id)
    }


}