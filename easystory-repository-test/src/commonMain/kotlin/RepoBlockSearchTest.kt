package ru.otus.otuskotlin.easystory.repository.test

import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.otus.otuskotlin.easystory.common.models.ESBlock
import ru.otus.otuskotlin.easystory.common.models.ESBlockFilter
import ru.otus.otuskotlin.easystory.common.repository.DBBlockFilterRequest
import ru.otus.otuskotlin.easystory.common.repository.IBlockRepository
import kotlin.test.assertEquals

abstract class RepoBlockSearchTest {
    abstract val repo: IBlockRepository
    protected open val initBlocks: List<ESBlock> = initObjects

    companion object : BaseInitBlock("search") {
        override val initObjects: List<ESBlock> = listOf(
            createInitTestModel("search-1"),
            createInitTestModel("search-2"),
            createInitTestModel("search-3"),
            createInitTestModel("2-search-3"),
        )
    }

    @Test
    fun searchSuccess() {
        val result =
            runBlocking { repo.searchBlock(DBBlockFilterRequest(filter = ESBlockFilter(searchString = searchString))) }

        val expected = initBlocks.filter {
            it.title.contains(searchString) || it.content.contains(searchString) || it.author.contains(
                searchString
            )
        }.sortedBy { it.id.asString() }
        assertEquals(true, result.isSuccess)
        assertEquals(expected, result.result?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

}