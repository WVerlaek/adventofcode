package y22

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import common.ext.intersectAll
import common.ext.removeLast
import common.datastructures.Grid
import common.datastructures.directions

fun main() = solvePuzzle(year = 2022, day = 8, dryRun=false) { Day8(it) }

data class Tree(val height: Int, var visibleFrom: Int)

class Day8(val input: Input) : Puzzle {
    fun parseGrid(lines: List<String>): Grid<Tree> {
        return Grid(lines.size, lines[0].length) { r, c ->
            Tree(lines[r][c].digitToInt(), Int.MAX_VALUE)
        }
    }

    override fun solveLevel1(): Any {
        val grid = parseGrid(input.lines)

        // N->S and S->N
        for (c in 0 until grid.numCols) {
            val ranges = (0 until grid.numRows).let { it ->
                listOf(it, it.reversed())
            }
            ranges.forEach { range ->
                var visibleFrom = 0
                for (r in range) {
                    visibleFrom = when {
                        // Boundary is always visible.
                        r == range.first -> 0
                        else -> {
                            val prevTree = grid[r - range.step][c].value
                            maxOf(visibleFrom, prevTree.height + 1)
                        }
                    }
                    val curTree = grid[r][c].value
                    curTree.visibleFrom = minOf(curTree.visibleFrom, visibleFrom)
                }
            }
        }

        // E->W and W->E
        for (r in 0 until grid.numRows) {
            val ranges = (0 until grid.numCols).let { it ->
                listOf(it, it.reversed())
            }
            ranges.forEach { range ->
                var visibleFrom = 0
                for (c in range) {
                    visibleFrom = when {
                        // Boundary is always visible.
                        c == range.first -> 0
                        else -> {
                            val prevTree = grid[r][c - range.step].value
                            maxOf(visibleFrom, prevTree.height + 1)
                        }
                    }
                    val curTree = grid[r][c].value
                    curTree.visibleFrom = minOf(curTree.visibleFrom, visibleFrom)
                }
            }
        }

        return grid.cells().count { cell ->
            cell.value.let { tree ->
                 tree.height >= tree.visibleFrom
            }
        }
    }

    override fun solveLevel2(): Any {
        val grid = parseGrid(input.lines)
        return grid.cells().maxOf { (r, c, tree) ->
            var score = 1
            directions().forEach { (dr, dc) ->
                var visibleTrees = 0
                var curR = r
                var curC = c
                while (grid.withinBounds(curR + dr, curC + dc)) {
                    curR += dr
                    curC += dc
                    visibleTrees++
                    if (grid[curR][curC].value.height >= tree.height) {
                        break
                    }
                }
                score *= visibleTrees
            }
            score
        }
    }
}
