package ru.otus.otuskotlin.easystory.app.v1

import org.junit.Test
import ru.otus.otuskotlin.easystory.app.jacksonMapper
import ru.otus.otuskotlin.easystory.app.v1.models.*
import kotlin.test.assertContains

class SerializationTest {

    @Test
    fun serializationRequestTest() {
        val blockCreateRequest = BlockCreateRequest(
            block = BlockToAddOrUpdate(
                title = "Story of mammals",
                author = "James Gunn",
                content = "<h1>Story of mammals</h1><p>Once upon the time...</p>"
            )
        )
        val jsonString = jacksonMapper.writeValueAsString(blockCreateRequest)
        assertContains(jsonString, """"title":"Story of mammals"""")
        assertContains(jsonString, """"author":"James Gunn"""")
        assertContains(jsonString, """"content":"<h1>Story of mammals</h1><p>Once upon the time...</p>"""")
    }

    @Test
    fun serializationResponseTest() {
        val blockCreateResponse = BlockCreateResponse(
            block = BlockResponseObject(
                id = 1,
                uuid = "346e02c6-d5c4-4f6b-94d9-5a718e42d0ca"
            )
        )
        val jsonString = jacksonMapper.writeValueAsString(blockCreateResponse)
        assertContains(jsonString, """"id":1""")
        assertContains(jsonString, """"uuid":"346e02c6-d5c4-4f6b-94d9-5a718e42d0ca"""")
    }

}