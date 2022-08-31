package ru.otus.otuskotlin.easystory.app

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.easystory.api.v1.models.IRequest
import ru.otus.otuskotlin.easystory.api.v1.models.IResponse
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.CORState
import ru.otus.otuskotlin.easystory.common.models.ESProcess
import ru.otus.otuskotlin.easystory.common.models.asInternalError
import ru.otus.otuskotlin.easystory.mappers.jackson.fromTransport
import ru.otus.otuskotlin.easystory.mappers.jackson.toTransportBlock

suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.controllerUtil(
    process: ESProcess,
    // some function to run
    block: EasyStoryContext.() -> Unit
) {
    val context = EasyStoryContext(
        timeStart = Clock.System.now()
    )

    try {
        val request = receive<Q>()
        context.fromTransport(request)
        context.block()
        val response = context.toTransportBlock()
        respond(response)
    } catch (e: Throwable) {
        process.also { context.process = it }
        context.state = CORState.FAILING
        context.errors.add(e.asInternalError())
        context.block()
        val response = context.toTransportBlock()
        respond(response)
    }
}