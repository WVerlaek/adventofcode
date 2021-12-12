package y21

import common.Day
import common.models.Cell
import common.models.Grid
import java.util.*

fun main() = Day11(2)

object Day11 : Day(2021, 11) {
    init {
//        useSampleInput {
//            """
//                5483143223
//                2745854711
//                5264556173
//                6141336146
//                6357385478
//                4167524645
//                2176841721
//                6882881134
//                4846848554
//                5283751526
//            """.trimIndent()
//        }
    }

    val startingGrid = Grid(lines.size, lines[0].length) { r, c -> lines[r][c].digitToInt()}

    fun Grid<Int>.simulateStep(): Pair<Grid<Int>, Int> {
        val new = copy()
        var flashes = 0
        val queue = LinkedList<Cell<Int>>()

        fun incCell(cell: Cell<Int>) {
            cell.value++
            if (cell.value == 10) {
                flashes++
                new.neighbors(cell, true).forEach { neigh ->
                    queue += neigh
                }
            }
        }

        new.cells().forEach(::incCell)

        while (queue.isNotEmpty()) {
            val cell = queue.pop()
            incCell(cell)
        }

        new.cells().forEach { cell -> if (cell.value >= 10) cell.value = 0 }

        return new to flashes
    }

    override fun level1(): String {
        val steps = 100
        var grid = startingGrid
        var totalFlashes = 0L
        for (i in 0 until steps) {
            val (next, flashes) = grid.simulateStep()
            grid = next
            totalFlashes += flashes
        }
        return totalFlashes.toString()
    }

    override fun level2(): String {
        var grid = startingGrid
        var step = 1
        while (true) {
            val (next, flashes) = grid.simulateStep()
            if (flashes == 100) {
                return step.toString()
            }
            grid = next
            step++
        }
    }
}
