package ru.otus.otuskotlin.easystory.app

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encoding.Encoder
import ru.otus.otuskotlin.easystory.app.v1.models.*

internal class RequestSerializer<T: IRequest>(private val serializer: KSerializer<T>): KSerializer<T> by serializer {
    override fun serialize(encoder: Encoder, value: T) {
        val request = when(value) {
            is BlockCreateRequest -> value.copy(requestType = "create")
            is BlockReadRequest -> value.copy(requestType = "read")
            is BlockUpdateRequest -> value.copy(requestType = "update")
            is BlockDeleteRequest -> value.copy(requestType = "delete")
            is BlockSearchRequest -> value.copy(requestType = "search")
            else -> throw SerializationException("Unknown requestType to Serialize")
        }
        return serializer.serialize(encoder, request as T)
    }
}