package ru.otus.otuskotlin.easystory.api.jackson.v1

import ru.otus.otuskotlin.easystory.api.v1.models.*

fun apiResponseSerialize(request: IResponse): String {
    val result = jacksonMapper.writeValueAsString(request)
    println("here apiResponseSerialize")
    return result
}

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiResponseDeserialize(jsonString: String): T =
    jacksonMapper.readValue(jsonString, IResponse::class.java) as T