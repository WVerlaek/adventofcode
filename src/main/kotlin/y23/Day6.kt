package y23

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2023, day = 6) { Day6(it) }

class Day6(val input: Input) : Puzzle {

    data class Race(
        val time: Long,
        val dist: Long,
    )

    private fun parseInput(lines: List<String>): List<Race> {
        val times = lines[0]
            .substringAfter(":")
            .split(" ")
            .filter { it.isNotEmpty() }
            .map { it.toLong() }
        val dists = lines[1]
            .substringAfter(":")
            .split(" ")
            .filter { it.isNotEmpty() }
            .map { it.toLong() }
        return times.mapIndexed { i, t ->
            Race(t, dists[i])
        }
    }

    private fun Race.waysToWin(): Long {
        // hold * (time - hold) == dist
        // hold * (time - hold) - dist == 0
        // hold^2 - time*hold + dist == 0
        // hold = (time - sqrt(D))/2 || hold = (time + sqrt(D))/2   D = time^2 - 4*dist
        val d = time * time - 4 * dist
        val sqrtD = sqrt(d.toDouble())

        var lower = ceil((time - sqrtD) / 2.0).roundToLong()
        var upper = floor((time + sqrtD) / 2.0).roundToLong()

        // Edge-case: exactly same dist as current best, must increase by 1 then.
        if (lower * (time - lower) == dist) {
            lower++
        }
        if (upper * (time - upper) == dist) {
            upper--
        }

        return upper - lower + 1
    }

    override fun solveLevel1(): Any {
        return parseInput(input.lines)
            .productOf { it.waysToWin() }
    }

    override fun solveLevel2(): Any {
        val removeKerning = input.lines.map { it.replace(" ", "") }
        val race = parseInput(removeKerning).requireSingleElement()
        return race.waysToWin()
    }
}
