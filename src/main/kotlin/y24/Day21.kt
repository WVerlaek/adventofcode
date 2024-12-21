package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*


fun main() = solvePuzzle(year = 2024, day = 21) { Day21(it) }

typealias PressMap = Map<Pair<Char, Char>, List<String>>

class Day21(val input: Input) : Puzzle {
    private val numpad = Grid(listOf(
        "789",
        "456",
        "123",
        " 0A",
    )) { _, _, c -> c }
    private val keypad = Grid(listOf(
        " ^A",
        "<v>",
    )) { _, _, c -> c }

    data class CacheKey(
        val from: Char,
        val to: Char,
        val robots: Int,
    )
    private val cache = mutableMapOf<CacheKey, Long>()

    private val numpadPresses: PressMap = createNumpadPresses(numpad)
    private val keypadPresses: PressMap = createNumpadPresses(keypad)

    private fun createNumpadPresses(grid: Grid<Char>): PressMap {
        val result = mutableMapOf<Pair<Char, Char>, List<String>>()
        grid.cells().forEach { c1 ->
            grid.cells().forEach { c2 ->
                result[c1.value to c2.value] = createPressesBetween(grid, c1.toPoint(), c2.toPoint(), mutableSetOf())
            }
        }

        return result
    }

    private fun createPressesBetween(grid: Grid<Char>, from: Point, to: Point, visited: MutableSet<Char>): List<String> {
        if (from == to) {
            return listOf("A")
        }

        return directions.flatMap { dir ->
            val neigh = from + dir.toPoint()
            if (!grid.withinBounds(neigh.row, neigh.col)) {
                return@flatMap emptyList()
            }

            val cell = grid[neigh]
            if (cell.value == ' ' || cell.value in visited) {
                return@flatMap emptyList()
            }

            visited += cell.value
            return@flatMap createPressesBetween(grid, neigh, to, visited)
                .map { sequence -> dir.toChar() + sequence }
                .also {
                    visited -= cell.value
                }
        }
    }

    private fun keyPresses(pressMap: PressMap, key: CacheKey): Long {
        if (key in cache) {
            return cache.getValue(key)
        }

        if (key.robots == 0) {
            return 1L
        }

        return pressMap.getValue(key.from to key.to).minOf { pressSequence ->
            var c = 'A'
            pressSequence.sumOf { toPress ->
                keyPresses(keypadPresses, CacheKey(c, toPress, key.robots - 1)).also {
                    c = toPress
                }
            }
        }.also {
            cache[key] = it
        }
    }

    private fun sequenceMinPresses(seq: String, numRobots: Int): Long {
        var c = 'A'
        return seq.sumOf { toC ->
            keyPresses(numpadPresses, CacheKey(c, toC, numRobots + 1)).also {
                c = toC
            }
        }
    }

    override fun solveLevel1(): Any {
        return input.lines.sumOf { seq ->
            val numeric = seq.filter { it.isDigit() }.toInt()
            numeric * sequenceMinPresses(seq, 2)
        }
    }

    override fun solveLevel2(): Any {
        return input.lines.sumOf { seq ->
            val numeric = seq.filter { it.isDigit() }.toInt()
            numeric * sequenceMinPresses(seq, 25)
        }
    }
}
