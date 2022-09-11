package ru.otus.otuskotlin.easystory.common.models

import kotlinx.datetime.*

val currentMoment = Clock.System.now().toLocalDateTime(TimeZone.UTC)

data class ESBlock(
    var id: ESBlockId = ESBlockId.NONE,
    var uuid: String = "",
    var title: String = "",
    var author: String = "",
    var content: String = "",
    var creationDate: LocalDateTime = currentMoment,
    var updatedDate: LocalDateTime = currentMoment,
    var lock: ESBlockLock = ESBlockLock.NONE
) {
    fun deepCopy(): ESBlock = ESBlock(
        id = this@ESBlock.id,
        uuid = this@ESBlock.uuid,
        title = this@ESBlock.title,
        author = this@ESBlock.author,
        content = this@ESBlock.content,
        creationDate = this@ESBlock.creationDate,
        updatedDate = this@ESBlock.updatedDate,
        lock = this@ESBlock.lock
    )
}