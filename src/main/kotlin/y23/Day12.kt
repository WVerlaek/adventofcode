package y23

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


fun main() = solvePuzzle(year = 2023, day = 12, dryRun = false) { Day12(it) }

class Day12(val input: Input) : Puzzle {
    enum class Condition(val c: Char) { Unknown('?'), Operational('.'), Damaged('#') }

    data class Spring(val conditions: List<Condition>, val groups: List<Int>) {
        val groupsStartMax = List(groups.size) { index ->
            val totalSize = groups.subList(index, groups.size).sum() +
                    // Operational conditions in between groups
                    groups.size - index - 1
            conditions.size - totalSize
        }
    }

    private fun parseSpring(line: String): Spring {
        val (conditions, groups) = line.split(" ")
        return Spring(
            conditions.map { c -> Condition.entries.first { it.c == c } },
            groups.splitToInts(","),
        )
    }

    fun Spring.copies(n: Int): Spring {
        val conditions = this.conditions.toMutableList()
        val groups = this.groups.toMutableList()
        for (i in 1 until n) {
            conditions += Condition.Unknown
            conditions += this.conditions
            groups += this.groups
        }
        return Spring(conditions, groups)
    }

    data class Cached(val i: Int, val groupIdx: Int)

    private fun numArrangements(spring: Spring, cache: MutableMap<Cached, Long>, i: Int = 0, groupIdx: Int = 0): Long {
        if (i >= spring.conditions.size) {
            // Must have created all groups.
            if (groupIdx < spring.groups.size) {
                return 0
            }
            return 1
        }

        if (groupIdx < spring.groups.size && i > spring.groupsStartMax[groupIdx]) {
            return 0
        }

        val key = Cached(i, groupIdx)
        cache[key]?.let { return it }

        fun tryStartGroup(): Long {
            if (groupIdx >= spring.groups.size) {
                return 0
            }
            val group = spring.groups[groupIdx]
            for (j in 1 until group) {
                if (i + j >= spring.conditions.size) {
                    return 0
                }
                if (spring.conditions[i + j] == Condition.Operational) {
                    return 0
                }
            }

            // Next condition after group (if exists) must be operational.
            if (i + group < spring.conditions.size && spring.conditions[i + group] == Condition.Damaged) {
                return 0
            }

            return numArrangements(spring, cache, i + group + 1, groupIdx + 1)
        }

        return when (spring.conditions[i]) {
            Condition.Damaged -> {
                tryStartGroup()
            }

            Condition.Operational -> {
                numArrangements(spring, cache, i + 1, groupIdx)
            }

            Condition.Unknown -> {
                // Two options: start damaged group, or operational.
                // Try starting damaged:
                tryStartGroup() + numArrangements(spring, cache, i + 1, groupIdx)
            }
        }.also { cache[key] = it }
    }

    override fun solveLevel1(): Any {
        return input.lines.sumOf { line ->
            val spring = parseSpring(line)
            numArrangements(spring, mutableMapOf())
        }
    }

    override fun solveLevel2(): Any {
        return input.lines.sumOf { line ->
            val spring = parseSpring(line).copies(5)
            numArrangements(spring, mutableMapOf())
        }
    }
}
