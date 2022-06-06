package ru.otus.otuskotlin.easystory.services

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.*
import easystory.stubs.Story

class BlockService {
    fun createBlock(easyStoryContext: EasyStoryContext): EasyStoryContext {
        val response = when (easyStoryContext.workMode) {
            ESWorkMode.PROD -> TODO()
            ESWorkMode.TEST -> easyStoryContext.blockRequest
            ESWorkMode.STUB -> Story.getBlock()
        }
        return easyStoryContext.successResponse {
            blockResponse = response
        }
    }

    fun readBlock(easyStoryContext: EasyStoryContext, buildError: () -> ESError): EasyStoryContext {
        val requestedId = easyStoryContext.blockRequest.id

        return when(easyStoryContext.stubCase) {
            ESStubs.SUCCESS -> easyStoryContext.successResponse {
                blockResponse = Story.getBlock().apply { id = requestedId }
            }
            else -> easyStoryContext.errorResponse(buildError) {
                it.copy(field = "block.id", message = notFoundError(requestedId.asStringOrNull()))
            }
        }
    }

    fun updateBlock(easyStoryContext: EasyStoryContext, buildError: () -> ESError) = when (easyStoryContext.stubCase) {
        ESStubs.SUCCESS -> easyStoryContext.successResponse {
            blockResponse = Story.getBlock {
                if (blockRequest.id != ESBlockId.NONE) id = blockRequest.id
                if (blockRequest.title.isNotEmpty()) title = blockRequest.title
            }
        }
        else -> easyStoryContext.errorResponse(buildError) {
            it.copy(
                field = "block.id",
                message = notFoundError(easyStoryContext.blockRequest.id.asStringOrNull())
            )
        }
    }

    fun deleteBlock(easyStoryContext: EasyStoryContext, buildError: () -> ESError) = when (easyStoryContext.stubCase) {
        ESStubs.SUCCESS -> easyStoryContext.successResponse {
            blockResponse = Story.getBlock { id = easyStoryContext.blockRequest.id }
        }
        else -> easyStoryContext.errorResponse(buildError) {
            it.copy(
                field = "block.id",
                message = notFoundError(easyStoryContext.blockRequest.id.asStringOrNull())
            )
        }
    }

    fun search(easyStoryContext: EasyStoryContext, buildError: () -> ESError): EasyStoryContext {
        val filter = easyStoryContext.blockFilter

        val searchedString = filter.searchString

        return when (easyStoryContext.stubCase) {
            ESStubs.SUCCESS -> easyStoryContext.successResponse {
                blocksResponse.addAll(
                    Story.getBlocks()
                )
            }
            else -> easyStoryContext.errorResponse(buildError) {
                it.copy(
                    message = "No results for $searchedString"
                )
            }
        }
    }

}