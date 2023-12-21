package common.util

fun gcd(a: Int, b: Int): Int = when {
    a < b -> gcd(b, a)
    a % b == 0 -> b
    else -> gcd(b, a % b)
}

fun gcd(a: Long, b: Long): Long = when {
    a < b -> gcd(b, a)
    a % b == 0L -> b
    else -> gcd(b, a % b)
}

data class ExtendedGcd(val gcd: Long, val x: Long, val y: Long)
fun extendedGcd(a: Long, b: Long): ExtendedGcd {
    var oldR = a
    var r = b
    var oldS = 1L
    var s = 0L
    var oldT = 0L
    var t = 1L

    while (r != 0L) {
        val quotient = oldR / r
        val newR = oldR - quotient * r
        oldR = r
        r = newR
        val newS = oldS - quotient * s
        oldS = s
        s = newS
        val newT = oldT - quotient * t
        oldT = t
        t = newT
    }

    return ExtendedGcd(oldR, oldS, oldT)
}

fun lcm(a: Int, b: Int): Int = (a * b) / gcd(a, b)
fun lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)
fun lcm(vararg values: Long): Long = values.reduce { acc, l -> lcm(acc, l) }

fun List<Int>.lcm() = reduce { acc, x: Int -> lcm(acc, x) }
fun List<Long>.lcm() = reduce { acc, x: Long -> lcm(acc, x) }
