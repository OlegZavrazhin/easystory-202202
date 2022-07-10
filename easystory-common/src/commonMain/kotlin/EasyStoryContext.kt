package ru.otus.otuskotlin.easystory.common

import ru.otus.otuskotlin.easystory.common.models.*
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.easystory.common.models.ESBlockFilter


data class EasyStoryContext(
    var process: ESProcess = ESProcess.NONE,
    var state: CORState = CORState.NONE,
    var errors: MutableList<ESError> = mutableListOf(),
    var workMode: ESWorkMode = ESWorkMode.PROD,
    var stubCase: ESStubs = ESStubs.NONE,

    var requestId: ESRequestId = ESRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var blockCopyForValidation: ESBlock = ESBlock(),
    var blockValidated: ESBlock = ESBlock(),

    var blockRequest: ESBlock = ESBlock(),
    var blockResponse: ESBlock = ESBlock(),
    var blockFilter: ESBlockFilter = ESBlockFilter(),
    var blocksResponse: MutableList<ESBlock> = mutableListOf()
)