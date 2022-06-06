package ru.otus.otuskotlin.easystory.app.v1

import org.junit.Test
import ru.otus.otuskotlin.easystory.app.v1.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestTest {

    private val blockCreateRequest = BlockCreateRequest(
        block = BlockToAddOrUpdate(
            title = "Story of mammals",
            author = "James Gunn",
            content = "<h1>Story of mammals</h1><p>Once upon the time...</p>"
        )
    )

    @Test
    fun serializationRequestTest() {
        val jsonString = apiRequestSerialize(blockCreateRequest)
        assertContains(jsonString, """"requestType":"create"""")
        assertContains(jsonString, """"title":"Story of mammals"""")
        assertContains(jsonString, """"author":"James Gunn"""")
        assertContains(jsonString, """"content":"<h1>Story of mammals</h1><p>Once upon the time...</p>"""")
    }

    @Test
    fun deserializationRequestTest() {
        val jsonString = apiRequestSerialize(blockCreateRequest)
        val decodedObj = apiRequestDeserialize<BlockCreateRequest>(jsonString)
        assertEquals("Story of mammals", decodedObj.block?.title)
        assertEquals("James Gunn", decodedObj.block?.author)
    }

}