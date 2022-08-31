package ru.otus.otuskotlin.easystory.app

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.easystory.services.BlockService

fun Route.v1Block(blockService: BlockService) {
    route("block") {
        post("create") {
            call.createBlock(blockService)
        }
        post("read") {
            call.readBlock(blockService)
        }
        post("update") {
            call.updateBlock(blockService)
        }
        post("delete") {
            call.deleteBlock(blockService)
        }
        post("search") {
            call.searchBlock(blockService)
        }
    }
}
