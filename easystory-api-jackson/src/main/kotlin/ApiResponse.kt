package ru.otus.otuskotlin.easystory.api.v1

import ru.otus.otuskotlin.easystory.api.jacksonMapper
import ru.otus.otuskotlin.easystory.api.v1.models.*

fun apiResponseSerialize(request: IResponse): String = jacksonMapper.writeValueAsString(request)

fun <T : IResponse> apiResponseDeserialize(jsonString: String): T =
    jacksonMapper.readValue(jsonString, IResponse::class.java) as T