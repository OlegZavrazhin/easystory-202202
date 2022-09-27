package ru.otus.otuskotlin.easystory.mappers.jackson

import ru.otus.otuskotlin.easystory.api.v1.models.*
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.*
import ru.otus.otuskotlin.easystory.mappers.jackson.exceptions.UnknownESProcess
import kotlinx.datetime.*

fun EasyStoryContext.toTransportBlock(): IResponse = when (val proc = process) {
    ESProcess.CREATE -> toTransportCreate().copy(responseType = "create")
    ESProcess.READ -> toTransportRead().copy(responseType = "read")
    ESProcess.UPDATE -> toTransportUpdate().copy(responseType = "update")
    ESProcess.DELETE -> toTransportDelete().copy(responseType = "delete")
    ESProcess.SEARCH -> toTransportSearch().copy(responseType = "search")
    ESProcess.NONE -> throw UnknownESProcess(proc)
}

fun EasyStoryContext.toTransportCreate() = BlockCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toBlockApiResponseResult(),
    errors = errors.toTransportErrors(),
    block = blockResponse.toTransportBlock()
)

fun EasyStoryContext.toTransportRead() = BlockReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toBlockApiResponseResult(),
    errors = errors.toTransportErrors(),
    block = blockResponse.toTransportReadBlock()
)

fun EasyStoryContext.toTransportUpdate() = BlockUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toBlockApiResponseResult(),
    errors = errors.toTransportErrors(),
    block = blockResponse.toTransportBlock()
)

fun EasyStoryContext.toTransportDelete() = BlockDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toBlockApiResponseResult(),
    errors = errors.toTransportErrors(),
    block = blockResponse.toTransportBlock()
)

fun EasyStoryContext.toTransportSearch() = BlockSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = state.toBlockApiResponseResult(),
    errors = errors.toTransportErrors(),
    blocks = blocksResponse.toTransportBlock()
)

private fun ESBlock.toTransportBlock(): BlockResponseObject = BlockResponseObject(
    id = id.takeIf { it != ESBlockId.NONE }?.asStringOrNull(),
    uuid = uuid.takeIf { it.isNotBlank() },
    lock = lock.takeIf { it != ESBlockLock.NONE }?.asString()
)

private fun List<ESBlock>.toTransportBlock(): List<BlockReadResponseObject>? = this
    .map { it.toTransportReadBlock() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun ESBlock.toTransportReadBlock(): BlockReadResponseObject = BlockReadResponseObject(
    id = id.takeIf { it != ESBlockId.NONE }?.asStringOrNull(),
    uuid = uuid.takeIf { it.isNotBlank() },
    title = title.takeIf { it.isNotBlank() },
    author = author.takeIf { it.isNotBlank() },
    created = creationDate.toTransportDate(),
    updated = updatedDate.toTransportDate(),
    content = content.takeIf { it.isNotBlank() },
    lock = lock.takeIf { it != ESBlockLock.NONE }?.asString()
)

private fun LocalDateTime.toTransportDate(): String? = this
    .toString()
    .takeIf { it.isNotEmpty() }

private fun List<ESError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportBlock() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun ESError.toTransportBlock() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun CORState.toBlockApiResponseResult(): ResponseResult = when(this) {
    CORState.RUNNING -> ResponseResult.SUCCESS
    CORState.FINISHING -> ResponseResult.SUCCESS
    else -> ResponseResult.ERROR
}