package ru.otus.otuskotlin.easystory.mappers

import io.ktor.server.auth.jwt.*
import ru.otus.otuskotlin.easystory.common.models.ESBlockPrincipalMode
import ru.otus.otuskotlin.easystory.common.models.UserId

fun JWTPrincipal?.toModel() = this?.run {
    ESBlockPrincipalMode(
        id = payload.getClaim("id").asString()?.let { UserId(it) } ?: UserId.NONE,
        fname = payload.getClaim("fname").asString() ?: "",
        mname = payload.getClaim("mname").asString() ?: "",
        lname = payload.getClaim("lname").asString() ?: "")
} ?: ESBlockPrincipalMode.NONE
