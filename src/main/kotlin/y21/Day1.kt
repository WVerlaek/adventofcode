package y21

import common.Day
import common.util.toInts

fun main() = Day1(2)

object Day1 : Day(2021, 1) {
    init {
        useSampleInput {
            """
                199
                200
                208
                210
                200
                207
                240
                269
                260
                263
            """.trimIndent()
        }
    }
    private val numbers = lines.toInts()

    override fun level1(): String {
        return numbers.numIncreases().toString()
    }

    override fun level2(): String {
        val numWindows = numbers.size - 2
        val windows = (0 until numWindows).map { i -> numbers.subList(i, i+3).sum() }
        return windows.numIncreases().toString()
    }
}

private fun List<Int>.numIncreases(): Int {
    data class Acc(val prevVal: Int, val numIncreases: Int)
    return this.fold(Acc(Int.MAX_VALUE, 0)) { acc, v ->
        if (v > acc.prevVal) {
            acc.copy(prevVal = v, numIncreases = acc.numIncreases + 1)
        } else {
            acc.copy(prevVal = v)
        }
    }.numIncreases
}
