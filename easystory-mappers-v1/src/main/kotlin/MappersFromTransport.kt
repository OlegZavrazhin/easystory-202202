package ru.otus.otuskotlin.easystory.mappers.jackson

import ru.otus.otuskotlin.easystory.api.v1.models.*
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.*
import ru.otus.otuskotlin.marketplace.mappers.jackson.exceptions.UnknownRequestClass

fun EasyStoryContext.fromTransport(request: IRequest) = when (request) {
    is BlockCreateRequest -> fromTransport(request)
    is BlockReadRequest -> fromTransport(request)
    is BlockUpdateRequest -> fromTransport(request)
    is BlockDeleteRequest -> fromTransport(request)
    is BlockSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun IRequest?.requestId() = this?.requestId?.let { ESRequestId(it) } ?: ESRequestId.NONE
private fun String?.toBlockId() = this?.let { ESBlockId(it) } ?: ESBlockId.NONE
private fun BaseBlockIdRequestBlock?.toBlockWithId() = ESBlock(id = this?.id.toBlockId())

private fun BlockToAddOrUpdate.toInternal(): ESBlock = ESBlock(
    title = this.title ?: "",
    author = this.author ?: "",
    content = this.content ?: ""
)

private fun BlockToUpdate.toInternal(): ESBlock = ESBlock(
    id = this.id.toBlockId(),
    title = this.title ?: "",
    author = this.author ?: "",
    content = this.content ?: ""
)

private fun BlockFilter?.toInternal(): ESBlockFilter = ESBlockFilter(
    searchString = this?.searchString ?: ""
)

private fun BlockDebug?.transportToWorkMode(): ESWorkMode = when (this?.mode) {
    BlockRequestDebugMode.PROD -> ESWorkMode.PROD
    BlockRequestDebugMode.STUB -> ESWorkMode.STUB
    BlockRequestDebugMode.TEST -> ESWorkMode.TEST
    null -> ESWorkMode.PROD
}

private fun BlockDebug?.transportToStubCase(): ESStubs = when (this?.stub) {
    BlockRequestDebugStubs.SUCCESS -> ESStubs.SUCCESS
    BlockRequestDebugStubs.NOT_FOUND -> ESStubs.NOT_FOUND
    BlockRequestDebugStubs.BAD_ID -> ESStubs.BAD_ID
    BlockRequestDebugStubs.BAD_TITLE -> ESStubs.BAD_TITLE
    BlockRequestDebugStubs.BAD_CONTENT -> ESStubs.BAD_CONTENT
    BlockRequestDebugStubs.CANNOT_DELETE -> ESStubs.CANNOT_DELETE
    BlockRequestDebugStubs.BAD_SEARCH_STRING -> ESStubs.BAD_SEARCH_STRING
    null -> ESStubs.NONE
}

fun EasyStoryContext.fromTransport(request: BlockCreateRequest) {
    process = ESProcess.CREATE
    requestId = request.requestId()
    blockRequest = request.block?.toInternal() ?: ESBlock()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun EasyStoryContext.fromTransport(request: BlockReadRequest) {
    process = ESProcess.READ
    requestId = request.requestId()
    blockRequest = request.block.toBlockWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun EasyStoryContext.fromTransport(request: BlockUpdateRequest) {
    process = ESProcess.UPDATE
    requestId = request.requestId()
    blockRequest = request.block?.toInternal() ?: ESBlock()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun EasyStoryContext.fromTransport(request: BlockDeleteRequest) {
    process = ESProcess.DELETE
    requestId = request.requestId()
    blockRequest = request.block.toBlockWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun EasyStoryContext.fromTransport(request: BlockSearchRequest) {
    process = ESProcess.SEARCH
    requestId = request.requestId()
    blockFilter = request.filter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}