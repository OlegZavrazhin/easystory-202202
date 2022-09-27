package ru.otus.otuskotlin.easystory.app

import io.ktor.server.application.*
import ru.otus.otuskotlin.easystory.api.v1.models.*
import ru.otus.otuskotlin.easystory.common.models.ESBlockPrincipalMode
import ru.otus.otuskotlin.easystory.services.BlockService
import ru.otus.otuskotlin.easystory.common.models.ESProcess

suspend fun ApplicationCall.createBlock(blockService: BlockService, principal: ESBlockPrincipalMode) {
    controllerUtil<BlockCreateRequest, BlockCreateResponse>(ESProcess.CREATE, principal) {
        blockService.createBlock(this)
    }
}

suspend fun ApplicationCall.readBlock(blockService: BlockService, principal: ESBlockPrincipalMode) {
    controllerUtil<BlockReadRequest, BlockReadResponse>(ESProcess.READ, principal) {
        blockService.readBlock(this)
    }
}

suspend fun ApplicationCall.updateBlock(blockService: BlockService, principal: ESBlockPrincipalMode) {
    controllerUtil<BlockUpdateRequest, BlockUpdateResponse>(ESProcess.UPDATE, principal) {
        blockService.updateBlock(this)
    }
}

suspend fun ApplicationCall.deleteBlock(blockService: BlockService, principal: ESBlockPrincipalMode) {
    controllerUtil<BlockDeleteRequest, BlockDeleteResponse>(ESProcess.DELETE, principal) {
        blockService.deleteBlock(this)
    }
}

suspend fun ApplicationCall.searchBlock(blockService: BlockService, principal: ESBlockPrincipalMode) {
    controllerUtil<BlockSearchRequest, BlockSearchResponse>(ESProcess.SEARCH, principal) {
        blockService.search(this)
    }
}
