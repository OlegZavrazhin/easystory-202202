package ru.otus.otuskotlin.easystory.repository.test

import ru.otus.otuskotlin.easystory.common.models.ESBlock
import ru.otus.otuskotlin.easystory.common.models.ESBlockId
import ru.otus.otuskotlin.easystory.common.models.ESBlockLock
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

abstract class BaseInitBlock(private val operation: String) : IInitObjects<ESBlock> {
    fun createInitTestModel(
        suf: String
    ) = ESBlock(
        id = ESBlockId("es-block-repo-$operation-$suf"),
//        uuid = "${UUID.randomUUID()}",
        title = "$suf repo title",
        author = "$suf repo author",
        content = "$suf repo content",
        creationDate = Clock.System.now().toLocalDateTime(TimeZone.UTC),
        updatedDate = Clock.System.now().toLocalDateTime(TimeZone.UTC),
        lock = ESBlockLock(
            stubLock
        )

    )
}