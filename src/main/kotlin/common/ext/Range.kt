package common.ext

infix fun Int.toOrFrom(j: Int) = if (this <= j) this..j else j..this

operator fun IntRange.compareTo(i: Int): Int {
    return when {
        this.last < i -> -1
        this.first > i -> 1
        else -> 0
    }
}

operator fun Int.compareTo(range: IntRange) = -range.compareTo(this)
