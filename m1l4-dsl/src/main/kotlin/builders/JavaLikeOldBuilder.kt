package builders

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
    private var  eggs = 0
    private var  bacon = false
    private var  title = ""
    private var  drink = Drink.WATER

    fun withEggs(amount: Int): BreakfastBuilder {
        this.eggs  = amount
        return this
    }

    fun withBacon(value: Boolean): BreakfastBuilder {
        this.bacon  = value
        return this
    }

    fun withTitle(value: String): BreakfastBuilder {
        this.title  = value
        return this
    }

    fun withDrink(value: Drink): BreakfastBuilder {
        this.drink  = value
        return this
    }

    fun build() = Meal.Breakfast(eggs, bacon, drink, title)
}

fun main() {
    val breakfast = BreakfastBuilder()
        .withEggs(3)
        .withBacon(true)
        .withTitle("Simple")
        .withDrink(Drink.COFFEE)
        .build()

    println(breakfast)
}