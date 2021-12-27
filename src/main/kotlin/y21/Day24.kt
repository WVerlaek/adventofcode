package y21

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 24, 2) { Day24(it) }

enum class Register {
    W, X, Y, Z
}

sealed class Value {
    data class Number(val n: Long) : Value()
    data class Variable(val register: Register) : Value()
}

fun interface Operation {
    operator fun invoke(a: Long, b: Long): Long

    companion object {
        val Add = Operation { a, b -> a + b }
        val Mul = Operation { a, b -> a * b }
        val Div = Operation { a, b -> a / b }
        val Mod = Operation { a, b -> a % b }
        val Eql = Operation { a, b -> if (a == b) 1L else 0L }
    }
}

sealed class Instruction {
    data class Inp(val a: Register) : Instruction()
    data class Op(val a: Register, val b: Value, val operation: Operation) : Instruction()

    companion object {
        fun parse(s: String): Instruction {
            val split = s.split(" ")
            val instruction = split[0]
            val a = Register.valueOf(split[1].uppercase())
            if (instruction == "inp") {
                return Inp(a)
            }

            val b = split[2].toLongOrNull()?.let { Value.Number(it) }
                ?: Register.valueOf(split[2].uppercase()).let { Value.Variable(it) }
            val op = when (instruction) {
                "add" -> Operation.Add
                "mul" -> Operation.Mul
                "div" -> Operation.Div
                "mod" -> Operation.Mod
                "eql" -> Operation.Eql
                else -> throw IllegalArgumentException("Unknown instruction $instruction")
            }
            return Op(a, b, op)
        }
    }
}

data class Registers(val values: LongArray = LongArray(Register.values().size)) {
    operator fun get(register: Register) = values[register.ordinal]
    operator fun set(register: Register, value: Long) { values[register.ordinal] = value }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is Registers && values.contentEquals(other.values)
    }

    override fun hashCode(): Int {
        return values.contentHashCode()
    }

    fun copyOf(): Registers {
        return Registers(values.copyOf())
    }
}

class ALU(val registers: Registers = Registers()) {
    fun executeAll(instructions: List<Instruction>, input: Long) {
        instructions.forEach { execute(it, input) }
    }

    fun execute(instruction: Instruction, input: Long) {
        when (instruction) {
            is Instruction.Inp -> registers[instruction.a] = input
            is Instruction.Op -> registers[instruction.a] = instruction.operation(registers[instruction.a], valueOf(instruction.b))
        }
    }

    fun getValue(register: Register) = registers[register]

    private fun valueOf(value: Value): Long {
        return when (value) {
            is Value.Number -> value.n
            is Value.Variable -> registers[value.register]
        }
    }
}

class Day24(val input: Input) : Puzzle {
    private val digitInstructions: List<List<Instruction>>
    private val allInstructions = input.lines.map { Instruction.parse(it) }
    init {
        val result = mutableListOf<List<Instruction>>()
        var cur = 0
        while (cur < allInstructions.size) {
            val curInstructions = mutableListOf<Instruction>()
            do {
                curInstructions += allInstructions[cur]
                cur++
            } while (cur < allInstructions.size && allInstructions[cur] !is Instruction.Inp)
            result += curInstructions
        }
        digitInstructions = result
    }

    data class SubProblem(val i: Int, val registers: Registers)
    class Memoized(
        val digitInstructions: List<List<Instruction>>,
        val order: IntProgression,
        val memo: HashMap<SubProblem, CharArray?> = HashMap()
    ) {
        fun solveFor(subProblem: SubProblem): CharArray? {
            val (i, registers) = subProblem
            if (subProblem in memo) {
                return memo.getValue(subProblem)
            }

            val instructionsForDigit = digitInstructions[i]
            var result: CharArray? = null
            for (d in order) {
                val alu = ALU(registers.copyOf())
                alu.executeAll(instructionsForDigit, d.toLong())
                when {
                    i >= digitInstructions.size - 1 -> {
                        // Check if Z == 0, and return d (highest allowed number).
                        if (alu.registers[Register.Z] == 0L) {
                            result = charArrayOf(d.digitToChar())
                            break
                        }
                    }
                    else -> {
                        val subProblemSolution = solveFor(SubProblem(i + 1, alu.registers))
                            ?: continue
                        result = charArrayOf(d.digitToChar()) + subProblemSolution
                        break
                    }
                }
            }

            memo[subProblem] = result
            return result
        }
    }

    override fun solveLevel1(): Any {
        val memoized = Memoized(digitInstructions, 9 downTo 1)
        return memoized.solveFor(SubProblem(0, Registers()))!!
            .joinToString("") { it.toString() }
            .toLong()
    }

    override fun solveLevel2(): Any {
        val memoized = Memoized(digitInstructions, 1..9)
        return memoized.solveFor(SubProblem(0, Registers()))!!
            .joinToString("") { it.toString() }
            .toLong()
    }
}
