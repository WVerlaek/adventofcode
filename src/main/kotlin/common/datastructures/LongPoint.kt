package common.datastructures

import kotlin.math.abs


data class LongPoint(val col: Long, val row: Long) {
    val x = col
    val y = row

    companion object {
        val Zero = LongPoint(0, 0)

        fun parse(line: String): LongPoint {
            val (x, y) = line.split(",").map { it.toLong() }
            return LongPoint(x, y)
        }
    }

    operator fun plus(other: LongPoint) = LongPoint(col + other.col, row + other.row)
    operator fun minus(other: LongPoint) = LongPoint(col - other.col, row - other.row)
    operator fun times(n: Long) = LongPoint(col * n, row * n)

    fun manhattanDistTo(other: LongPoint) = abs(x - other.x) + abs(y - other.y)

    fun inBounds(numRows: Long, numCols: Long): Boolean {
        return x in 0..<numCols && y in 0..<numRows
    }
}