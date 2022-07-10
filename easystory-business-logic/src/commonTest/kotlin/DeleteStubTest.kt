import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.easystory.business.logic.BlockProcessor
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.NONE
import ru.otus.otuskotlin.easystory.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class DeleteStubTest {
    private val processor = BlockProcessor()
    val id = ESBlockId("321")
    val title = "FairyTail"
    val author = "Steven King"
    val content = "<h2>Shining</h2><p>Once upon a time...</p>"
    val requestId = ESRequestId(id = "123")

    @Test
    fun deleteSuccessTest() = runTest {
        val context = EasyStoryContext(
            process = ESProcess.DELETE,
            state = CORState.NONE,
            workMode = ESWorkMode.STUB,
            stubCase = ESStubs.SUCCESS,
            requestId = requestId,
            timeStart = Instant.NONE,
            blockRequest = ESBlock(
                id = id
            )
        )

        processor.exec(context)
        assertEquals(id, context.blockResponse.id)
        assertEquals(CORState.FINISHING, context.state)
        println("DeleteStubTest deleteSuccessTest() context: $context")
    }

    @Test
    fun testStubBadId() = runTest {
        val context = EasyStoryContext(
            process = ESProcess.DELETE,
            state = CORState.NONE,
            workMode = ESWorkMode.STUB,
            stubCase = ESStubs.BAD_ID,
            requestId = requestId,
            timeStart = Instant.NONE,
            blockRequest = ESBlock(
                id = ESBlockId("wrong id")
            )
        )

        processor.exec(context)
        val err = context.errors.first()
        assertEquals("id-validation", err.code)
        assertEquals("id", err.field)
        assertEquals("validation", err.group)
        assertEquals("id not valid", err.message)
        assertEquals(CORState.FAILING, context.state)
        assertNotEquals(id, context.blockResponse.id)
        println("DeleteStubTest deleteSuccessTest() context: $context")
    }

    @Test
    fun noSuchStubTest() = runTest {
        val context = EasyStoryContext(
            process = ESProcess.DELETE,
            state = CORState.NONE,
            workMode = ESWorkMode.STUB,
            stubCase = ESStubs.BAD_CONTENT,
            requestId = requestId,
            timeStart = Instant.NONE,
            blockRequest = ESBlock(
                id = id
            )
        )

        processor.exec(context)
        val err = context.errors.first()
        assertEquals("stub-validation", err.code)
        assertEquals("stub", err.field)
        assertEquals("validation", err.group)
        assertEquals(CORState.FAILING, context.state)
        println("DeleteStubTest deleteSuccessTest() context: $context")
    }
}