package ru.otus.otuskotlin.easystory.app

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.easystory.services.BlockService

fun Route.v1Block(blockService: BlockService) {
    route("block") {
        post("create") {
            call.createAd(blockService)
        }
        post("read") {
            call.readAd(blockService)
        }
        post("update") {
            call.updateAd(blockService)
        }
        post("delete") {
            call.deleteAd(blockService)
        }
        post("search") {
            call.searchAd(blockService)
        }
    }
}
