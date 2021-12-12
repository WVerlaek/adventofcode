package common.puzzle

import common.client.Client

interface Puzzle {
    fun solveLevel1(): Any
    fun solveLevel2(): Any
}

fun solvePuzzle(year: Int, day: Int, level: Int, dryRun: Boolean = false, puzzle: (input: Input) -> Puzzle) {
    val client = Client()
    val input = client.getInput(year, day).trimEnd() // Remove empty last line.
    val p = puzzle(Input(input))
    val answer = when (level) {
        1 -> p.solveLevel1()
        2 -> p.solveLevel2()
        else -> throw IllegalArgumentException("Level should be 1 or 2")
    }.toString()

    println("Answer for $year/$day level $level: '$answer'")
    if (!dryRun) {
        client.postAnswer(year, day, level, answer)
    }
}
