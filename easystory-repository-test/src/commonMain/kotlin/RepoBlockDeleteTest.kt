package ru.otus.otuskotlin.easystory.repository.test

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.easystory.common.models.ESBlock
import ru.otus.otuskotlin.easystory.common.models.ESBlockId
import ru.otus.otuskotlin.easystory.common.repository.DBBlockIdRequest
import ru.otus.otuskotlin.easystory.common.repository.IBlockRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoBlockDeleteTest {
    abstract val repo: IBlockRepository
    protected open val successId =  ESBlockId(deleteSuccessStub.id.asString())

    companion object: BaseInitBlock("delete") {
        override val initObjects: List<ESBlock> = listOf(createInitTestModel("delete"))
        private val deleteSuccessStub = initObjects.first()
        val notFoundId = ESBlockId("block-repo-delete-not-found")
    }

    @Test
    fun deleteSuccess()  {
        val result = runBlocking { repo.deleteBlock(DBBlockIdRequest(id = successId, lock = deleteSuccessStub.lock)) }

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
        assertEquals(deleteSuccessStub.copy(id = successId), result.result)
    }

    @Test
    fun deleteNotFound() {
        val result = runBlocking { repo.deleteBlock(DBBlockIdRequest(notFoundId)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(notFoundError),
            result.errors
        )
    }
}