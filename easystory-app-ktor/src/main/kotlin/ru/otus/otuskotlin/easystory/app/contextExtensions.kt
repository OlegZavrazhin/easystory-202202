package ru.otus.otuskotlin.easystory.app

import ru.otus.otuskotlin.easystory.api.v1.models.ResponseResult
import ru.otus.otuskotlin.easystory.common.models.ESError

fun buildError() = ESError(
    field = "_", code = ResponseResult.ERROR.value
)


