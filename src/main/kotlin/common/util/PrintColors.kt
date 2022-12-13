package common.util

object PrintColors {
    private val boldGreen = "\u001B[1;32m"
    private val boldRed = "\u001B[1;31m"
    private val reset = "\u001B[0m"

    fun success(s: String): String {
        return "$boldGreen$s$reset"
    }

    fun error(s: String): String {
        return "$boldRed$s$reset"
    }
}

fun printSuccess(s: String) {
    println(PrintColors.success(s))
}

fun printError(s: String) {
    println(PrintColors.error(s))
}
