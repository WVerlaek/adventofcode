package y22

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import common.ext.intersectAll

fun main() = solvePuzzle(year = 2022, day = 3, level = 2) { Day3(it) }

data class Sack(val left: List<Int>, val right: List<Int>)
fun Char.toPriority() = if (this in 'a'..'z') this - 'a' + 1 else this - 'A' + 27

class Day3(val input: Input) : Puzzle {
    fun parse(lines: List<String>): List<Sack> {
        return lines.map { line ->
            val half = line.length / 2
            val left = line.substring(0, half)
            val right = line.substring(half)
            Sack(left.map(Char::toPriority), right.map(Char::toPriority))
        }
    }

    override fun solveLevel1(): Any {
        return parse(input.lines).sumOf { sack ->
            val intersection = sack.left.toSet().intersect(sack.right.toSet())
            require(intersection.size == 1)
            intersection.first()
        }
    }

    override fun solveLevel2(): Any {
        val sacks = input.lines.map { line -> line.map(Char::toPriority) }
        val groups = sacks.withIndex()
            .groupBy { it.index / 3 }
            .values
            // Remove index from sacks.
            .map { group -> group.map { sackWithIdx -> sackWithIdx.value } }

        return groups.sumOf { group ->
            val intersection = group.map { sack -> sack.toSet() }.intersectAll()
            intersection.first()
        }
    }
}
