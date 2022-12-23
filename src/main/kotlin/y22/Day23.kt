package y22

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2022, day = 23) { Day23(it) }

class Day23(val input: Input) : Puzzle {

    // N -> S -> W -> E
    val dirs = listOf(1, 3, 0, 2).map { directions[it] }

    data class Move(val from: Point, val to: Point)

    fun parse(lines: List<String>, padding: Int = 12): Grid<Boolean> {
        return lines.toGrid().expand(padding, false)
    }

    fun move(grid: Grid<Boolean>, fromDir: Int, curPositions: Set<Point>? = null): Pair<Set<Point>, Boolean> {
        val positions = curPositions
            ?: grid.cells()
                .filter { it.value }
                .map { it.toPoint() }
                .toSet()

        val allMoves = positions.map { p ->
            val cell = grid[p]
            if (grid.neighbors(cell, true).none { it.value }) {
                // No neighbours, don't move.
                return@map Move(p, p)
            }

            fun hasNeigh(dir: Dir): Boolean {
                for (j in -1..1) {
                    val neigh = p + Point(
                        dir.dCol + j*dir.dRow.sign,
                        dir.dRow + j*dir.dCol.sign,
                    )
                    if (grid[neigh].value) {
                        // Can't move in this dir.
                        return true
                    }
                }
                return false
            }

            for (i in fromDir..fromDir+3) {
                val dir = dirs[i % 4]
                if (hasNeigh(dir)) {
                    continue
                }

                // No neighbour, propose this direction.
                return@map Move(p, p + dir.toPoint())
            }

            // No valid move found, stay in position.
            Move(p, p)
        }

        var moved = false
        val targets = allMoves.groupBy { move -> move.to }
        val newPositions = allMoves.map { move ->
            if (targets[move.to]!!.size > 1) {
                // Collision, don't move to target.
                move.from
            } else {
                // Update map.
                grid[move.from].value = false
                grid[move.to].value = true
                if (move.from != move.to) {
                    moved = true
                }
                move.to
            }
        }

        return newPositions.toSet() to moved
    }

    override fun solveLevel1(): Any {
        val grid = parse(input.lines)
        var curPositions: Set<Point>? = null
        var curDir = 0
        repeat(10) {
            curPositions = move(grid, curDir++, curPositions).first
        }

        return grid.trim().cells().count { !it.value }
    }

    override fun solveLevel2(): Any {
        val grid = parse(input.lines, 500)
        var curPositions: Set<Point>? = null
        var curDir = 0
        while (true) {
            val (newPositions, moved) = move(grid, curDir++, curPositions)
            curPositions = newPositions
            if (!moved) {
                return curDir
            }
        }
    }
}
