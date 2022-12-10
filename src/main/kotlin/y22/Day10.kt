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

fun main() = solvePuzzle(year = 2022, day = 10, dryRun = true) { Day10(it) }

sealed class Instruction(val cycles: Int) {
    object Noop : Instruction(1)
    class Add(val x: Int) : Instruction(2)
}

class CPU(var x: Int = 1, val instructions: List<Instruction>) {
    private var curInstr = 0
    private var cyclesLeft = instructions[0].cycles

    fun cycle() {
        if (cyclesLeft <= 0) {
            instructions.getOrNull(curInstr)?.let { instr ->
                if (instr is Instruction.Add) {
                    x += instr.x
                }
            }
            curInstr++
            cyclesLeft = instructions.getOrNull(curInstr)?.cycles ?: 1
        }

        cyclesLeft--
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
        val cpu = CPU(instructions = parseInstructions(input.lines))

        var nextSignal = 20
        val signals = mutableListOf<Int>()
        for (i in 0..220) {
            if (i == nextSignal) {
                signals += i * cpu.x
                nextSignal += 40
            }
            cpu.cycle()
        }

        return signals.sum()

    }

    override fun solveLevel2(): Any {
        val cpu = CPU(instructions = parseInstructions(input.lines))

        val cols = 40
        val rows = 6

        val grid = Grid(rows, cols) { r, c ->
            cpu.cycle()
            val sprite = cpu.x
            c in sprite-1..sprite+1
        }

        return grid.toString()
    }
}
