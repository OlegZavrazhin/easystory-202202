package ru.otus.otuskotlin.easystory.api.v1

import ru.otus.otuskotlin.easystory.api.ResponseSerializer
import ru.otus.otuskotlin.easystory.api.v1.models.*

internal object ResponseSerializers {
    val create = ResponseSerializer(BlockCreateResponse.serializer())
    val read = ResponseSerializer(BlockReadResponse.serializer())
    val update = ResponseSerializer(BlockUpdateResponse.serializer())
    val delete = ResponseSerializer(BlockDeleteResponse.serializer())
    val search = ResponseSerializer(BlockSearchResponse.serializer())
}

