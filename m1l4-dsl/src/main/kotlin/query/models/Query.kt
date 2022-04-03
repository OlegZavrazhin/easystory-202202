package query.models

// TODO: is it OK?
import query.dsl.SelectContext

data class Query (
    val select: SelectContext
)