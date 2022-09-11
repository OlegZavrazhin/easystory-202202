package ru.otus.otuskotlin.easystory.api.jackson.v1

import ru.otus.otuskotlin.easystory.api.v1.models.*

fun apiResponseSerialize(request: IResponse): String = jacksonMapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiResponseDeserialize(jsonString: String): T =
    jacksonMapper.readValue(jsonString, IResponse::class.java) as T