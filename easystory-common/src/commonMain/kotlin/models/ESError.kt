package ru.otus.otuskotlin.easystory.common.models

import ru.otus.otuskotlin.easystory.common.EasyStoryContext

data class ESError (
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null
)

fun EasyStoryContext.addError(error: ESError) = errors.add(error)
fun EasyStoryContext.toFailWithError(error: ESError) {
    addError(error)
    state = CORState.FAILING
}

fun buildErrorValidation(
    field: String,
    violationCode: String,
    description: String,
) =
    ESError(
        code = "$field-$violationCode-validation",
        group = "validation",
        field = field,
        message = "Validation error, field: $field, description: $description"
    )