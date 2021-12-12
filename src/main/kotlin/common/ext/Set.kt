package common.ext

fun <T> List<Set<T>>.intersectAll(): Set<T> {
    require(isNotEmpty())
    var result = this[0]
    for (i in 1 until size) {
        result = result.intersect(this[i])
    }
    return result
}

fun <T> List<Set<T>>.unionAll(): Set<T> {
    var result = emptySet<T>()
    for (set in this) {
        result = result.union(set)
    }
    return result
}
