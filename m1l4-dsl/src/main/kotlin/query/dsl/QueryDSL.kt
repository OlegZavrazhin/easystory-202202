package query.dsl

import query.models.Query

//val query = query {
//    select {
//        id = 123
//        value =  "DummyValue"
//        created = "12.20.2021"
//        from {
//            table = "table"
//            asAlias = "t"
//            join {
//                table = "tableJoined"
//                asAlias = "tj"
//                on = "t.value = tj.value"
//            }
//        }
//    }
//}

class SelectContext  {
    lateinit var from: FromContext
}

class FromContext {
    var table: String = ""
    var asAlias: String = ""
    lateinit var join: JoinContext
}

class JoinContext {
    var table: String = ""
    var asAlias: String = ""
    var on: String = ""
}

class QueryBuilder {
    private var select = SelectContext()

    fun select(block: SelectContext.() -> Unit) {
        val ctx = SelectContext().apply(block)

        select = ctx
    }

    fun build() = Query(
        select
    )

}

//class SelectBuilder {
//
//    fun from(block: FromContext.() -> Unit) {
//        val ctx = FromContext().apply(block)
//
//    }
//
//
//}

class FromBuilder {
    private var table = ""
    private var asAlias = ""
    private var join = JoinContext()

    fun join(block: JoinContext.() -> Unit) {
        val ctx = JoinContext().apply(block)

        join = ctx
    }

    fun table(block: FromContext.() -> Unit) {
        val ctx = FromContext().apply(block)

        table = ctx.table
    }

    fun asAlias(block: FromContext.() -> Unit) {
        val ctx = FromContext().apply(block)

        asAlias = ctx.asAlias
    }
}

class JoinBuilder {
    private var table = ""
    private var asAlias = ""
    private var join = JoinContext()

    fun join(block: JoinContext.() -> Unit) {
        val ctx = JoinContext().apply(block)

        table = ctx.table
        asAlias = ctx.asAlias
        join = ctx
    }

}

//class JoinBuilder {
//    private var table = ""
//    private var asAlias = ""
//    private var on = ""
//}
fun query(block: QueryBuilder.() -> Unit) = QueryBuilder().apply(block).build()