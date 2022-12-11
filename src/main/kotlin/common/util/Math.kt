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

fun lcm(a: Int, b: Int): Int = (a * b) / gcd(a, b)
fun lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)

fun List<Int>.lcm() = reduce { acc, x: Int -> lcm(acc, x) }
fun List<Long>.lcm() = reduce { acc, x: Long -> lcm(acc, x) }
