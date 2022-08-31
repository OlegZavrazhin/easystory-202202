package ru.otus.otuskotlin.easystory.app

import io.ktor.server.application.*
import ru.otus.otuskotlin.easystory.api.v1.models.*
import ru.otus.otuskotlin.easystory.services.BlockService
import ru.otus.otuskotlin.easystory.common.models.ESProcess

suspend fun ApplicationCall.createBlock(blockService: BlockService) {
    controllerUtil<BlockCreateRequest, BlockCreateResponse>(ESProcess.CREATE) {
        blockService.createBlock(this)
    }
}

suspend fun ApplicationCall.readBlock(blockService: BlockService) {
    controllerUtil<BlockReadRequest, BlockReadResponse>(ESProcess.READ) {
        blockService.readBlock(this)
    }
}

suspend fun ApplicationCall.updateBlock(blockService: BlockService) {
    controllerUtil<BlockUpdateRequest, BlockUpdateResponse>(ESProcess.UPDATE) {
        blockService.updateBlock(this)
    }
}

suspend fun ApplicationCall.deleteBlock(blockService: BlockService) {
    controllerUtil<BlockDeleteRequest, BlockDeleteResponse>(ESProcess.DELETE) {
        blockService.deleteBlock(this)
    }
}

suspend fun ApplicationCall.searchBlock(blockService: BlockService) {
    controllerUtil<BlockSearchRequest, BlockSearchResponse>(ESProcess.SEARCH) {
        blockService.search(this)
    }
}
