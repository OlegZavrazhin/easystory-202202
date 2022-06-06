package ru.otus.otuskotlin.easystory.app.v1

import ru.otus.otuskotlin.easystory.app.RequestSerializer
import ru.otus.otuskotlin.easystory.app.v1.models.*

internal object RequestSerializers {
    val create = RequestSerializer(BlockCreateRequest.serializer())
    val read = RequestSerializer(BlockReadRequest.serializer())
    val update = RequestSerializer(BlockUpdateRequest.serializer())
    val delete = RequestSerializer(BlockDeleteRequest.serializer())
    val search = RequestSerializer(BlockSearchRequest.serializer())
}

