package ru.otus.otuskotlin.easystory.api.kmp.v1

import ru.otus.otuskotlin.easystory.api.v1.models.BlockCreateResponse
import ru.otus.otuskotlin.easystory.api.v1.models.BlockResponseObject
import kotlin.test.assertContains
import kotlin.test.Test
import kotlin.test.assertEquals

class ResponseTest {
    private val blockCreateResponse = BlockCreateResponse(
        block = BlockResponseObject(
            id = "1",
            uuid = "346e02c6-d5c4-4f6b-94d9-5a718e42d0ca"
        )
    )

    @Test
    fun serializationRequestTest() {
        val jsonString = responseSerialize(blockCreateResponse)
        assertContains(jsonString, "\"id\":\"1\"")
        assertContains(jsonString, """"uuid":"346e02c6-d5c4-4f6b-94d9-5a718e42d0ca"""")
    }

    @Test
    fun deserializationRequestTest() {
        val jsonString = responseSerialize(blockCreateResponse)
        val decodedObj = responseDeserialize<BlockCreateResponse>(jsonString)
        assertEquals("1", decodedObj.block?.id)
        assertEquals("346e02c6-d5c4-4f6b-94d9-5a718e42d0ca", decodedObj.block?.uuid)
    }

}