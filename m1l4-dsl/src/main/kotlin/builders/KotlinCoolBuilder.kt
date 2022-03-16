package builders.new

enum class Drink {
    WATER,
    COFFEE,
    TEA
}

abstract class Meal {
    data class Breakfast(
        val eggs: Int,
        val bacon: Boolean,
        val drink: Drink,
        val title: String
    ) : Meal() // наследуется от Meal
}

class BreakfastBuilder {
    var  eggs = 0
    var  bacon = false
    var  title = ""
    var  drink = Drink.WATER

//    fun withEggs(amount: Int): BreakfastBuilder {
//        this.eggs  = amount
//        return this
//    }
//
//    fun withBacon(value: Boolean): BreakfastBuilder {
//        this.bacon  = value
//        return this
//    }
//
//    fun withTitle(value: String): BreakfastBuilder {
//        this.title  = value
//        return this
//    }
//
//    fun withDrink(value: Drink): BreakfastBuilder {
//        this.drink  = value
//        return this
//    }

    fun build() = Meal.Breakfast(eggs, bacon, drink, title)
}

fun BreakfastBuilder.block() {
//    this.bacon
}

fun breakfast(block: BreakfastBuilder.() -> Unit): Meal.Breakfast {
    return BreakfastBuilder().apply(block).build()
}

fun main() {
    val myBreakfast = breakfast {
        eggs = 3
        bacon = true
        title = "Simple"
        drink = Drink.COFFEE
    }
//    val myBreakfast = BreakfastBuilder().apply {
//        eggs = 3
//        bacon = true
//        title = "Simple"
//        drink = Drink.COFFEE
//    }.build()
//    val breakfast = BreakfastBuilder()
//        .withEggs(3)
//        .withBacon(true)
//        .withTitle("Simple")
//        .withDrink(Drink.COFFEE)
//        .build()

    println(myBreakfast)
}