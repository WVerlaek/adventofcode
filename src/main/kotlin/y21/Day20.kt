package y21

import common.datastructures.Grid
import common.ext.repeatApply
import common.ext.toInt
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 20, 2) { Day20(it) }

data class InfiniteImage(val grid: Grid<Boolean>, val outerFillValue: Boolean)
typealias EnhancementAlgo = List<Boolean>

fun InfiniteImage.enhance(enhancement: EnhancementAlgo): InfiniteImage {
    // Simulates the border of the infinite image.
    val allFalseNext = enhancement[0]
    val allTrueNext = enhancement[511]

    // Expand image grid by 1 in every direction.
    val newImageGrid = Grid(grid.numRows + 2, grid.numCols + 2) { _, _ -> outerFillValue }

    newImageGrid.cells().forEach { cell ->
        var result = 0
        for (i in 0..8) {
            val dx = (i % 3) - 1
            val dy = (i / 3) - 1

            val row = cell.row + dy
            val col = cell.col + dx
            val v = when {
                grid.withinBounds(row - 1, col - 1) -> grid[row - 1][col - 1].value.toInt()
                else -> outerFillValue.toInt()
            }

            result += v shl (8 - i)
        }

        cell.value = enhancement[result]
    }

    val newOuterFill = if (outerFillValue) allTrueNext else allFalseNext
    return InfiniteImage(newImageGrid, newOuterFill)
}

class Day20(val input: Input) : Puzzle {
    private val enhancement = input.lines[0].map { it == '#' }

    private val originalImage = InfiniteImage(Grid(input.lines.size - 2, input.lines[2].length) { row, col ->
        input.lines[row + 2][col] == '#'
    }, false)

    override fun solveLevel1(): Any {
        return originalImage
            .repeatApply(2) { it.enhance(enhancement) }
            .also { println(it.grid) }
            .grid.cells().count { it.value }
    }

    override fun solveLevel2(): Any {
        return originalImage
            .repeatApply(50) { it.enhance(enhancement) }
            .also { println(it.grid) }
            .grid.cells().count { it.value }
    }
}
