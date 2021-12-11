package common

import common.input.loadInput
import common.input.submitAnswer

abstract class Day(val year: Int, val day: Int) {
    private val realInput by lazy { loadInput(year, day).trimEnd() } // Remove empty last line.
    private var sampleInput: String? = null

    val input: String
        get() = sampleInput ?: realInput

    val lines: List<String>
        get() = input.lines()

    var dryRun = false

    fun useSampleInput(s: () -> String) {
        sampleInput = s()
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
