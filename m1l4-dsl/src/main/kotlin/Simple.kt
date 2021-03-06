fun sout(block: () -> Any?) {
    val result = block()
    println(result)
}

class MyContext {
    fun time() = System.currentTimeMillis()
}

fun soutWithTimeStamp (block: MyContext.() -> Any?) {
    val context = MyContext()
    val result = block(context)
    println(result)
}

public infix fun String.time(value: String): String {
    return "$this:$value"
}

fun main() {
    val pair = Pair("key", "value")

    val pairNew = "key" to "value"

    val myTime = "12" time "30"
//    val a =
}