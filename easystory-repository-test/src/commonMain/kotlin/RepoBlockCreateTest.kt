package ru.otus.otuskotlin.easystory.repository.test

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.easystory.common.models.ESBlock
import ru.otus.otuskotlin.easystory.common.models.ESBlockId
import ru.otus.otuskotlin.easystory.common.repository.DBBlockRequest
import ru.otus.otuskotlin.easystory.common.repository.IBlockRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class RepoBlockCreateTest {
    abstract val repo: IBlockRepository

    companion object: BaseInitBlock("create") {
        private val createObj = ESBlock(
            title = "create object",
            author = "create object author",
            content = "create object content"
        )
        override val initObjects: List<ESBlock> = emptyList()
    }

    @Test
    fun createSuccess() {
        val result = runBlocking { repo.createBlock(DBBlockRequest(createObj)) }
        val expected = createObj.copy(id = result.result?.id ?: ESBlockId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.title, result.result?.title)
        assertEquals(expected.author, result.result?.author)
        assertEquals(expected.content, result.result?.content)
        assertNotEquals(ESBlockId.NONE, result.result?.id)
        assertEquals(emptyList(), result.errors)
    }
}