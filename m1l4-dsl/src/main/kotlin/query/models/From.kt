package query.models

data class From(
    val table: String,
    val asAlias: String,
    val join: Join
)