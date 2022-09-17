package ru.otus.otuskotlin.easystory.repository.test

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.easystory.common.models.ESBlock
import ru.otus.otuskotlin.easystory.common.models.ESBlockId
import ru.otus.otuskotlin.easystory.common.repository.DBBlockIdRequest
import ru.otus.otuskotlin.easystory.common.repository.IBlockRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoBlockReadTest {
    abstract val repo: IBlockRepository
    protected open val successId = Companion.successId

    @Test
    fun readSuccess() {
        val result = runBlocking { repo.readBlock(DBBlockIdRequest(successId)) }

        assertEquals(true, result.isSuccess)
        assertEquals(readSuccessStub, result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() {
        val result = runBlocking { repo.readBlock(DBBlockIdRequest(notFoundId)) }
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(notFoundError),
            result.errors
        )
    }

    companion object: BaseInitBlock("read") {
        override val initObjects: List<ESBlock> = listOf(
            createInitTestModel("read")
        )
        private val readSuccessStub = initObjects.first()
        private val successId = ESBlockId(readSuccessStub.id.asString())
        val notFoundId = ESBlockId("block repo read not found")
    }


}