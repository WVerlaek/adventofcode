package common.puzzle

class Input(val string: String) {
    val lines
        get() = string.lines()

    fun splitToInts(separator: String = ",") = string.split(separator).map { it.toInt() }
    fun linesToInts() = lines.map { it.toInt() }
}
