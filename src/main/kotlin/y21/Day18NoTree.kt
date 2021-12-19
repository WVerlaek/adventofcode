package y21

import common.puzzle.Input
import common.puzzle.Puzzle

data class SnailfishNumber(
    val numbers: List<Long>,
    val depth: List<Int>,
) {
    operator fun plus(other: SnailfishNumber): SnailfishNumber {
        val n = numbers + other.numbers
        val d = depth + other.depth
        return SnailfishNumber(n, d.map { it + 1 }).fullyReduce()
    }

    fun magnitude(): Long {
        var reduced = this
        do {
            reduced = reduced.reduceMagnitude()
        } while (reduced.numbers.size > 1)
        return reduced.numbers[0]
    }

    fun reduceMagnitude(): SnailfishNumber {
        for (i in 0 until numbers.size - 1) {
            val j = i + 1
            if (depth[i] == depth[j]) {
                // i,j is pair
                val newNumbers = numbers.toMutableList()
                val newDepth = depth.toMutableList()
                newNumbers[i] = 3L * numbers[i] + 2L * numbers[j]
                newNumbers.removeAt(j)
                newDepth[i] = depth[i] - 1
                newDepth.removeAt(j)
                return SnailfishNumber(newNumbers, newDepth)
            }
        }
        return this
    }

    fun reduce(): SnailfishNumber? {
        for (i in numbers.indices) {
            if (depth[i] == 4) {
                // Explode i,i+1.
                val newNumbers = numbers.toMutableList()
                val newDepth = depth.toMutableList()
                val left = numbers[i]
                val right = numbers[i+1]
                newNumbers[i] = 0
                newDepth[i] = depth[i] - 1
                newNumbers.removeAt(i+1)
                newDepth.removeAt(i+1)
                if (i > 0) {
                    newNumbers[i - 1] += left
                }
                if (i + 2 < numbers.size) {
                    newNumbers[i + 1] += right
                }
                return SnailfishNumber(newNumbers, newDepth)
            }
        }

        // Split.
        for (i in numbers.indices) {
            if (numbers[i] >= 10) {
                val newNumbers = numbers.toMutableList()
                val newDepth = depth.toMutableList()
                val left = numbers[i] / 2
                val right = (numbers[i] + 1) / 2
                newNumbers[i] = left
                newNumbers.add(i + 1, right)
                newDepth[i] = depth[i] + 1
                newDepth.add(i + 1, newDepth[i])
                return SnailfishNumber(newNumbers, newDepth)
            }
        }
        return null
    }

    fun fullyReduce(): SnailfishNumber {
        var result = this
        while (true) {
            result = result.reduce()
                ?: return result
        }
    }

    companion object {
        fun parse(input: String): SnailfishNumber {
            val numbers = mutableListOf<Long>()
            val depth = mutableListOf<Int>()
            parseLeft(input, 0, -1, numbers, depth)
            return SnailfishNumber(numbers, depth)
        }

        private fun parseLeft(input: String, from: Int, curDepth: Int, numbers: MutableList<Long>, depth: MutableList<Int>): Int {
            return when (input[from]) {
                '[' -> {
                    val leftLen = parseLeft(input, from + 1, curDepth + 1, numbers, depth) // Include [
                    val rightLen = parseLeft(input, from + leftLen + 2, curDepth + 1, numbers, depth) // Include [,
                    leftLen + rightLen + 3 // Include [,]
                }
                else -> {
                    var to = from + 1
                    while (to < input.length && input[to].isDigit()) to++
                    val numStr = input.substring(from, to)
                    numbers += numStr.toLong()
                    depth += curDepth
                    numStr.length
                }
            }
        }
    }
}

class Day18NoTree(val input: Input) : Puzzle {
    override fun solveLevel1(): Any {
        val snailfishNumbers = input.lines.map { SnailfishNumber.parse(it) }
        return snailfishNumbers.reduce { acc, number -> acc + number }.magnitude()
    }

    override fun solveLevel2(): Any {
        return sequence {
            for (i in 0 until input.lines.size) {
                for (j in 0 until input.lines.size) {
                    if (i == j) continue

                    val numI = SnailfishNumber.parse(input.lines[i])
                    val numJ = SnailfishNumber.parse(input.lines[j])
                    yield((numI + numJ).magnitude())
                }
            }
        }.maxOf { it }
    }
}
