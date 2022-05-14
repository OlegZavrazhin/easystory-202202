package ru.otus.otuskotlin.easystory.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encoding.Encoder
import ru.otus.otuskotlin.easystory.api.v1.models.*

internal class ResponseSerializer<T: IResponse>(private val serializer: KSerializer<T>): KSerializer<T> by serializer {
    override fun serialize(encoder: Encoder, value: T) {
        val response = when(value) {
            is BlockCreateResponse -> value.copy(responseType = "create")
            is BlockReadResponse -> value.copy(responseType = "read")
            is BlockUpdateResponse -> value.copy(responseType = "update")
            is BlockDeleteResponse -> value.copy(responseType = "delete")
            is BlockSearchResponse -> value.copy(responseType = "search")
            else -> throw SerializationException("Unknown ResponseType to Serialize")
        }
        return serializer.serialize(encoder, response as T)
    }
}