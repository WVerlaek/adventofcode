package common.ext

fun <T> MutableMap<T, Long>.mergeAll(other: Map<T, Long>) {
    other.forEach { (k, v) -> this[k] = (this[k] ?: 0L) + v }
}
