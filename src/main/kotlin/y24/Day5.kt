package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.puzzle.splitToInts
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2024, day = 5) { Day5(it) }

class Day5(val input: Input) : Puzzle {

    data class PageRule(
        val before: Int,
        val after: Int,
    )

    data class Update(
        val pageNumbers: List<Int>,
    ) {
        val middle = pageNumbers[pageNumbers.size / 2]

        fun satisfies(rules: List<PageRule>): Boolean {
            val pageIndices = pageNumbers.mapIndexed { index, i -> i to index }.toMap()
            return rules.all { (before, after) ->
                val beforeIndex = pageIndices[before] ?: return@all true
                val afterIndex = pageIndices[after] ?: return@all true
                beforeIndex < afterIndex
            }
        }

        fun sortedBy(rules: List<PageRule>): Update {
            val sorted = pageNumbers.sortedWith(Comparator { p1, p2 ->
                val rule = rules.find { (it.before == p1 && it.after == p2) || (it.before == p2 && it.after == p1) }
                    ?: return@Comparator 0

                if (rule.before == p1) -1 else 1
            })

            return Update(sorted)
        }
    }

    private val emptyLine = input.lines.indexOf("")
    private val rules = input.lines.subList(0, emptyLine).map { line ->
        val (before, after) = line.splitToInts("|")
        PageRule(before, after)
    }
    private val updates = input.lines.subList(emptyLine + 1, input.lines.size).map { line ->
        val pageNumbers = line.splitToInts(",")
        Update(pageNumbers)
    }

    override fun solveLevel1(): Any {
        return updates.filter { it.satisfies(rules) }.sumOf { it.middle }
    }
    override fun solveLevel2(): Any {
        return updates.filter { !it.satisfies(rules) }.map { it.sortedBy(rules) }.sumOf { it.middle }
    }
}
