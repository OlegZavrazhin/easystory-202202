import org.junit.Test
import query.dsl.query
import kotlin.test.assertEquals

class DBQueryTestCase {
    @Test
    fun `test query`() {
        val query = query {
            select {
//                id = 123
//                value =  "DummyValue"
//                created = "12.20.2021"
                from {
                    table = "table"
                    asAlias = "t"
                    join {
                        table = "tableJoined"
                        asAlias = "tj"
                        on = "t.value = tj.value"
                    }
                }
            }
        }

        assertEquals(
            "select id, value, created from table as t join tableJoined as tj on t.value = tj.value",
            query
        )
    }
}