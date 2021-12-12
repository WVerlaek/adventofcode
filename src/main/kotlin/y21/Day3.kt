package y21

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import kotlin.math.pow
import kotlin.math.roundToInt

fun main() = solvePuzzle(2021, 3, 2) { Day3(it) }

class Day3(val input: Input) : Puzzle {
    val digits = input.lines[0].length

    override fun solveLevel1(): Any {
        val half = if (input.lines.size % 2 == 0) input.lines.size / 2 else (input.lines.size + 1) / 2
        val gammaBin = (0 until digits).map { col ->
            val isOne = input.lines.count { it[col] == '1' } >= half
            if (isOne) "1" else "0"
        }
        val gamma = gammaBin.joinToString(separator = "").toLong(2)
        // eps = 2^digits - 1 - gamma
        val eps = 2.0.pow(digits.toDouble()).roundToInt() - 1 - gamma
        return gamma * eps
    }

    fun mcv(values: List<Boolean>): Boolean {
        return values.count { it } >= values.count { !it }
    }

    fun lcv(values: List<Boolean>) = !mcv(values)

    override fun solveLevel2(): Any {
        fun List<String>.getRating(i: Int, mostCommon: Boolean): String {
            if (size == 1) return this[0]

            val bits = this.map { it[i] == '1' }
            val requires = if (mostCommon) mcv(bits) else lcv(bits)
            val requiresChar = if (requires) '1' else '0'
            val newList = this.filter { it[i] == requiresChar }
            return newList.getRating(i + 1, mostCommon)
        }

        val oxy = input.lines.getRating(0, true)
        val co2 = input.lines.getRating(0, false)

        val ol = oxy.toLong(2)
        val cl = co2.toLong(2)
        println("o: ${ol}, c: $cl")
        return ol * cl
    }
}
