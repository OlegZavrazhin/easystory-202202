package ru.otus.otuskotlin.easystory.api.v1

import ru.otus.otuskotlin.easystory.api.v1.models.IRequest

fun requestSerialize(request: IRequest): String = serializationMapper.encodeToString(BlockRequestSerializer, request)

fun <T : Any> requestDeserialize(json: String): T = serializationMapper.decodeFromString(BlockRequestSerializer, json) as T