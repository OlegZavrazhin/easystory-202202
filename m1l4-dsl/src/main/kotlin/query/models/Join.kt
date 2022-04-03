package query.models

data class Join(
    val table: String,
    val asAlias: String,
    val on: String
)