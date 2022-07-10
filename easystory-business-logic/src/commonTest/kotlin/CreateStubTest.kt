import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.easystory.business.logic.BlockProcessor
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.*
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.easystory.common.NONE
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateStubTest {
    private val processor = BlockProcessor()
    val id = ESBlockId("321")
    val title = "FairyTail"
    val author = "Steven King"
    val content = "<h2>Shining</h2><p>Once upon a time...</p>"
    val requestId = ESRequestId(id = "123")

    @Test
    fun createTest() = runTest {
        val context = EasyStoryContext(
            process = ESProcess.CREATE,
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
        assertEquals(ESBlockId("created block stub"), context.blockResponse.id)
        assertEquals(title, context.blockResponse.title)
        assertEquals(author, context.blockResponse.author)
        assertEquals(content, context.blockResponse.content)
        assertEquals(CORState.FINISHING, context.state)
        println("CreateStubTest createTest() context: $context")

    }

    @Test
    fun badTitleTest() = runTest {
        val context = EasyStoryContext(
            process = ESProcess.CREATE,
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
        println("CreateStubTest badTitleTest() context: $context")

    }

    @Test
    fun noSuchStubTest() = runTest {
        val context = EasyStoryContext(
            process = ESProcess.CREATE,
            state = CORState.NONE,
            workMode = ESWorkMode.STUB,
            stubCase = ESStubs.BAD_ID,
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
        println("CreateStubTest noSuchStubTest() context: $context")
    }

}