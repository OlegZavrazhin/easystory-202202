package ru.otus.otuskotlin.easystory.api.v1

import ru.otus.otuskotlin.easystory.api.jacksonMapper
import ru.otus.otuskotlin.easystory.api.v1.models.*

fun apiRequestSerialize(request: IRequest): String = jacksonMapper.writeValueAsString(request)

fun <T : IRequest> apiRequestDeserialize(jsonString: String): T =
    jacksonMapper.readValue(jsonString, IRequest::class.java) as T