package ru.otus.otuskotlin.easystory.repository.inmemory.models

import ru.otus.otuskotlin.easystory.common.models.ESBlock
import ru.otus.otuskotlin.easystory.common.models.ESBlockId
import ru.otus.otuskotlin.easystory.common.models.ESBlockLock
import kotlinx.datetime.*

data class BlockEntity(
    val id: String? = null,
    val uuid: String? = null,
    val title: String? = null,
    val author: String? = null,
    val content: String? = null,
    val creationDate: LocalDateTime? = null,
    val updatedDate: LocalDateTime? = null,
    val lock: String? = null,
) {
    constructor(model: ESBlock) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        uuid = model.uuid.takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        author = model.author.takeIf { it.isNotBlank() },
        content = model.content.takeIf { it.isNotBlank() },
        creationDate = model.creationDate,
        updatedDate = model.updatedDate,
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = ESBlock(
        id = id?.let { ESBlockId(it) } ?: ESBlockId.NONE,
        uuid = uuid ?: "",
        title = title ?: "",
        author = author ?: "",
        content = content ?: "",
        creationDate = creationDate ?: Clock.System.now().toLocalDateTime(TimeZone.UTC),
        updatedDate = updatedDate ?: Clock.System.now().toLocalDateTime(TimeZone.UTC),
        lock = lock?.let { ESBlockLock(it) } ?: ESBlockLock.NONE
    )
}
