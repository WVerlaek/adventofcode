package common.util

infix fun Int.toOrFrom(j: Int) = if (this <= j) this..j else j..this
