package ru.otus.otuskotlin.easystory.app.v1

import org.junit.Test
import ru.otus.otuskotlin.easystory.app.v1.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseTest {

    private val blockCreateResponse = BlockCreateResponse(
        block = BlockResponseObject(
            id = 1,
            uuid = "346e02c6-d5c4-4f6b-94d9-5a718e42d0ca"
        )
    )

    @Test
    fun serializationRequestTest() {
        val jsonString = apiResponseSerialize(blockCreateResponse)
        assertContains(jsonString, """"id":1""")
        assertContains(jsonString, """"uuid":"346e02c6-d5c4-4f6b-94d9-5a718e42d0ca"""")
    }

    @Test
    fun deserializationRequestTest() {
        val jsonString = apiResponseSerialize(blockCreateResponse)
        val decodedObj = apiResponseDeserialize<BlockCreateResponse>(jsonString)
        assertEquals(1, decodedObj.block?.id)
        assertEquals("346e02c6-d5c4-4f6b-94d9-5a718e42d0ca", decodedObj.block?.uuid)
    }

}