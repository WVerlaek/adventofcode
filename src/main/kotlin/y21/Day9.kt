package y21

import common.Day
import common.models.Cell
import common.models.Grid
import java.util.*

fun main() = Day9(2)

object Day9 : Day(2021, 9) {
    init {
//        useSampleInput {
//            """
//                2199943210
//                3987894921
//                9856789892
//                8767896789
//                9899965678
//            """.trimIndent()
//        }
    }

    val map = Grid(lines.size, lines[0].length) { row, col ->
        lines[row][col].digitToInt()
    }

    val lowPoints = map.cells().filter { cell ->
        map.neighbors(cell).all { neigh -> neigh.value > cell.value }
    }

    override fun level1(): String {
        return lowPoints.sumOf { cell -> cell.value + 1 }.toString()
    }

    override fun level2(): String {
        return lowPoints.map { p -> basinSize(p) }
            .sortedDescending()
            .also { println(it) }
            .take(3)
            .fold(1L) { acc, value -> acc * value }
            .toString()
    }

    private fun basinSize(lowPoint: Cell<Int>): Long {
        val queue = PriorityQueue<Cell<Int>>(compareBy { it.value })
        val visited = mutableSetOf<Cell<Int>>()
        var size = 0
        queue.add(lowPoint)

        while (queue.isNotEmpty()) {
            val next = queue.poll()
            if (next in visited || next.value >= 9) {
                continue
            }

            visited += next
            size++
            for (neigh in map.neighbors(next)) {
                queue += neigh
            }
        }

        return size.toLong()
    }
}
