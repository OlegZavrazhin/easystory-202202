package ru.otus.otuskotlin.easystory.common.repository

import ru.otus.otuskotlin.easystory.common.models.ESBlock
import ru.otus.otuskotlin.easystory.common.models.ESError

data class DBBlockResponse(
    override val result: ESBlock?,
    override val isSuccess: Boolean,
    override val errors: List<ESError> = emptyList()
) : IDBResponse<ESBlock>