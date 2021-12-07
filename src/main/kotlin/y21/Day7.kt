package y21

import common.Day
import kotlin.math.abs

fun main() = Day7(2)

object Day7 : Day(2021, 7) {
    init {
//        useSampleInput { "16,1,2,0,4,2,7,1,2,14" }
    }
    val crabs = lines[0].split(",").map { it.toInt() }

    override fun level1(): String {
        val max = crabs.maxOrNull() ?: 0
        val costs = IntArray(max + 1) { i ->
            crabs.sumOf { crab -> abs(crab - i) }
        }
        return costs.minOrNull()?.toString() ?: ""
    }

    override fun level2(): String {
        val max = crabs.maxOrNull() ?: 0

        val costToMoveNSteps = IntArray(max + 1)
        for (i in 1 until costToMoveNSteps.size) {
            costToMoveNSteps[i] = costToMoveNSteps[i - 1] + i
        }

        val costs = IntArray(max + 1) { i ->
            crabs.sumOf { crab -> costToMoveNSteps[abs(crab - i)] }
        }
        return costs.minOrNull()?.toString() ?: ""
    }
}
