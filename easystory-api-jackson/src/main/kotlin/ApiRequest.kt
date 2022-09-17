package ru.otus.otuskotlin.easystory.api.jackson.v1

import ru.otus.otuskotlin.easystory.api.v1.models.*

fun apiRequestSerialize(request: IRequest): String = jacksonMapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiRequestDeserialize(jsonString: String): T =
    jacksonMapper.readValue(jsonString, IRequest::class.java) as T