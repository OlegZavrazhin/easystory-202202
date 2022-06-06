package ru.otus.otuskotlin.easystory.mappers.jackson

import ru.otus.otuskotlin.easystory.app.v1.models.*
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.*
import ru.otus.otuskotlin.easystory.mappers.jackson.exceptions.UnknownESProcess
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun EasyStoryContext.toTransportBlock(): IResponse = when (val proc = process) {
    ESProcess.CREATE -> toTransportCreate()
    ESProcess.READ -> toTransportRead()
    ESProcess.UPDATE -> toTransportUpdate()
    ESProcess.DELETE -> toTransportDelete()
    ESProcess.SEARCH -> toTransportSearch()
    ESProcess.NONE -> throw UnknownESProcess(proc)
}

fun EasyStoryContext.toTransportCreate() = BlockCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == ESState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    block = blockResponse.toTransportBlock()
)

fun EasyStoryContext.toTransportRead() = BlockReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == ESState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    block = blockResponse.toTransportReadBlock()
)

fun EasyStoryContext.toTransportUpdate() = BlockUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == ESState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    block = blockResponse.toTransportBlock()
)

fun EasyStoryContext.toTransportDelete() = BlockDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == ESState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    block = blockResponse.toTransportBlock()
)

fun EasyStoryContext.toTransportSearch() = BlockSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == ESState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    blocks = blocksResponse.toTransportBlock()
)

private fun ESBlock.toTransportBlock(): BlockResponseObject = BlockResponseObject(
    id = id.takeIf { it != ESBlockId.NONE }?.asIntOrNull(),
    uuid = uuid.takeIf { it.isNotBlank() }
)

private fun List<ESBlock>.toTransportBlock(): List<BlockResponseObject>? = this
    .map { it.toTransportBlock() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun ESBlock.toTransportReadBlock(): BlockReadResponseObject = BlockReadResponseObject(
    id = id.takeIf { it != ESBlockId.NONE }?.asIntOrNull(),
    uuid = uuid.takeIf { it.isNotBlank() },
    title = title.takeIf { it.isNotBlank() },
    author = author.takeIf { it.isNotBlank() },
    created = creationDate.toTransportDate(),
    updated = updatedDate.toTransportDate(),
    content = content.takeIf { it.isNotBlank() }
)

private fun LocalDateTime.toTransportDate(): String? = this
    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
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