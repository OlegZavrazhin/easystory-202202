package ru.otus.otuskotlin.easystory.app

import io.ktor.server.application.*
import ru.otus.otuskotlin.easystory.api.v1.models.*
import ru.otus.otuskotlin.easystory.common.models.ESBlockPrincipalMode
import ru.otus.otuskotlin.easystory.services.BlockService
import ru.otus.otuskotlin.easystory.common.models.ESProcess
import ru.otus.otuskotlin.easystory.logging.ESLogWrapper

suspend fun ApplicationCall.createBlock(
    blockService: BlockService,
    principal: ESBlockPrincipalMode,
    logger: ESLogWrapper
) {
    controllerUtil<BlockCreateRequest, BlockCreateResponse>(
        process = ESProcess.CREATE,
        principal = principal,
        logger = logger,
        logId = "block create"
    ) {
        blockService.createBlock(this)
    }
}

suspend fun ApplicationCall.readBlock(
    blockService: BlockService,
    principal: ESBlockPrincipalMode,
    logger: ESLogWrapper
) {
    controllerUtil<BlockReadRequest, BlockReadResponse>(
        process = ESProcess.READ,
        principal = principal,
        logger = logger,
        logId = "block read"
    ) {
        blockService.readBlock(this)
    }
}

suspend fun ApplicationCall.updateBlock(
    blockService: BlockService,
    principal: ESBlockPrincipalMode,
    logger: ESLogWrapper
) {
    controllerUtil<BlockUpdateRequest, BlockUpdateResponse>(
        process = ESProcess.UPDATE,
        principal = principal,
        logger = logger,
        logId = "block update"
    ) {
        blockService.updateBlock(this)
    }
}

suspend fun ApplicationCall.deleteBlock(
    blockService: BlockService,
    principal: ESBlockPrincipalMode,
    logger: ESLogWrapper
) {
    controllerUtil<BlockDeleteRequest, BlockDeleteResponse>(
        process = ESProcess.DELETE,
        principal = principal,
        logger = logger,
        logId = "block delete"
    ) {
        blockService.deleteBlock(this)
    }
}

suspend fun ApplicationCall.searchBlock(
    blockService: BlockService,
    principal: ESBlockPrincipalMode,
    logger: ESLogWrapper
) {
    controllerUtil<BlockSearchRequest, BlockSearchResponse>(
        process = ESProcess.SEARCH,
        principal = principal,
        logger = logger,
        logId = "block search") {
        blockService.search(this)
    }
}
