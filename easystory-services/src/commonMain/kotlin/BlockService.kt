package ru.otus.otuskotlin.easystory.services

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.*
import ru.otus.otuskotlin.easystory.business.logic.BlockProcessor

class BlockService(
    private val settings: ESSettings
) {
    private val processor = BlockProcessor(settings)

    suspend fun exec(context: EasyStoryContext) = processor.exec(context)
    suspend fun createBlock(context: EasyStoryContext) = processor.exec(context)
    suspend fun readBlock(context: EasyStoryContext) = processor.exec(context)
    suspend fun updateBlock(context: EasyStoryContext) = processor.exec(context)
    suspend fun deleteBlock(context: EasyStoryContext) = processor.exec(context)
    suspend fun search(context: EasyStoryContext) = processor.exec(context)
}