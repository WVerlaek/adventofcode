package common.ext

fun Boolean.toInt() = if (this) 1 else 0

fun List<Int>.product() = fold(1) { a, b -> a * b }
fun List<Long>.product() = fold(1L) { a, b -> a * b }

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun <T> Iterable<T>.productOf(selector: (T) -> Int): Int {
    var prod = 1
    for (e in this) {
        prod *= selector(e)
    }
    return prod
}

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun <T> Iterable<T>.productOf(selector: (T) -> Long): Long {
    var prod = 1L
    for (e in this) {
        prod *= selector(e)
    }
    return prod
}

fun Int.divideRoundUp(divideBy: Int) = (this + divideBy - 1) / divideBy
