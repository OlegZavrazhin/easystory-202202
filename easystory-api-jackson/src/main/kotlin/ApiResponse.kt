package ru.otus.otuskotlin.easystory.app.v1

import ru.otus.otuskotlin.easystory.app.jacksonMapper
import ru.otus.otuskotlin.easystory.app.v1.models.*

fun apiResponseSerialize(request: IResponse): String = jacksonMapper.writeValueAsString(request)

fun <T : IResponse> apiResponseDeserialize(jsonString: String): T =
    jacksonMapper.readValue(jsonString, IResponse::class.java) as T