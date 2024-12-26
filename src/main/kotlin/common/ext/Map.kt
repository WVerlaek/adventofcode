package common.ext

fun <T> MutableMap<T, Long>.mergeAll(other: Map<T, Long>) {
    other.forEach { (k, v) -> this[k] = (this[k] ?: 0L) + v }
}

fun <Key> MutableMap<Key, Int>.addOrSet(key: Key, toAdd: Int) {
    this[key] = this.getOrDefault(key, 0) + toAdd
}

fun <K, V> MutableMap<K, V>.swap(a: K, b: K) {
    val aValue = getValue(a)
    val bValue = getValue(b)
    this[a] = bValue
    this[b] = aValue
}