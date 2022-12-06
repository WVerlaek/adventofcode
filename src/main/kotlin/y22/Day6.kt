package y22

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import common.ext.intersectAll
import common.ext.removeLast

fun main() = solvePuzzle(year = 2022, day = 6, level = 2) { Day6(it) }

class Day6(val input: Input) : Puzzle {
    fun findMarker(ds: String, unique: Int): Int {
        val count = HashMap<Char, Int>()
        ds.forEachIndexed { i, c ->
            if (i - unique >= 0) {
                val toRemove = ds[i - unique]
                val newValue = count.getValue(toRemove) - 1
                when {
                    newValue <= 0 -> count.remove(toRemove)
                    else -> count[toRemove] = newValue
                }
            }

            count[c] = count.getOrDefault(c, 0) + 1

            if (count.size >= unique) {
                return i + 1
            }
        }

        return -1
    }

    override fun solveLevel1(): Any {
        return input.lines.map { line ->
            findMarker(line, 4)
        }.joinToString(",")
    }

    override fun solveLevel2(): Any {
        return input.lines.map { line ->
            findMarker(line, 14)
        }.joinToString(",")
    }
}
