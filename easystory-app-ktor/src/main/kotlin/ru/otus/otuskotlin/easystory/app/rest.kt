package ru.otus.otuskotlin.easystory.app

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.easystory.common.models.ESBlockPrincipalMode
import ru.otus.otuskotlin.easystory.logging.esLogger
import ru.otus.otuskotlin.easystory.services.BlockService

val logger = esLogger(Route::v1Block::class.java)

fun Route.v1Block(blockService: BlockService, principalSupplier: ApplicationCall.() -> ESBlockPrincipalMode) {
    route("block") {
        post("create") {
            call.createBlock(blockService, call.principalSupplier(), logger)
        }
        post("read") {
            call.readBlock(blockService, call.principalSupplier(), logger)
        }
        post("update") {
            call.updateBlock(blockService, call.principalSupplier(), logger)
        }
        post("delete") {
            call.deleteBlock(blockService, call.principalSupplier(), logger)
        }
        post("search") {
            call.searchBlock(blockService, call.principalSupplier(), logger)
        }
    }
}
