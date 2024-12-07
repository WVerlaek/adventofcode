package y24

import common.datastructures.*
import common.ext.*
import common.puzzle.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess

fun main() = solvePuzzle(year = 2024, day = 7, dryRun = false) { Day7(it) }

class Day7(val input: Input) : Puzzle {

    enum class Operator(val s: String, val op: (a: Long, b: Long) -> Long) {
        Add("+", { a, b -> a + b }),
        Multiply("*", { a, b -> a * b }),
        Concatenate("||", {a, b -> "$a$b".toLong() }),
    }
    data class Equation(val testValue: Long, val numbers: List<Long>)
    data class Solution(val operators: List<Operator>)

    fun findSolutions(eq: Equation, availableOperators: List<Operator>, i: Int = 1, curTestValue: Long = eq.numbers[0], operators: MutableList<Operator> = mutableListOf()): List<Solution> {
        if (curTestValue > eq.testValue) {
            // No negative numbers, cannot reach testValue anymore.
            return emptyList()
        }

        if (i == eq.numbers.size) {
            if (curTestValue == eq.testValue) {
                return listOf(Solution(operators.toList()))
            }

            return emptyList()
        }

        val num = eq.numbers[i]
        return availableOperators.flatMap { op ->
            val newTestValue = op.op(curTestValue, num)

            operators.add(op)
            val solutions = findSolutions(eq, availableOperators, i + 1, newTestValue, operators)
            operators.removeLast()
            solutions
        }
    }

    private fun parseInput(lines: List<String>): List<Equation> {
        return lines.map { line ->
            val (testValue, numbers) = line.split(": ")
            Equation(testValue.toLong(), numbers.splitToLongs(" "))
        }
    }

    override fun solveLevel1(): Any {
        val equations = parseInput(input.lines)

        return equations.filter { eq ->
            findSolutions(eq, listOf(Operator.Add, Operator.Multiply)).isNotEmpty()
        }.sumOf { it.testValue }
    }

    override fun solveLevel2(): Any {
        val equations = parseInput(input.lines)

        return equations.filter { eq ->
            findSolutions(eq, listOf(Operator.Add, Operator.Multiply, Operator.Concatenate)).isNotEmpty()
        }.sumOf { it.testValue }
    }
}
