import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.easystory.business.logic.BlockProcessor
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.NONE
import ru.otus.otuskotlin.easystory.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class UpdateStubTest {
    private val processor = BlockProcessor()
    val id = ESBlockId("321")
    val title = "FairyTail"
    val author = "Steven King"
    val content = "<h2>Shining</h2><p>Once upon a time...</p>"
    val requestId = ESRequestId(id = "123")

    @Test
    fun updateSuccessTest() = runTest {
        val context = EasyStoryContext(
            process = ESProcess.UPDATE,
            state = CORState.NONE,
            workMode = ESWorkMode.STUB,
            stubCase = ESStubs.SUCCESS,
            requestId = requestId,
            timeStart = Instant.NONE,
            blockRequest = ESBlock(
                id = id,
                title = title,
                author = author,
                content = content
            )
        )

        processor.exec(context)
        assertEquals(id, context.blockResponse.id)
        assertEquals(title, context.blockResponse.title)
        assertEquals(author, context.blockResponse.author)
        assertEquals(content, context.blockResponse.content)
        assertEquals(CORState.FINISHING, context.state)
        println("UpdateStubTest updateSuccessTest() context: $context")
    }

    @Test
    fun testStubBadId() = runTest {
        val context = EasyStoryContext(
            process = ESProcess.UPDATE,
            state = CORState.NONE,
            workMode = ESWorkMode.STUB,
            stubCase = ESStubs.BAD_ID,
            requestId = requestId,
            timeStart = Instant.NONE,
            blockRequest = ESBlock(
                id = ESBlockId("wrong id"),
                title = title,
                author = author,
                content = content
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
        println("UpdateStubTest testStubBadId() context: $context")
    }

    @Test
    fun badTitleTest() = runTest {
        val context = EasyStoryContext(
            process = ESProcess.UPDATE,
            state = CORState.NONE,
            workMode = ESWorkMode.STUB,
            stubCase = ESStubs.BAD_TITLE,
            requestId = requestId,
            timeStart = Instant.NONE,
            blockRequest = ESBlock(
                id = id,
                title = "",
                author = author,
                content = content
            )
        )

        processor.exec(context)
        val err = context.errors.first()
        assertEquals("title-validation", err.code)
        assertEquals("title", err.field)
        assertEquals("validation", err.group)
        assertEquals("title not valid", err.message)
        assertEquals(CORState.FAILING, context.state)
        println("UpdateStubTest badTitleTest() context: $context")

    }

    @Test
    fun noSuchStubTest() = runTest {
        val context = EasyStoryContext(
            process = ESProcess.CREATE,
            state = CORState.NONE,
            workMode = ESWorkMode.STUB,
            stubCase = ESStubs.CANNOT_DELETE,
            requestId = requestId,
            timeStart = Instant.NONE,
            blockRequest = ESBlock(
                id = id,
                title = "",
                author = author,
                content = content
            )
        )

        processor.exec(context)
        val err = context.errors.first()
        assertEquals("stub-validation", err.code)
        assertEquals("stub", err.field)
        assertEquals("validation", err.group)
        assertEquals(CORState.FAILING, context.state)
        println("UpdateStubTest noSuchStubTest() context: $context")
    }
}