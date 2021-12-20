package common.puzzle

import common.client.Client
import kotlin.system.measureTimeMillis

interface Puzzle {
    fun solveLevel1(): Any
    fun solveLevel2(): Any
}

fun solvePuzzle(year: Int, day: Int, level: Int, dryRun: Boolean = false, puzzle: (input: Input) -> Puzzle) {
    val client = Client()
    val input = client.getInput(year, day).trimEnd() // Remove empty last line.
    val p = puzzle(Input(input))

    val answerAny: Any
    val millis = measureTimeMillis {
        answerAny = when (level) {
            1 -> p.solveLevel1()
            2 -> p.solveLevel2()
            else -> throw IllegalArgumentException("Level should be 1 or 2")
        }
    }

    val answer = answerAny.toString()
    println("Answer for $year/$day level $level: '$answer' (took ${millis}ms)")
    if (!dryRun) {
        client.postAnswer(year, day, level, answer)
    }
}
