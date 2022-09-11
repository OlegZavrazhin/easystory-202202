package ru.otus.otuskotlin.easystory.repository.test

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.easystory.common.models.ESBlock
import ru.otus.otuskotlin.easystory.common.models.ESBlockId
import ru.otus.otuskotlin.easystory.common.models.ESBlockLock
import ru.otus.otuskotlin.easystory.common.repository.DBBlockRequest
import ru.otus.otuskotlin.easystory.common.repository.IBlockRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoBlockUpdateTest {
    abstract val repo: IBlockRepository
    private val updateId = initObjects.first().id
    protected open val newLock = ESBlockLock(stubNewLock)
    protected open val updateObj = ESBlock (
        id = updateId,
        title = "update object",
        author = "update object author",
        content = "update object content",
        lock = initObjects.first().lock
    )

    @Test
    fun updateSuccess() {
        val result = runBlocking { repo.updateBlock(DBBlockRequest(updateObj)) }
        assertEquals(true, result.isSuccess)
        assertEquals(updateObj.id, result.result?.id)
        assertEquals(updateObj.title, result.result?.title)
        assertEquals(updateObj.author, result.result?.author)
        assertEquals(updateObj.content, result.result?.content)
        assertEquals(newLock, result.result?.lock)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() {
        val result = runBlocking { repo.updateBlock(DBBlockRequest(updateObjNotFound)) }
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(notFoundError),
            result.errors
        )
    }

    companion object: BaseInitBlock("update")  {
        override val initObjects: List<ESBlock> = listOf(
            createInitTestModel("update")
        )
        private val updateIdNotFound = ESBlockId("block-repo-update-not-found")

        private val updateObjNotFound = ESBlock (
            id = updateIdNotFound,
            title = "update object not found",
            author = "update object not found author",
            content = "update object not found content"
        )
    }
}