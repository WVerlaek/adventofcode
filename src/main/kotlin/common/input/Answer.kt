package common.input

import common.client.Client

fun submitAnswer(year: Int, day: Int, level: Int, dryRun: Boolean = true, answer: () -> Any) {
    val a = answer().toString()
    println("Submitting answer: $a (dryRun=$dryRun)")
    if (dryRun) return

    Client().postAnswer(year, day, level, a)
}
