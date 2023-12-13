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


fun main() = solvePuzzle(year = 2023, day = 13, dryRun = false) { Day13(it) }

class Day13(val input: Input) : Puzzle {

    data class Pattern(val grid:Grid<Boolean>)

    data class Mirror(val offset: Int, val isHorizontal: Boolean) {
        val score = if (isHorizontal) 100 * offset else offset
    }

    fun parsePatterns(lines: List<String>): List<Pattern> {
        val patternStarts = listOf(-1) + lines.mapIndexedNotNull { index, line ->
            if (line.isEmpty()) index else null
        } + listOf(lines.size)

        return patternStarts.windowed(2) { (beforeStart, end) ->
            val patternLines = lines.subList(beforeStart + 1, end)
            val grid = Grid(patternLines.size, patternLines[0].length) { r, c ->
                patternLines[r][c] == '#'
            }
            Pattern(grid)
        }
    }

    fun Pattern.findMirror(withSmudge: Boolean, originalMirror: Mirror?): Mirror {
        return findHorizontalMirror(usedSmudge = !withSmudge, originalMirror = originalMirror)
            ?: findVerticalMirror(usedSmudge = !withSmudge, originalMirror = originalMirror)!!
    }

    fun Pattern.findHorizontalMirror(col: Int = 0, offset: Int = 0, usedSmudge: Boolean = false, originalMirror: Mirror?): Mirror? {
        if (col >= grid.numCols) {
            if (!usedSmudge) return null
            return Mirror(offset, true).also {
                if (it == originalMirror) return null
            }
        }

        fun isMirror(offset: Int): Boolean {
            var i = 0
            while (offset + i < grid.numRows && offset - i - 1 >= 0) {
                if (grid[offset + i][col].value != grid[offset - i - 1][col].value) {
                    return false
                }
                i++
            }
            return true
        }

        if (col == 0) {
            // Try all offsets
            for (row in 1 until grid.numRows) {
                if (isMirror(row)) {
                    findHorizontalMirror(1, row, usedSmudge, originalMirror)?.let { return it }
                }

                if (!usedSmudge) {
                    // Try with smudge
                    for (smudgeRow in 0 until grid.numRows) {
                        grid[smudgeRow][col].value = !grid[smudgeRow][col].value

                        var found: Mirror? = null
                        if (isMirror(row)) {
                            found = findHorizontalMirror(1, row, true, originalMirror)
                        }

                        // Undo
                        grid[smudgeRow][col].value = !grid[smudgeRow][col].value

                        if (found != null) {
                            return found
                        }
                    }
                }
            }
            return null
        }

        if (!usedSmudge) {
            for (smudgeRow in 0 until grid.numRows) {
                grid[smudgeRow][col].value = !grid[smudgeRow][col].value

                var found: Mirror? = null
                if (isMirror(offset)) {
                    found = findHorizontalMirror(col + 1, offset, true, originalMirror)
                }

                // Undo
                grid[smudgeRow][col].value = !grid[smudgeRow][col].value

                if (found != null) {
                    return found
                }
            }
        }

        if (!isMirror(offset)) {
            return null
        }

        return findHorizontalMirror(col + 1, offset, usedSmudge, originalMirror)
    }

    fun Pattern.findVerticalMirror(row: Int = 0, offset: Int = 0, usedSmudge: Boolean = false, originalMirror: Mirror?): Mirror? {
        if (row >= grid.numRows) {
            if (!usedSmudge) return null
            return Mirror(offset, false).also {
                if (it == originalMirror) return null
            }
        }

        fun isMirror(offset: Int): Boolean {
            var i = 0
            while (offset + i < grid.numCols && offset - i - 1 >= 0) {
                if (grid[row][offset + i].value != grid[row][offset - i - 1].value) {
                    return false
                }
                i++
            }
            return true
        }

        if (row == 0) {
            // Try all offsets
            for (col in 1 until grid.numCols) {
                if (isMirror(col)) {
                    findVerticalMirror(1, col, usedSmudge, originalMirror)?.let { return it }
                }

                if (!usedSmudge) {
                    // Try with smudge
                    for (smudgeCol in 0 until grid.numCols) {
                        grid[row][smudgeCol].value = !grid[row][smudgeCol].value

                        var found: Mirror? = null
                        if (isMirror(col)) {
                            found = findVerticalMirror(1, col, true, originalMirror)
                        }

                        // Undo
                        grid[row][smudgeCol].value = !grid[row][smudgeCol].value

                        if (found != null) {
                            return found
                        }
                    }
                }
            }
            return null
        }

        if (!usedSmudge) {
            for (smudgeCol in 0 until grid.numCols) {
                grid[row][smudgeCol].value = !grid[row][smudgeCol].value

                var found: Mirror? = null
                if (isMirror(offset)) {
                    found = findVerticalMirror(row + 1, offset, true, originalMirror)
                }

                // Undo
                grid[row][smudgeCol].value = !grid[row][smudgeCol].value

                if (found != null) {
                    return found
                }
            }
        }

        if (!isMirror(offset)) {
            return null
        }

        return findVerticalMirror(row + 1, offset, usedSmudge, originalMirror)
    }

    override fun solveLevel1(): Any {
        return parsePatterns(input.lines)
            .sumOf { it.findMirror(withSmudge = false, originalMirror = null).score }
    }

    override fun solveLevel2(): Any {
        return parsePatterns(input.lines).sumOf { pattern ->
            val originalMirror = pattern.findMirror(withSmudge = false, originalMirror = null)
            pattern.findMirror(withSmudge = true, originalMirror = originalMirror).score
        }
    }
}
