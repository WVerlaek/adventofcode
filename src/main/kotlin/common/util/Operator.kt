package common.util

fun operator(left: Long, op: Char, right: Long): Long {
    return when (op) {
        '+' -> left + right
        '-' -> left - right
        '*' -> left * right
        '/' -> left / right
        else -> throw IllegalArgumentException("Unknown operator '$op'")
    }
}
