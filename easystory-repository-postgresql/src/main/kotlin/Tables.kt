package ru.otus.otuskotlin.easystory.repository.postgresql

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.statements.InsertStatement
import ru.otus.otuskotlin.easystory.common.models.ESBlock
import ru.otus.otuskotlin.easystory.common.models.ESBlockId
import ru.otus.otuskotlin.easystory.common.models.ESBlockLock
import java.util.*

object BlockTable : StringIdTable("Block") {
    val title = varchar("title", 512)
    val author = varchar("author", 512)
    val content = text("content")
    val created = datetime("created")
    val updated = datetime("updated")
    val lock = varchar("lock", 50)

    fun from(result: InsertStatement<Number>) = ESBlock(
        id = ESBlockId(result[id].toString()),
        title = result[title],
        author = result[author],
        content = result[content],
        creationDate = result[created],
        updatedDate = result[updated],
        lock = ESBlockLock(result[lock].toString())
    )

    fun from(result: ResultRow) = ESBlock(
        id = ESBlockId(result[id].toString()),
        title = result[title],
        author = result[author],
        content = result[content],
        creationDate = result[created],
        updatedDate = result[updated],
        lock = ESBlockLock(result[lock].toString())
    )
}

open class StringIdTable(name: String = "", columnName: String = "id", columnLength: Int = 50) : IdTable<String>(name) {
    override val id: Column<EntityID<String>> =
        varchar(columnName, columnLength)
            .uniqueIndex()
            .default(generateUuid())
            .entityId()
    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }
}

fun generateUuid() = UUID.randomUUID().toString().substring(0, 20)