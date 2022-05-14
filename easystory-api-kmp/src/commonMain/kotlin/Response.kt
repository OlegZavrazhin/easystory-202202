package ru.otus.otuskotlin.easystory.api.v1

import ru.otus.otuskotlin.easystory.api.v1.models.IResponse

fun responseSerialize(response: IResponse): String = serializationMapper.encodeToString(BlockResponseSerializer, response)

fun <T : Any> responseDeserialize(json: String): T = serializationMapper.decodeFromString(BlockResponseSerializer, json) as T