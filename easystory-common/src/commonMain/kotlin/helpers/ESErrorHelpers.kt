package ru.otus.otuskotlin.easystory.common.helpers

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.models.ESError
import ru.otus.otuskotlin.easystory.common.models.ESErrorLevels

fun errorConcurrency(
    violationCode: String,
    description: String,
    level: ESErrorLevels = ESErrorLevels.ERROR,
) = ESError(
    code = "concurrent-$violationCode",
    group = "concurrency",
    field = "lock",
    message = "Concurrent object access error: $description",
    exception = null,
    level = level
)

fun EasyStoryContext.fail(error: ESError) {
    addError(error)
    state = CORState.FAILING
}

fun EasyStoryContext.addError(error: ESError) = errors.add(error)

fun errorAdministration(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    exception: Exception? = null,
    level: ESErrorLevels = ESErrorLevels.ERROR,
) = ESError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
)