package common.ext

fun Boolean.toInt() = if (this) 1 else 0

fun List<Int>.product() = fold(1) { a, b -> a * b }
fun List<Long>.product() = fold(1L) { a, b -> a * b }
