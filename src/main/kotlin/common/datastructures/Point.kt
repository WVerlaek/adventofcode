package common.datastructures

import kotlin.math.*

data class Point(val col: Int, val row: Int) {
    val x = col
    val y = row

    companion object {
        val Zero = Point(0, 0)

        fun parse(line: String): Point {
            val (x, y) = line.split(",").map { it.toInt() }
            return Point(x, y)
        }
    }

    operator fun plus(other: Point) = Point(col + other.col, row + other.row)
    operator fun minus(other: Point) = Point(col - other.col, row - other.row)
    operator fun times(n: Int) = Point(col * n, row * n)

    fun manhattanDistTo(other: Point) = abs(x - other.x) + abs(y - other.y)

    fun inBounds(numRows: Int, numCols: Int): Boolean {
        return x in 0..<numCols && y in 0..<numRows
    }
}
