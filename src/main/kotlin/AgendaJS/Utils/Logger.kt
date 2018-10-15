package AgendaJS.Utils

object Logger {
    var minLogLevel: LogLevel = LogLevel.INFO

    fun debug(message: String) = print(message, LogLevel.DEBUG)
    fun info(message: String)  = print(message, LogLevel.INFO)
    fun warn(message: String)  = print(message, LogLevel.WARN)
    fun error(message: String) = print(message, LogLevel.ERROR)


    private fun print(message: String, level: LogLevel) {
        if(level.ordinal >= minLogLevel.ordinal)
            println("${level.color}[Logger/$level]: $message${AnsiColor.RESET}")
    }
}

enum class LogLevel(val color: AnsiColor) {
    DEBUG(AnsiColor.GREY),
    INFO(AnsiColor.RESET),
    WARN(AnsiColor.BLACK_BOLD),
    ERROR(AnsiColor.RED);
}

internal enum class AnsiColor(private val code: String) {
    BLACK("\u001B[0;30m"),
    BLACK_BOLD("\u001B[1;30m"),
    RED("\u001B[0;31m"),
    GREY("\u001B[0;37m"),
    RESET("\u001B[0m");//Equivalent to uncolored

    override fun toString() = code
}