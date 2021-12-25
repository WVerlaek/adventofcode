package common.ext

infix fun Int.toOrFrom(j: Int) = if (this <= j) this..j else j..this

operator fun IntRange.compareTo(i: Int): Int {
    return when {
        this.last < i -> -1
        this.first > i -> 1
        else -> 0
    }
}

fun IntRange.intersect(other: IntRange): IntRange {
    require(this.step ==  1)
    require(other.step == 1)
    return maxOf(first, other.first)..minOf(last, other.last)
}

val IntRange.size: Int
    get() {
        require(step == 1)
        return maxOf(0, last - first + 1)
    }

operator fun Int.compareTo(range: IntRange) = -range.compareTo(this)
