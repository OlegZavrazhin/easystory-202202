package ru.otus.otuskotlin.easystory.common.models

import java.time.LocalDateTime


data class ESBlock(
    var id: ESBlockId = ESBlockId.NONE,
    var uuid: String = "",
    var title: String = "",
    var author: String = "",
    var content: String = "",
    var creationDate: LocalDateTime = LocalDateTime.now(),
    var updatedDate: LocalDateTime = LocalDateTime.now()
) {
    fun deepCopy(): ESBlock = ESBlock(
        id = this@ESBlock.id,
        uuid = this@ESBlock.uuid,
        title = this@ESBlock.title,
        author = this@ESBlock.author,
        content = this@ESBlock.content,
        creationDate = this@ESBlock.creationDate,
        updatedDate = this@ESBlock.updatedDate

    )
}