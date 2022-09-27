package ru.otus.otuskotlin.easystory.app

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.easystory.common.models.ESBlockPrincipalMode
import ru.otus.otuskotlin.easystory.services.BlockService

fun Route.v1Block(blockService: BlockService, principalSupplier: ApplicationCall.() -> ESBlockPrincipalMode) {
    route("block") {
        post("create") {
            call.createBlock(blockService, call.principalSupplier())
        }
        post("read") {
            call.readBlock(blockService, call.principalSupplier())
        }
        post("update") {
            call.updateBlock(blockService, call.principalSupplier())
        }
        post("delete") {
            call.deleteBlock(blockService, call.principalSupplier())
        }
        post("search") {
            call.searchBlock(blockService, call.principalSupplier())
        }
    }
}
