package common

import common.input.loadInput
import common.input.submitAnswer

abstract class Day(val year: Int, val day: Int) {
    var input = loadInput(year, day).trimEnd() // Remove empty last line.

    val lines: List<String>
        get() = input.lines()

    private var dryRun = false

    fun useSampleInput(s: () -> String) {
        input = s()
        dryRun = true
    }

    abstract fun level1(): String
    abstract fun level2(): String

    fun solveLevel(level: Int) {
        val answer = when (level) {
            1 -> level1()
            2 -> level2()
            else -> throw IllegalArgumentException("Level should be 1 or 2")
        }
        submitAnswer(year, day, level, dryRun = dryRun) {
            answer
        }
    }

    operator fun invoke(level: Int) = solveLevel(level)
}
