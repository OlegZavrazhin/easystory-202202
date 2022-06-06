package ru.otus.otuskotlin.easystory

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.otuskotlin.easystory.api.v1.models.*
import kotlin.test.assertEquals

class StubsTests {

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }

    @Test
    fun create() = testApplication {
        val client = myClient()

        val response = client.post("/block/create") {
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
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<BlockCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(321, responseObj.block?.id)
    }

    @Test
    fun read() = testApplication {
        val client = myClient()

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
            setBody(requestObj)
        }
        val responseObj = response.body<BlockReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(321, responseObj.block?.id)
    }

    @Test
    fun update() = testApplication {
        val client = myClient()

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
            setBody(requestObj)
        }
        val responseObj = response.body<BlockUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(321, responseObj.block?.id)
    }

    @Test
    fun delete() = testApplication {
        val client = myClient()

        val response = client.post("/block/delete") {
            val requestObj = BlockDeleteRequest(
                requestId = "12345",
                block = BaseBlockIdRequestBlock("321"),
                debug = BlockDebug(
                    mode = BlockRequestDebugMode.STUB,
                    stub = BlockRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<BlockDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals(321, responseObj.block?.id)
    }

}