package common.puzzle

class Input(val string: String) {
    val lines by lazy { string.lines() }
    fun splitToInts(separator: String = ",") = string.splitToInts(separator)
    fun linesToInts() = lines.toInts()
}

fun String.splitToInts(separator: String = ",") = split(separator).map { it.toInt() }
fun String.splitToLongs(separator: String = ",") = split(separator).map { it.toLong() }
fun List<String>.toInts() = map { it.toInt() }
