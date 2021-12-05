package y21

import common.Day
import common.models.Grid
import common.models.Line
import common.models.Point
import common.util.toOrFrom

fun main() = Day5(2)

object Day5 : Day(2021, 5) {
    init {
//        useSampleInput {
//            """
//                0,9 -> 5,9
//                8,0 -> 0,8
//                9,4 -> 3,4
//                2,2 -> 2,1
//                7,0 -> 7,4
//                6,4 -> 2,0
//                0,9 -> 2,9
//                3,4 -> 1,4
//                0,0 -> 8,8
//                5,5 -> 8,2
//            """.trimIndent()
//        }
    }

    val ventLines = lines.map { line ->
        val split = line.split(" -> ")
        val p0 = Point(split[0]).swap()
        val p1 = Point(split[1]).swap()
        Line(p0, p1)
    }

    val width = ventLines.maxOf { line -> maxOf(line.from.col, line.to.col) } + 1
    val height = ventLines.maxOf { line -> maxOf(line.from.row, line.to.row) } + 1
    val grid = Grid(height, width) { _, _ -> 0 }

    init {
        grid.cellFormatter = {
            when (it.value) {
                0 -> "."
                else -> it.value.toString()
            }
        }
    }

    override fun level1(): String {
        ventLines.forEach { line ->
            if (line.from.row == line.to.row) {
                // Horizontal line.
                (line.from.col toOrFrom line.to.col)
                    .forEach { i -> grid[line.from.row][i].value++ }
            } else if (line.from.col == line.to.col) {
                // Vertical line.
                (line.from.row toOrFrom line.to.row)
                    .forEach { i -> grid[i][line.from.col].value++ }
            } else {
                // Angled line. Ignore.
            }
        }
        println(grid)
        return grid.cells().count { it.value > 1 }.toString()
    }

    override fun level2(): String {
        ventLines.forEach { line ->
            if (line.from.row == line.to.row) {
                // Horizontal line.
                (line.from.col toOrFrom line.to.col)
                    .forEach { i -> grid[line.from.row][i].value++ }
            } else if (line.from.col == line.to.col) {
                // Vertical line.
                (line.from.row toOrFrom line.to.row)
                    .forEach { i -> grid[i][line.from.col].value++ }
            } else {
                // Diagonal line.
                if (line.from.row <= line.to.row) {
                    (line.from.row..line.to.row)
                        .forEachIndexed { index, row ->
                            val col = if (line.from.col <= line.to.col) line.from.col + index else line.from.col - index
                            grid[row][col].value++
                        }
                } else {
                    (line.to.row..line.from.row)
                        .forEachIndexed { index, row ->
                            val col = if (line.to.col <= line.from.col) line.to.col + index else line.to.col - index
                            grid[row][col].value++
                        }
                }
            }
        }
        println(grid)
        return grid.cells().count { it.value > 1 }.toString()
    }
}
