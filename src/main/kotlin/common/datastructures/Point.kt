package common.datastructures

data class Point(val col: Int, val row: Int) {
    val x = col
    val y = row
    constructor(str: String) : this(str.split(",")[0].toInt(), str.split(",")[1].toInt())
}
