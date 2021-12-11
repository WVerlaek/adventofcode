package common.util

fun String.splitToInts(separator: String = ",") = split(separator).map { it.toInt() }
fun List<String>.toInts() = map { it.toInt() }
