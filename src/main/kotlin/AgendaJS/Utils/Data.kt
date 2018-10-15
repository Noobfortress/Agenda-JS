package AgendaJS.Utils

data class Week(
        val number: Int,
        val mon: Day,
        val tue: Day,
        val wed: Day,
        val thu: Day,
        val fri: Day,
        val sat: Day,
        val sun: Day
) {
    val days = arrayOf(mon, tue, wed, thu, fri, sat, sun)
}

/**
 * Class representing a day's schedule
 * A day has a name, as defined by [Weekday], a date, and a list of events
 *
 * @param name The day's name
 * @see Weekday
 * @param date The date at which the day takes place
 * @param events The events which take place on this day
 */
data class Day(val name: Weekday, val date: SimpleDate, val events: Array<Event>) {
    override fun toString() = "Day (${name.dayname} $date: ${events.contentDeepToString()})"

    /** Proper implementations because of [events] being an array */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Day)  return false

        if (name != other.name) return false
        if (date != other.date) return false
        if (!events.contentDeepEquals(other.events)) return false

        return true
    }
    override fun hashCode(): Int =
            31 * (31 * name.hashCode()+ date.hashCode()) + events.contentDeepHashCode()
}

/**
 * Class representing an event in the agenda
 *
 * @param name The event's full display name
 * @param short The event's shorthand name (e.g. Wiskunde B -> wisb, Nederlands -> nl)
 * @param location The location at which the event takes place
 * @param start The time at which the event starts
 * @param end The time at which the event ends
 */
data class Event(val name: String, val short: String, val location: String, val start: SimpleTime, val end: SimpleTime) {
    /** Derives the value of 'data-content' based off of [name] */
    fun getDataContent() = "events/event-${name.toLowerCase().replace(' ', '-')}"
}

/**
 * Simple representation of a date
 * Dates are represented as dd/mm
 */
data class SimpleDate(val day: Int, val month: Int) {
    init {
        if (month < 1 || month > 12)
            throw IllegalArgumentException("Invalid given month! ($month)")
        if (day < 1 || day > when (month) {
                    1  -> 31; 2  -> 28; 3  -> 31
                    4  -> 30; 5  -> 31; 6  -> 30
                    7  -> 31; 8  -> 31; 9  -> 30
                    10 -> 31; 11 -> 30; 12 -> 31
                    else -> throw IllegalStateException("Someone's been tampering with the universe!")
        })
            throw IllegalArgumentException("Invalid given day! ($day)")
    }
    /** Simple date representation */
    override fun toString() = "$day/$month"
}

/**
 * Simple representation of time
 * Time is represented as <hours>:<minutes>
 *
 * If you want to input time as a string,
 * use [decode] to get the hour & minute values
 */
data class SimpleTime(val hours: Int, val minutes: Int) {
    companion object {
        /** Correctly formatted time matches this expression */
        private val regex = Regex("(\\d+):(\\d{2})")

        /**
         * Tries to extract time values from the input [time]
         * [time] should be formatted like (h)h:mm, where h=hours & m=minutes
         *
         * @return The hour & minute values from [time], (0,0) by default
         */
        fun decode(time: String): Pair<Int, Int> {
            if (!regex.matches(time)) {
                Logger.warn("Input time is invalid!")
                return 0 to 0
            }
            val matches = regex.find(time)!!.groups
            val hours = matches[1]!!.value.toInt(); val minutes = matches[2]!!.value.toInt()
            if (hours >= 24 || minutes >= 60) {
                Logger.warn("Input time is invalid!")
                return 0 to 0
            }
            return hours to minutes
        }
    }
    /** Simple time representation */
    override fun toString() = "$hours:${minutes.toString().padStart(2, '0')}"
}

enum class Weekday(val dayname: String, val short: String) {
    MONDAY    ("Maandag",   "mon"),
    TUESDAY   ("Dinsdag",   "tue"),
    WEDNESDAY ("Woensdag",  "wed"),
    THURSDAY  ("Donderdag", "thu"),
    FRIDAY    ("Vrijdag",   "fri"),
    SATURDAY  ("Zaterdag",  "sat"),
    SUNDAY    ("Zondag",    "sun")
}