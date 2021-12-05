package common.models

data class Point(val row: Int, val col: Int) {
    constructor(str: String) : this(str.split(",")[0].toInt(), str.split(",")[1].toInt())

    fun swap() = Point(col, row)
}
