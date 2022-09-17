package ru.otus.otuskotlin.easystory.common.helpers

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