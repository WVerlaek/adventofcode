package y23

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import okhttp3.internal.trimSubstring
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2023, day = 4) { Day4(it) }

class Day4(val input: Input) : Puzzle {

    data class Card(
        val winningNumbers: Set<Int>,
        val numbers: List<Int>,
    ) {
        fun matchingNumbers() = numbers.count { it in winningNumbers }

        fun score(): Int {
            val n = matchingNumbers()
            if (n == 0) return 0

            return 1 shl (n - 1)
        }
    }

    private fun parseCard(line: String): Card {
        val winning = line.substringAfter(": ").substringBefore(" |")
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.toInt() }

        val numbers = line.substringAfter("| ")
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.toInt() }

        return Card(winning.toSet(), numbers)
    }

    override fun solveLevel1(): Any {
        return input.lines
            .map { parseCard(it) }
            .sumOf { it.score() }
    }

    override fun solveLevel2(): Any {
        val cards = input.lines
            .map { parseCard(it) }

        val cardCounts = IntArray(cards.size) { 1 }
        for (i in cards.indices) {
            val newCards = cards[i].matchingNumbers()

            for (j in 0 until newCards) {
                cardCounts[i + j + 1] += cardCounts[i]
            }
        }

        return cardCounts.sum()
    }
}
