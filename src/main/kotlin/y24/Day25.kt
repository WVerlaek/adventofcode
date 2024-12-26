package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2024, day = 25) { Day25(it) }

class Day25(val input: Input) : Puzzle {

    private fun parseInput(lines: List<String>): Pair<List<List<Int>>, List<List<Int>>> {
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()

        var i = 0
        while (i < lines.size) {
            if (lines[i].all { it == '#' }) {
                locks += sequence(lines.subList(i, i + 7))
            } else {
                keys += sequence(lines.subList(i, i + 7).reversed())
            }
            i += 8
        }

        return locks to keys
    }

    private fun sequence(lines: List<String>): List<Int> {
        return IntArray(lines[0].length) { i ->
            lines.indexOfFirst { it[i] == '.' } - 1
        }.toList()
    }

    private fun fits(lock: List<Int>, key: List<Int>): Boolean {
        return lock.indices.all { i -> lock[i] + key[i] < 6 }
    }

    override fun solveLevel1(): Any {
        val (locks, keys) = parseInput(input.lines)
        return locks.sumOf { lock ->
            keys.count { key ->
                fits(lock, key)
            }
        }
    }

    override fun solveLevel2(): Any {
        TODO()
    }
}
