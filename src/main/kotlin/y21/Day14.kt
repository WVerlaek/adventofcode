package y21

import common.ext.mergeAll
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 14, 2) { Day14(it) }

typealias InsertionRules = Map<String, Char>

class Day14(val input: Input) : Puzzle {
    private val template = input.lines[0]
    private val rules: InsertionRules = input.lines.subList(2, input.lines.size)
            .associate { line -> line.substring(0, 2) to line[6] }

    private fun Map<Char, Long>.score(): Long {
        val sortedCounts = toList().sortedBy { (_, count) -> count }
        return sortedCounts.last().second - sortedCounts.first().second
    }

    override fun solveLevel1(): Any {
        val n = 10
        var polymer = template
        for (i in 0 until n) {
            polymer = polymer.applyInsertions(rules)
        }

        val counts = HashMap<Char, Long>()
        for (c in polymer) {
            counts[c] = (counts[c] ?: 0L) + 1L
        }

        return counts.score()
    }

    private fun String.applyInsertions(rules: InsertionRules): String {
        val newPolymer = mutableListOf<Char>()
        for (i in 0 until length - 1) {
            newPolymer += this[i]
            val pair = this.substring(i, i + 2)
            rules[pair]?.let { newPolymer += it }
        }
        newPolymer += last()
        return newPolymer.joinToString("")
    }

    private val cache = Array(26 * 26) {
        Array(100) { emptyMap<Char, Long>() }
    }

    private val String.key: Int
        get() = 26 * (this[0] - 'A') + (this[1] - 'A')

    // Excludes the pair's second char in the returned count.
    private fun countElements(pair: String, rules: InsertionRules, steps: Int): Map<Char, Long> {
        if (steps <= 0) {
            return mapOf(pair[0] to 1L)
        }

        if (cache[pair.key][steps].isNotEmpty()) {
            return cache[pair.key][steps]
        }

        val rule = rules[pair]
        if (rule == null) {
            // Nothing to insert.
            val result = mapOf(pair[0] to 1L)
            cache[pair.key][steps] = result
            return result
        }

        val pairA = "${pair[0]}$rule"
        val pairB = "$rule${pair[1]}"

        // Merge/sum subtree counts.
        val result = countElements(pairA, rules, steps - 1).toMutableMap()
        result.mergeAll(countElements(pairB, rules, steps - 1))
        cache[pair.key][steps] = result
        return result
    }

    override fun solveLevel2(): Any {
        val result = HashMap<Char, Long>()
        for (i in 0 until template.length - 1) {
            val pair = template.substring(i, i + 2)
            result.mergeAll(countElements(pair, rules, 40))
        }
        result[template.last()] = (result[template.last()] ?: 0L) + 1L
        return result.score()
    }
}
