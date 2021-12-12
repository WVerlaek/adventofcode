package y21

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 2, 2) { Day2(it) }

class Day2(val input: Input) : Puzzle {
    override fun solveLevel1(): Any {
        var horizontal = 0L
        var depth = 0L
        input.lines.forEach { command ->
            val amount = command.substringAfter(" ").toInt()
            when {
                command.startsWith("forward") -> {
                    horizontal += amount
                }
                command.startsWith("down") -> {
                    depth += amount
                }
                command.startsWith("up") -> {
                    depth -= amount
                }
            }
        }
        return depth * horizontal
    }

    override fun solveLevel2(): Any {
        var aim = 0L
        var horizontal = 0L
        var depth = 0L
        input.lines.forEach { command ->
            val amount = command.substringAfter(" ").toInt()
            when {
                command.startsWith("forward") -> {
                    horizontal += amount
                    depth += aim * amount
                }
                command.startsWith("down") -> {
                    aim += amount
                }
                command.startsWith("up") -> {
                    aim -= amount
                }
            }
        }
        return depth * horizontal
    }
}
