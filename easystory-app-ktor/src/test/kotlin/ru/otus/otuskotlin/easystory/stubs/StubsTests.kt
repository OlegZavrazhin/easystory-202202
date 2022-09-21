package ru.otus.otuskotlin.easystory.stubs

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.otuskotlin.easystory.api.jackson.v1.apiRequestSerialize
import ru.otus.otuskotlin.easystory.api.jackson.v1.apiResponseDeserialize
import ru.otus.otuskotlin.easystory.api.v1.models.*
import ru.otus.otuskotlin.easystory.config.AuthConfig
import ru.otus.otuskotlin.easystory.helpers.addAuth
import ru.otus.otuskotlin.easystory.module
import kotlin.test.assertEquals

class StubsTests {

    @Test
    fun create() = testApplication {
        application { module(authConfig = AuthConfig.TEST) }
        val requestObj = BlockCreateRequest(
            requestId = "0923840238",
            block = BlockToAddOrUpdate(
                title = "FairyTail",
                author = "author",
                content = "content"

            ),
            debug = BlockDebug(
                mode = BlockRequestDebugMode.STUB,
                stub = BlockRequestDebugStubs.SUCCESS
            )
        )

        val response = client.post("/block/create") {
            contentType(ContentType.Application.Json)
            addAuth()
            setBody(apiRequestSerialize(requestObj))
        }
        val responseObj = apiResponseDeserialize<BlockCreateResponse>(response.bodyAsText())

        assertEquals(200, response.status.value)
        assertEquals("created block stub", responseObj.block?.id)
    }

    @Test
    fun read() = testApplication {
        application { module(authConfig = AuthConfig.TEST) }

        val response = client.post("/block/read") {
            val requestObj = BlockReadRequest(
                requestId = "12345",
                block = BaseBlockIdRequestBlock("321"),
                debug = BlockDebug(
                    mode = BlockRequestDebugMode.STUB,
                    stub = BlockRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            addAuth()
            setBody(apiRequestSerialize(requestObj))
        }
        val responseObj = apiResponseDeserialize<BlockReadResponse>(response.bodyAsText())
        assertEquals(200, response.status.value)
        assertEquals("321", responseObj.block?.id)
    }

    @Test
    fun update() = testApplication {
        application { module(authConfig = AuthConfig.TEST) }

        val response = client.post("/block/update") {
            val requestObj = BlockUpdateRequest(
                requestId = "12345",
                block = BlockToUpdate(
                    id = "321",
                    title = "FairyTail",
                    author = "author",
                    content = "content"
                ),
                debug = BlockDebug(
                    mode = BlockRequestDebugMode.STUB,
                    stub = BlockRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            addAuth()
            setBody(apiRequestSerialize(requestObj))
        }
        val responseObj = apiResponseDeserialize<BlockUpdateResponse>(response.bodyAsText())
        assertEquals(200, response.status.value)
        assertEquals("321", responseObj.block?.id)
    }

    @Test
    fun delete() = testApplication {
        application { module(authConfig = AuthConfig.TEST) }

        val response = client.post("/block/delete") {
            val requestObj = BlockDeleteRequest(
                requestId = "12345",
                block = BaseBlockIdRequestWithLockBlock("321"),
                debug = BlockDebug(
                    mode = BlockRequestDebugMode.STUB,
                    stub = BlockRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            addAuth()
            setBody(apiRequestSerialize(requestObj))
        }
        val responseObj = apiResponseDeserialize<BlockDeleteResponse>(response.bodyAsText())
        assertEquals(200, response.status.value)
        assertEquals("321", responseObj.block?.id)
    }

}