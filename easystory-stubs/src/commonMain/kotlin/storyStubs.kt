package easystory.stubs

import ru.otus.otuskotlin.easystory.common.models.*
import kotlinx.datetime.*

object Story {
    private fun stub() = ESBlock(
        id = ESBlockId(id = "321"),
        uuid = "346e02c6-d5c4-4f6b-94d9-5a718e42d0ca",
        title = "FairyTail",
        author = "Steven King",
        content = "<h2>Shining</h2><p>Once upon a time...</p>",
        creationDate = Clock.System.now().toLocalDateTime(TimeZone.UTC),
        updatedDate = Clock.System.now().toLocalDateTime(TimeZone.UTC)
    )

    fun getBlock(block: (ESBlock.() -> Unit)? = null) = block?.let {
        stub().apply(it)
    } ?: stub()

    fun getBlocks() = mutableListOf(
        stub(),
        stub()
    )

    fun ESBlock.update(newBlock: ESBlock) = apply {
        title = newBlock.title
        author = newBlock.author
        content = newBlock.content
        creationDate = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        updatedDate = Clock.System.now().toLocalDateTime(TimeZone.UTC)
    }
}