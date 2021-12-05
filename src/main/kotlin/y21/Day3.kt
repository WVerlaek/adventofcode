package y21

import common.Day
import kotlin.math.pow
import kotlin.math.roundToInt

fun main() = Day3(2)

object Day3 : Day(2021, 3) {
    init {
//        useSampleInput {
//            """
//                00100
//                11110
//                10110
//                10111
//                10101
//                01111
//                00111
//                11100
//                10000
//                11001
//                00010
//                01010
//            """.trimIndent()
//        }
    }
    val digits = lines[0].length

    override fun level1(): String {
        val half = if (lines.size % 2 == 0) lines.size / 2 else (lines.size + 1) / 2
        val gammaBin = (0 until digits).map { col ->
            val isOne = lines.count { it[col] == '1' } >= half
            if (isOne) "1" else "0"
        }
        val gamma = gammaBin.joinToString(separator = "").toLong(2)
        // eps = 2^digits - 1 - gamma
        val eps = 2.0.pow(digits.toDouble()).roundToInt() - 1 - gamma
        return "${gamma * eps}"
    }

    fun mcv(values: List<Boolean>): Boolean {
        return values.count { it } >= values.count { !it }
    }

    fun lcv(values: List<Boolean>) = !mcv(values)

    override fun level2(): String {
        fun List<String>.getRating(i: Int, mostCommon: Boolean): String {
            if (size == 1) return this[0]

            val bits = this.map { it[i] == '1' }
            val requires = if (mostCommon) mcv(bits) else lcv(bits)
            val requiresChar = if (requires) '1' else '0'
            val newList = this.filter { it[i] == requiresChar }
            return newList.getRating(i + 1, mostCommon)
        }

        val oxy = lines.getRating(0, true)
        val co2 = lines.getRating(0, false)

        val ol = oxy.toLong(2)
        val cl = co2.toLong(2)
        println("o: ${ol}, c: $cl")
        return "${ol * cl}"
    }
}
