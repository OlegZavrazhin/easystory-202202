package ru.otus.otuskotlin.easystory.services

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.*

fun EasyStoryContext.errorResponse(buildError: () -> ESError, error: (ESError) -> ESError) = apply {
    state = CORState.FAILING
    errors.add(error(buildError()))
}

fun EasyStoryContext.successResponse(context: EasyStoryContext.() -> Unit) = apply(context)
    .apply { state = CORState.RUNNING }

val notFoundError: (String?) -> String = { "Not found block by id $it" }
