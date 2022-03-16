fun main() {
//    firstTask()
    secondTask()
}

fun firstTask() {
    val whatTypeIAm = 3 // Int
    println("whatTypeIAm is Int: ${whatTypeIAm is Int}")
    val correctType: Double = 3.4 // Double
    println("correctType is Double: ${correctType is Double}")
//    val whatTheResult: Int = 3L // Ошибка
//    println("whatTheResult: ${whatTheResult}")
    val string = "someString"
    val charVariable: Char = string[2] // m
    println("charVariable: ${charVariable}")
    val someAny: Any = "hello"
    println("someAny : ${someAny is Double}") // false
}

fun secondTask() {
    var nullable = null
    val whatTypeIAm =  nullable?.toDouble() // null (WRONG) Double?
    println("whatTypeIAm is Double?: ${whatTypeIAm is Double?}")
    println("whatTypeIAm === null: ${whatTypeIAm === null}")
    val guessType = whatTypeIAm ?: 4.5 // Double 4.5
    println("guessType is Double: ${guessType is Double}")
    println("guessType: ${guessType}")
    val guessType2 = nullable?.toLong() ?: 5 //  Int 5 (WRONG) Long
    println("guessType2 is Long: ${guessType2 is Long}")
    println("guessType2: ${guessType2}")
    val guessType3 = guessType?.toInt() ?: 2 // Int 4
    println("guessType3 is Int: ${guessType3 is Int}")
    println("guessType3: ${guessType3}")
    var anyType: Any = 12
    println("anyType: ${anyType}")
    val castFromAny = anyType as? String // String (WRONG) null
    println("castFromAny: ${castFromAny}")
    println("castFromAny is Any?: ${castFromAny is Any?}")
    println("castFromAny === null: ${castFromAny === null}")
//    println("castFromAny: ${castFromAny is Int}")
}