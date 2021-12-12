package y21

import common.datastructures.Cell
import common.datastructures.Grid
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import java.util.*

fun main() = solvePuzzle(2021, 9, 2) { Day9(it) }

class Day9(val input: Input) : Puzzle {
    val map = Grid(input.lines.size, input.lines[0].length) { row, col ->
        input.lines[row][col].digitToInt()
    }

    val lowPoints = map.cells().filter { cell ->
        map.neighbors(cell).all { neigh -> neigh.value > cell.value }
    }

    override fun solveLevel1(): Any {
        return lowPoints.sumOf { cell -> cell.value + 1 }
    }

    override fun solveLevel2(): Any {
        return lowPoints.map { p -> basinSize(p) }
            .sortedDescending()
            .also { println(it) }
            .take(3)
            .fold(1L) { acc, value -> acc * value }
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
