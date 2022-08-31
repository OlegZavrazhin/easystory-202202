package ru.otus.otuskotlin.easystory.services

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.*
import easystory.stubs.Story
import ru.otus.otuskotlin.easystory.business.logic.BlockProcessor

class BlockService {

    val processor = BlockProcessor()

    suspend fun exec(context: EasyStoryContext) = processor.exec(context)
    suspend fun createBlock(context: EasyStoryContext) = processor.exec(context)
    suspend fun readBlock(context: EasyStoryContext) = processor.exec(context)
    suspend fun updateBlock(context: EasyStoryContext) = processor.exec(context)
    suspend fun deleteBlock(context: EasyStoryContext) = processor.exec(context)
    suspend fun search(context: EasyStoryContext) = processor.exec(context)
}