package ru.otus.otuskotlin.easystory.app

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.easystory.app.v1.models.*
import ru.otus.otuskotlin.easystory.services.BlockService
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.mappers.jackson.*

suspend fun ApplicationCall.createAd(blockService: BlockService) {
    val createRequest = receive<BlockCreateRequest>()
    respond(
        EasyStoryContext().apply { fromTransport(createRequest) }.let {
            blockService.createBlock(it)
        }.toTransportCreate()
    )
}

suspend fun ApplicationCall.readAd(blockService: BlockService) {
    val readRequest = receive<BlockReadRequest>()
    respond(
        EasyStoryContext().apply { fromTransport(readRequest) }.let {
            blockService.readBlock(it, ::buildError)
        }.toTransportRead()
    )
}

suspend fun ApplicationCall.updateAd(blockService: BlockService) {
    val updateRequest = receive<BlockUpdateRequest>()
    respond(
        EasyStoryContext().apply { fromTransport(updateRequest) }.let {
            blockService.updateBlock(it, ::buildError)
        }.toTransportUpdate()
    )
}

suspend fun ApplicationCall.deleteAd(blockService: BlockService) {
    val deleteRequest = receive<BlockDeleteRequest>()
    respond(
        EasyStoryContext().apply { fromTransport(deleteRequest) }.let {
            blockService.deleteBlock(it, ::buildError)
        }.toTransportDelete()
    )
}

suspend fun ApplicationCall.searchAd(blockService: BlockService) {
    val searchRequest = receive<BlockSearchRequest>()
    respond(
        EasyStoryContext().apply { fromTransport(searchRequest) }.let {
            blockService.search(it, ::buildError)
        }.toTransportSearch()
    )
}
