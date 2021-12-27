package y21

import common.datastructures.Grid
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 25, 1) { Day25(it) }

enum class GridState(val c: Char) {
    Empty('.'), East('>'), South('v')
}

typealias OceanFloor = Grid<GridState>

class Day25(val input: Input) : Puzzle {
    val cellFormatter = Grid.CellFormatter<GridState>({ cell -> cell.value.c.toString() }, cellSeparator = "")
    val initialFloor: OceanFloor = Grid(input.lines.size, input.lines[0].length) { row, col ->
        GridState.values().first { it.c == input.lines[row][col] }
    }.also { it.cellFormatter = cellFormatter }

    fun OceanFloor.simulateStep(): OceanFloor? {
        val movedEast = moveEast()
        val movedSouth = (movedEast ?: this).moveSouth()
        return movedSouth ?: movedEast
    }

    private fun OceanFloor.moveEast(): OceanFloor? {
        val moveEast = Grid(numRows, numCols) { _, _ -> GridState.Empty }.also { it.cellFormatter = cellFormatter }
        var moves = 0
        this.cells().forEach { cell ->
            if (cell.value != GridState.East) {
                if (cell.value != GridState.Empty) {
                    moveEast[cell.row][cell.col].value = cell.value
                }
                return@forEach
            }

            // Check if it can move.
            val nextCol = (cell.col + 1) % numCols
            if (this[cell.row][nextCol].value == GridState.Empty) {
                moves++
                moveEast[cell.row][nextCol].value = GridState.East
            } else {
                // Can't move.
                moveEast[cell.row][cell.col].value = GridState.East
            }
        }

        return if(moves == 0) null else moveEast
    }

    private fun OceanFloor.moveSouth(): OceanFloor? {
        val moveSouth = Grid(numRows, numCols) { _, _ -> GridState.Empty }.also { it.cellFormatter = cellFormatter }
        var moves = 0
        this.cells().forEach { cell ->
            if (cell.value != GridState.South) {
                if (cell.value != GridState.Empty) {
                    moveSouth[cell.row][cell.col].value = cell.value
                }
                return@forEach
            }

            // Check if it can move.
            val nextRow = (cell.row + 1) % numRows
            if (this[nextRow][cell.col].value == GridState.Empty) {
                moves++
                moveSouth[nextRow][cell.col].value = GridState.South
            } else {
                // Can't move.
                moveSouth[cell.row][cell.col].value = GridState.South
            }
        }

        return if(moves == 0) null else moveSouth
    }

    override fun solveLevel1(): Any {
        var floor = initialFloor
        var steps = 1
        while (true) {
            val next = floor.simulateStep()
                ?: return steps
            steps++
            floor = next
        }
    }

    override fun solveLevel2(): Any {
        return 0
    }
}
