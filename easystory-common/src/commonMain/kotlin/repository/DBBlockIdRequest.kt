package ru.otus.otuskotlin.easystory.common.repository

import ru.otus.otuskotlin.easystory.common.models.ESBlock
import ru.otus.otuskotlin.easystory.common.models.ESBlockId
import ru.otus.otuskotlin.easystory.common.models.ESBlockLock

data class DBBlockIdRequest(
    val id: ESBlockId,
    val lock: ESBlockLock = ESBlockLock.NONE
) {
    constructor(block: ESBlock) :  this(block.id,  block.lock)
}
