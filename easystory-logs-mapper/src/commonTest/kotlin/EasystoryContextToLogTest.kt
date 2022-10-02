package ru.otus.otuskotlin.easystory.logs.mapper

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class EasystoryContextToLogTest {

    @Test
    fun fromContext()  {
        val context = EasyStoryContext(
            requestId = ESRequestId("999"),
            process = ESProcess.CREATE,
            blockResponse = ESBlock(
                id = ESBlockId(id = null),
                title = "title",
                author = "author",
                content = "content",
            ),
            errors = mutableListOf(
                ESError(
                    code = "error",
                    group = "request",
                    field = "author",
                    message = "author is null"
                )
            ),
            state = CORState.RUNNING
        )

        val log = context.toLog("7834")

        assertEquals("7834", log.logId)
        assertEquals("block", log.source)
        assertEquals("999", log.block?.requestId)
        assertEquals("author is null", log.errors?.firstOrNull()?.message)
    }
}