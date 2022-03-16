package user.dsl

import user.models.Action
import user.models.User
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import java.util.*

// val user = user {
//            name {
//                first = "Kirill"
//                last = "Krylov"
//            }
//            contacts {
//                email = ""
//                phone = "81234567890"
//            }
//            actions {
//                add(Actions.UPDATE)
//                add(Actions.ADD)
//
//                +Actions.DELETE
//                +Actions.READ
//            }
//            availability {
//                monday("11:30")
//                friday("18:00")
//            }
//        }
@UserDSL
class NameContext {
    var first: String = ""
    var second: String = ""
    var last: String = ""
}
@UserDSL
class ContactsContext {
    var email: String = ""
    var phone: String = ""
    var last: String = ""
}
@UserDSL
class ActionsContext {
    private val _actions: MutableSet<Action> = mutableSetOf()

    val actions: Set<Action>
        get() = _actions.toSet()

    fun add(action: Action) {
        _actions.add(action)
    }

    fun add(action: String) {
        add(Action.valueOf(action))
    }

    operator fun Action.unaryPlus() {
        add(this)
    }

    operator fun String.unaryPlus() {
        add(this)
    }
}
@UserDSL
class AvailabilityContext {
    private val _availabilities: MutableList<LocalDateTime> = mutableListOf()

    val availabilities: List<LocalDateTime>
        get() = _availabilities.toList()

    fun sunday(time: String) = dayTimeOfWeek(DayOfWeek.SUNDAY, time)
    fun monday(time: String) = dayTimeOfWeek(DayOfWeek.MONDAY, time)
    fun tuesday(time: String) = dayTimeOfWeek(DayOfWeek.TUESDAY, time)
    fun wednesday(time: String) = dayTimeOfWeek(DayOfWeek.WEDNESDAY, time)
    fun thursday(time: String) = dayTimeOfWeek(DayOfWeek.THURSDAY, time)
    fun friday(time: String) = dayTimeOfWeek(DayOfWeek.FRIDAY, time)
    fun saturday(time: String) = dayTimeOfWeek(DayOfWeek.SATURDAY, time)

    fun dayTimeOfWeek(day: DayOfWeek, time: String) {
        val dDay = LocalDate.now().with(TemporalAdjusters.next(day))
        val dTime = time.split(":")
            .map { it.toInt() }
            .let { LocalTime.of(it[0], it[1]) }

        _availabilities.add(LocalDateTime.of(dDay, dTime))
    }
}
@UserDSL
class UserBuilder {
    private var id = UUID.randomUUID().toString()
    private var firstName = ""
    private var secondName = ""
    private var lastName = ""
    private var phone = ""
    private var email = ""

    private var actions = emptySet<Action>()
    private var available = emptyList<LocalDateTime>()

    fun name(block: NameContext.() -> Unit) {
        val ctx = NameContext().apply(block)

        firstName = ctx.first
        secondName = ctx.second
        lastName = ctx.last
    }

    fun contacts(block: ContactsContext.() -> Unit) {
        val ctx = ContactsContext().apply(block)

        phone = ctx.phone
        email = ctx.email
    }
    @UserDSL
    fun actions(block: ActionsContext.() -> Unit) {
        val ctx = ActionsContext().apply(block)

        actions = ctx.actions
    }

    @UserDSL
    fun availability(block: AvailabilityContext.() -> Unit) {
        val ctx = AvailabilityContext().apply(block)

        available = ctx.availabilities
    }

    fun build() = User(
        id,
        firstName,
        secondName,
        lastName,
        phone,
        email,
        actions,
        available,
    )
}

@UserDSL1
fun user(block: UserBuilder.() -> Unit) = UserBuilder().apply(block).build()

@DslMarker
annotation class UserDSL

@DslMarker
annotation class UserDSL1