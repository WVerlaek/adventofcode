package y24

import common.datastructures.*
import common.ext.*
import common.puzzle.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess

fun main() = solvePuzzle(year = 2024, day = 7, dryRun = true) { Day7(it) }

class Day7(val input: Input) : Puzzle {

    enum class Operator(val op: (a: Long, b: Long) -> Long) {
        Add({ a, b -> a + b }),
        Multiply({ a, b -> a * b }),
        Concatenate({a, b -> "$a$b".toLong() }),
    }
    data class Equation(val testValue: Long, val numbers: List<Long>)

    fun hasSolutions(eq: Equation, availableOperators: List<Operator>, i: Int = 1, curTestValue: Long = eq.numbers[0]): Boolean {
        if (curTestValue > eq.testValue) {
            // No negative numbers, cannot reach testValue anymore.
            return false
        }

        if (i == eq.numbers.size) {
            return curTestValue == eq.testValue
        }

        val num = eq.numbers[i]
        return availableOperators.any { op ->
            val newTestValue = op.op(curTestValue, num)
            hasSolutions(eq, availableOperators, i + 1, newTestValue)
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
            hasSolutions(eq, listOf(Operator.Add, Operator.Multiply))
        }.sumOf { it.testValue }
    }

    override fun solveLevel2(): Any {
        val equations = parseInput(input.lines)

        return equations.filter { eq ->
            hasSolutions(eq, listOf(Operator.Add, Operator.Multiply, Operator.Concatenate))
        }.sumOf { it.testValue }
    }
}
