package common.ext

inline fun <T> T.repeatApply(times: Int, operation: (T) -> T): T {
    var result = this
    repeat(times) {
        result = operation(result)
    }
    return result
}
