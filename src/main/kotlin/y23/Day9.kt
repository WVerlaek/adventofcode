package y23

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.puzzle.splitToInts
import common.util.*
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2023, day = 9) { Day9(it) }

class Day9(val input: Input) : Puzzle {

    data class Sequence(val list: ArrayDeque<Int>) {
        val isZero = list.all { it == 0 }

        val diff: Sequence? = if (isZero) null else {
            List(list.size - 1) { index ->
                list[index + 1] - list[index]
            }.let { Sequence(ArrayDeque(it)) }
        }

        fun generateNext(): Int {
            val next = when (diff) {
                null -> 0
                else -> list.last() + diff.generateNext()
            }

            list.addLast(next)
            return next
        }

        fun generatePrevious(): Int {
            val prev = when (diff) {
                null -> 0
                else -> list.first() - diff.generatePrevious()
            }

            list.addFirst(prev)
            return prev
        }
    }

    private fun parseHistory(line: String): Sequence {
        return Sequence(ArrayDeque(line.splitToInts(" ")))
    }

    override fun solveLevel1(): Any {
        val histories = input.lines.map(::parseHistory)
        return histories.sumOf { it.generateNext() }
    }

    override fun solveLevel2(): Any {
        val histories = input.lines.map(::parseHistory)
        return histories.sumOf { it.generatePrevious() }
    }
}
