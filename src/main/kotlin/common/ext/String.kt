package common.ext

fun String.substringBetween(delimiterBefore: String, delimiterAfter: String): String {
    return substringAfter(delimiterBefore).substringBefore(delimiterAfter)
}