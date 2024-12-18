package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.puzzle.splitToInts
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2024, day = 17, dryRun = true) { Day17(it) }

data class Computer(
    var a: Long,
    var b: Long,
    var c: Long,
) {
    var output: MutableList<Int> = mutableListOf()
}

fun Computer.readCombo(combo: Int): Long {
    return when (combo) {
        4 -> a
        5 -> b
        6 -> c
        else -> combo.toLong()
    }
}

fun Computer.run(program: List<Int>, requireOutput: List<Int>?): Boolean {
    var pointer = 0
    while (pointer in program.indices) {
        val instr = Instruction.entries[program[pointer]]
        val value = program[pointer + 1]
        instr.op(this, value)?.let {
            pointer = it
        } ?: run { pointer += 2 }

        if (instr == Instruction.Out && requireOutput != null) {
            if (output.last() != requireOutput[output.size - 1]) {
                return false
            }
        }
    }

    return requireOutput?.let { it.size == output.size } ?: true
}

enum class Instruction(val op: (Computer, Int) -> Int?) {
    Adv({ comp, combo ->
        comp.a = comp.a shr comp.readCombo(combo).toInt()
        null
    }),
    Bxl({ comp, literal ->
        comp.b = comp.b xor literal.toLong()
        null
    }),
    Bst({ comp, combo ->
        comp.b = comp.readCombo(combo) % 8
        null
    }),
    Jnz({ comp, literal ->
        if (comp.a == 0L) {
            null
        } else {
            literal
        }
    }),
    Bxc({ comp, _ ->
        comp.b = comp.b xor comp.c
        null
    }),
    Out({ comp, combo ->
        comp.output += (comp.readCombo(combo) % 8).toInt()
        null
    }),
    Bdv({ comp, combo ->
        comp.b = comp.a shr comp.readCombo(combo).toInt()
        null
    }),
    Cdv({ comp, combo ->
        comp.c = comp.a shr comp.readCombo(combo).toInt()
        null
    })
}

class Day17(val input: Input) : Puzzle {

    private fun parseInput(lines: List<String>): Pair<Computer, List<Int>> {
        val a = lines[0].substringAfter(": ").toLong()
        val b = lines[1].substringAfter(": ").toLong()
        val c = lines[2].substringAfter(": ").toLong()
        val program = lines[4].substringAfter(": ")
            .splitToInts()
        return Computer(a, b, c) to program
    }

    override fun solveLevel1(): Any {
        val (computer, program) = parseInput(input.lines)
        computer.run(program, requireOutput = null)
        return computer.output.joinToString(",")
    }

    // Program:
    // b = (a % 8) xor 1
    // c = a / 2^b = a >> b
    // b = b xor 5 xor c
    // out(b % 8)
    // a = a / 2^3 = a >> 3
    private fun findA(program: List<Int>, from: Int): Long {
        var a = if (from == program.size - 1) {
            0
        } else {
            findA(program, from + 1) shl 3
        }

        while (true) {
            val comp = Computer(a, 0, 0)
            if (comp.run(program, program.subList(from, program.size))) {
                return a
            }

            a++
        }
    }

    override fun solveLevel2(): Any {
        val (_, program) = parseInput(input.lines)
        return findA(program, 0)
    }
}
