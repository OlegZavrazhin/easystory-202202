import org.junit.Test
import ru.otus.otuskotlin.easystory.api.v1.models.*
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.*
import ru.otus.otuskotlin.easystory.mappers.jackson.fromTransport
import ru.otus.otuskotlin.easystory.mappers.jackson.toTransportBlock
import kotlinx.datetime.*
import kotlin.test.assertEquals


class MapperTest {
    @Test
    fun fromTransport() {
        val request = BlockCreateRequest(
            requestId = "1",
            debug = BlockDebug(
                mode = BlockRequestDebugMode.TEST,
                stub = BlockRequestDebugStubs.BAD_CONTENT
            ),
            block = BlockToAddOrUpdate(
                title = "Story of mammals",
                author = "James Gunn",
                content = "<h1>Story of mammals</h1><p>Once upon the time...</p>"
            )
        )

        val context = EasyStoryContext()
        context.fromTransport(request)

        assertEquals("1", context.requestId.asString())
        assertEquals(ESStubs.BAD_CONTENT, context.stubCase)
        assertEquals(ESWorkMode.TEST, context.workMode)
        assertEquals("Story of mammals", context.blockRequest.title)
        assertEquals("James Gunn", context.blockRequest.author)
        assertEquals("<h1>Story of mammals</h1><p>Once upon the time...</p>", context.blockRequest.content)
    }

    @Test
    fun toTransport() {
        val someDay = LocalDateTime(2021, 1, 30, 8, 30, 0)
        println("someDay ${someDay}")

        val context = EasyStoryContext(
            requestId = ESRequestId("1"),
            process = ESProcess.READ,
            blockResponse = ESBlock(
                id = ESBlockId("2"),
                uuid = "346e02c6-d5c4-4f6b-94d9-5a718e42d0ca",
                title = "Story of mammals",
                author = "James Gunn",
                content = "<h1>Story of mammals</h1><p>Once upon the time...</p>",
                creationDate = LocalDateTime(2021, 1, 30, 8, 30, 0),
                updatedDate = LocalDateTime(2021, 1, 30, 8, 30, 0)
            ),
            errors = mutableListOf(
                ESError(
                    code = "exception",
                    group = "request",
                    field = "id",
                    message = "id is not found"
                )
            ),
            state = CORState.FAILING
        )

        val response = context.toTransportBlock() as BlockReadResponse

        assertEquals("1", response.requestId)
        assertEquals("2", response.block?.id)
        assertEquals("346e02c6-d5c4-4f6b-94d9-5a718e42d0ca", response.block?.uuid)
        assertEquals("Story of mammals", response.block?.title)
        assertEquals("James Gunn", response.block?.author)
        assertEquals("<h1>Story of mammals</h1><p>Once upon the time...</p>", response.block?.content)
        // TODO: handle with zone
        assertEquals("2021-01-30T08:30", response.block?.created)
        assertEquals("2021-01-30T08:30", response.block?.updated)
        assertEquals("exception", response.errors?.firstOrNull()?.code)
        assertEquals("request", response.errors?.firstOrNull()?.group)
        assertEquals("id", response.errors?.firstOrNull()?.field)
        assertEquals("id is not found", response.errors?.firstOrNull()?.message)
    }
}