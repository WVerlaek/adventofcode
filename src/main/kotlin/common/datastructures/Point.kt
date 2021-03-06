package common.datastructures

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
}
