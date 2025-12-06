package y25

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2025, day = 6) { Day6(it) }

class Day6(val input: Input) : Puzzle {

    private val lineElements = input.lines.map { line -> line.elements() }

    fun String.elements(): List<String> {
        return split(" ").filter { it.isNotBlank() }
    }

    private val startCols = input.lines.last().let { operators ->
        val indices = mutableListOf<Int>()
        operators.forEachIndexed { i, c ->
            if (c != ' ') {
                indices += i
            }
        }
        indices
    }

    override fun solveLevel1(): Any {
        val n = lineElements[0].size
        require(lineElements.all { it.size == n })

        return (0..<n).sumOf { i ->
            val numbers = lineElements.dropLast(1).map { list -> list[i].toLong() }
            when (val operator = lineElements.last()[i]) {
                "+" -> numbers.sum()
                "*" -> numbers.product()
                else -> error("Unknown operator $operator")
            }
        }
    }

    private fun solveProblem(i: Int): Long {
        val untilExcl = if (i == startCols.size - 1) input.lines[0].length else startCols[i + 1] - 1
        val from = startCols[i]

        val numbers = (from..<untilExcl).map { col ->
            val digits = input.lines.dropLast(1).map { line -> line[col] }.filter { it != ' ' }
            digits.joinToString(separator = "").toLong()
        }

        return when (val operator = input.lines.last()[from]) {
            '+' -> numbers.sum()
            '*' -> numbers.product()
            else -> error("Unknown operator $operator")
        }
    }

    override fun solveLevel2(): Any {
        return startCols.indices.sumOf { i -> solveProblem(i) }
    }
}
