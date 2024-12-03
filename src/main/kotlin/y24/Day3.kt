package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle

fun main() = solvePuzzle(year = 2024, day = 3) { Day3(it) }

class Day3(val input: Input) : Puzzle {
    private val mulRegex = Regex("""mul\((\d+),(\d+)\)""")

    override fun solveLevel1(): Any {
        return mulRegex.findAll(input.string).sumOf {
            it.groupValues[1].toLong() * it.groupValues[2].toLong()
        }
    }

    override fun solveLevel2(): Any {
        val memory = disableMul(input.string)
        return mulRegex.findAll(memory).sumOf {
            it.groupValues[1].toLong() * it.groupValues[2].toLong()
        }
    }

    fun disableMul(input: String): String {
        val result = input.toCharArray()

        var i = 0
        while (i < input.length) {
            val nextDont = input.indexOf("don't()", i)
            if (nextDont == -1) {
                return result.joinToString("")
            }

            val nextDo = input.indexOf("do()", nextDont)
            if (nextDo == -1) {
                // Clear until end of string.
                result.fill(' ', nextDont, input.length)
                return result.joinToString("")
            }

            result.fill(' ', nextDont, nextDo)
            i = nextDo
        }

        return result.joinToString("")
    }
}
