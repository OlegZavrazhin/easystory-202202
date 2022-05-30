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
)