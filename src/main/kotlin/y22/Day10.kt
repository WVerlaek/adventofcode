package y22

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import common.ext.intersectAll
import common.ext.removeLast
import common.datastructures.Dir
import common.datastructures.Grid
import common.datastructures.directions
import common.datastructures.Point
import common.datastructures.toPoint
import common.datastructures.toGrid
import kotlin.math.sign

fun main() = solvePuzzle(year = 2022, day = 10) { Day10(it) }

sealed class Instruction(val cycles: Int) {
    object Noop : Instruction(1)
    class Add(val x: Int) : Instruction(2)
}

class CPU(var x: Int = 1) {
    private var cycle = 1
    private var nextSignal = 20
    private val signalMax = 220
    private val signals = mutableListOf<Int>()

    fun execute(i: Instruction) {
        var newX = when (i) {
            is Instruction.Noop -> x
            is Instruction.Add -> x + i.x
        }

        if (cycle + i.cycles > nextSignal && nextSignal <= signalMax) {
            signals += nextSignal * x
            nextSignal += 40
        }

        cycle += i.cycles
        x = newX
    }

    fun signalStrengths(): List<Int> {
        while (nextSignal <= signalMax) {
            signals += nextSignal * x
            nextSignal += 40
        }
        return signals
    }
}

class Day10(val input: Input) : Puzzle {

    fun parseInstructions(lines: List<String>): List<Instruction> {
        return lines.map { line ->
            when (line) {
                "noop" -> Instruction.Noop
                else -> Instruction.Add(line.substringAfter("addx ").toInt())
            }
        }
    }

    override fun solveLevel1(): Any {
        val cpu = CPU()
        parseInstructions(input.lines).forEach { instruction ->
            cpu.execute(instruction)
        }

        return cpu.signalStrengths().sum()

    }

    override fun solveLevel2(): Any {
        TODO()
    }
}
