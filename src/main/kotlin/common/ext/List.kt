package common.ext

fun <T> MutableList<T>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun <T> List<T>.requireSingleElement(): T {
    require(size == 1) { "Was: $this" }
    return first()
}

fun <T> List<T>.other(t: T): T {
    require(size == 2)
    if (t == this[0]) return this[1]
    if (t == this[1]) return this[0]
    throw IllegalArgumentException("$t not found in list $this")
}

fun <T> MutableList<T>.removeLast(n: Int): List<T> {
    val removed = subList(size - n, size).toList()
    for (i in 0 until n) this.removeAt(size - 1)
    return removed
}
