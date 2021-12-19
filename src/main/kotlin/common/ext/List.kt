package common.ext

fun <T> MutableList<T>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun <T> List<T>.requireSingleElement(): T {
    require(size == 1)
    return first()
}

fun <T> List<T>.other(t: T): T {
    require(size == 2)
    if (t == this[0]) return this[1]
    if (t == this[1]) return this[0]
    throw IllegalArgumentException("$t not found in list $this")
}
