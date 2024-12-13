package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2024, day = 13) { Day13(it) }

class Day13(val input: Input) : Puzzle {
    private val costA = 3
    private val costB = 1

    data class Machine(
        val a: LongPoint,
        val b: LongPoint,
        val prize: LongPoint,
    )

    private fun parseButton(line: String): LongPoint {
        val x = line.substringBetween("X+", ",").toLong()
        val y = line.substringAfter("Y+").toLong()
        return LongPoint(x, y)
    }

    private fun parsePrize(line: String): LongPoint {
        val x = line.substringBetween("X=", ",").toLong()
        val y = line.substringAfter("Y=").toLong()
        return LongPoint(x, y)
    }

    private fun parseInput(lines: List<String>): List<Machine> {
        val n = (lines.size + 1) / 4
        return (0..<n).map { i ->
            val a = parseButton(lines[i * 4])
            val b = parseButton(lines[i * 4 + 1])
            val prize = parsePrize(lines[i * 4 + 2])
            Machine(a, b, prize)
        }
    }

    private fun minCost(machine: Machine): Long? {
        // Using Cramer's method
        val d = (machine.a.x * machine.b.y) - (machine.b.x * machine.a.y)
        if (d == 0L) {
            return null
        }

        val dx = (machine.prize.x * machine.b.y) - (machine.b.x * machine.prize.y)
        val dy = (machine.a.x * machine.prize.y) - (machine.prize.x * machine.a.y)

        if (dx % d != 0L) {
            return null
        }
        val m = dx / d

        if (dy %d != 0L) {
            return null
        }
        val n = dy / d

        return m * costA + n * costB
    }

    override fun solveLevel1(): Any {
        val machines = parseInput(input.lines)
        return machines.mapNotNull { minCost(it) }.sum()
    }

    override fun solveLevel2(): Any {
        val machines = parseInput(input.lines).map { machine ->
            machine.copy(
                prize = machine.prize.copy(
                    col = machine.prize.col + 10000000000000L,
                    row = machine.prize.row + 10000000000000L,
                )
            )
        }
        return machines.mapNotNull { minCost(it) }.sum()
    }
}
