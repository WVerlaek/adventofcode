package common.puzzle

import common.client.Client
import kotlin.system.measureTimeMillis

interface Puzzle {
    fun solveLevel1(): Any
    fun solveLevel2(): Any
}

fun solvePuzzle(year: Int, day: Int, level: Int? = null, dryRun: Boolean = false, puzzle: (input: Input) -> Puzzle) {
    val client = Client()
    val input = client.getInput(year, day).trimEnd() // Remove empty last line.
    val p = puzzle(Input(input))

    val levels = when (level) {
        null -> listOf(1, 2)
        else -> listOf(level)
    }

    levels.forEach { lvl ->
        val answerAny: Any
        val millis = measureTimeMillis {
            try {
                answerAny = when (lvl) {
                    1 -> p.solveLevel1()
                    2 -> p.solveLevel2()
                    else -> throw IllegalArgumentException("Level should be 1 or 2")
                }
            } catch (e: NotImplementedError) {
                println("Skipping level $lvl, not implemented")
                return@forEach
            }
        }

        val answer = answerAny.toString()
        println("Answer for $year/$day level $lvl: '$answer' (took ${millis}ms)")
        if (!dryRun) {
            client.postAnswer(year, day, lvl, answer)
        }
    }
}
