package y25

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2025, day = 1) { Day1(it) }

class Day1(val input: Input) : Puzzle {

    private val rotations = input.lines.map { line ->
        when (line[0]) {
            'L' -> -line.substring(1).toInt()
            'R' -> line.substring(1).toInt()
            else -> error("Unexpected character ${line[0]}")
        }
    }

    override fun solveLevel1(): Any {
        var position = 50
        return rotations.count { rotation ->
            position += rotation
            position = (position + 100) % 100
            position == 0
        }
    }

    override fun solveLevel2(): Any {
        var position = 1000000050
        var password = 0
        rotations.forEach { rotation ->
            if (rotation < 0) {
                // 99 -> 0
                // 100 (0) -> 0
                // 101 (1) -> 1
                val start = (position - 1) / 100
                position += rotation
                val end = (position - 1) / 100
                password += start - end
            } else {
                // 99 -> 0
                // 100 (0) -> 1
                // 101 (1) -> 1
                val start = position / 100
                position += rotation
                val end = position  / 100
                password += end - start
            }
        }
        return password
    }
}
