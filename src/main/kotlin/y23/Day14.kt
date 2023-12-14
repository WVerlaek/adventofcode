package y23

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2023, day = 14) { Day14(it) }

class Day14(val input: Input) : Puzzle {

    enum class Rock(val c: Char) {
        None('.'), Square('#'), Round('O')
    }

    private fun parseGrid(lines: List<String>): Grid<Rock> {
        return Grid(lines.size, lines[0].length) { r, c ->
            Rock.entries.first { it.c == lines[r][c] }
        }
    }

    enum class Direction(val dRow: Int, val dCol: Int) {
        North(-1, 0), West(0, -1), South(1, 0), East(0, 1),
    }

    private fun Grid<Rock>.roll(dir: Direction) {
        fun tryRollRock(row: Int, col: Int) {
            if (this[row][col].value != Rock.Round) {
                return
            }

            var stopBeforeRow = row + dir.dRow
            var stopBeforeCol = col + dir.dCol
            while (stopBeforeRow in rows.indices && stopBeforeCol in 0 until numCols && this[stopBeforeRow][stopBeforeCol].value == Rock.None) {
                stopBeforeRow += dir.dRow
                stopBeforeCol += dir.dCol
            }

            this[row][col].value = Rock.None
            this[stopBeforeRow - dir.dRow][stopBeforeCol - dir.dCol].value = Rock.Round
        }

        when (dir) {
            Direction.North, Direction.West -> {
                for (row in 0 until numRows) {
                    for (col in 0 until numCols) {
                        tryRollRock(row, col)
                    }
                }
            }
            Direction.East, Direction.South -> {
                for (row in numRows - 1 downTo 0) {
                    for (col in numCols - 1 downTo 0) {
                        tryRollRock(row, col)
                    }
                }
            }
        }

    }

    private fun Grid<Rock>.totalLoad(): Int {
        return cells().sumOf { (row, _, value) ->
            when (value) {
                Rock.Round -> numRows - row
                else -> 0
            }
        }
    }

    override fun solveLevel1(): Any {
        return parseGrid(input.lines)
            .also { it.roll(Direction.North) }
            .totalLoad()

    }

    override fun solveLevel2(): Any {
        val grid = parseGrid(input.lines)
        val seen = mutableMapOf<Grid<Rock>, Int>()
        var i = 0
        val n = 1000000000
        while (i < n) {
            Direction.entries.forEach { dir -> grid.roll(dir) }

            val key = grid.copy()
            if (key in seen) {
                // Found cycle
                val cycleLen = i - seen.getValue(key)
                val remainingCycles = (n - i) / cycleLen
                i += remainingCycles * cycleLen + 1
            } else {
                seen[grid.copy()] = i
                i++
            }

        }
        return grid.totalLoad()
    }
}
