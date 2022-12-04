package y22

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import common.ext.intersectAll

fun main() = solvePuzzle(year = 2022, day = 4, level = 2) { Day4(it) }

operator fun IntRange.contains(o: IntRange): Boolean {
    return start <= o.start && endInclusive >= o.endInclusive
}

fun IntRange.overlaps(o: IntRange): Boolean {
    return when {
        endInclusive < o.start -> false
        start > o.endInclusive -> false
        else -> true
    }
}

class Day4(val input: Input) : Puzzle {
    fun parseRange(s: String): IntRange {
        val (from, to) = s.split("-")
        return from.toInt()..to.toInt()
    }

    fun parsePairs(lines: List<String>): List<Pair<IntRange, IntRange>> {
        return lines.map { line ->
            val (p1, p2) = line.split(",")
            parseRange(p1) to parseRange(p2)
        }
    }

    override fun solveLevel1(): Any {
        val pairs = parsePairs(input.lines)
        return pairs.count { (r1, r2) ->
            r1 in r2 || r2 in r1
         }
    }

    override fun solveLevel2(): Any {
        return parsePairs(input.lines)
            .count { (r1, r2) ->
                r1.overlaps(r2)
            }
    }
}
