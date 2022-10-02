package ru.otus.otuskotlin.easystory.logs.mapper

import com.benasher44.uuid.uuid4
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.easystory.api.v1.models.*
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.*

fun EasyStoryContext.toLog(logId: String) = CommonLogModel(
    messageId = uuid4().toString(),
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "block",
    block = toBlockLog(),
    errors = errors.map { it.toLog() }

)

fun EasyStoryContext.toBlockLog(): BlockLogModel? {
    val blockNone = ESBlock()
    return BlockLogModel(
        requestId = requestId.takeIf { it != ESRequestId.NONE }?.asString(),
        requestBlock = blockRequest.takeIf { it != blockNone }?.toLog(),
        requestFilter = blockFilter.takeIf { it != ESBlockFilter() }?.toLog(),
        responseBlock = blockResponse.takeIf { it != blockNone }?.toLog(),
        responseBlocks = blocksResponse.takeIf { it.isNotEmpty() }?.filter { it != blockNone }?.map { it.toLog() }
    ).takeIf { it != BlockLogModel() }
}

fun ESBlock.toLog() = BlockLog(
    id = id.takeIf { it != ESBlockId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    author = author.takeIf { it.isNotBlank() },
    content = content.takeIf { it.isNotBlank() }
)

fun ESBlockFilter.toLog() = BlockFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() }
)

fun ESError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name
)