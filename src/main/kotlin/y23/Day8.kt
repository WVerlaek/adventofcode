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


fun main() = solvePuzzle(year = 2023, day = 8, dryRun = true) { Day8(it) }

class Day8(val input: Input) : Puzzle {
    private val lineRegex = Regex("""([A-Z0-9]+) = \(([A-Z0-9]+), ([A-Z0-9]+)\)""")

    enum class Instruction(val c: Char) { Left('L'), Right('R') }
    class Instructions(val list: List<Instruction>) {
        operator fun get(i: Int): Instruction = list[i % list.size]
    }

    class Graph(val adj: MutableMap<String, Pair<String, String>> = mutableMapOf()) {
        fun putAdjacent(node: String, left: String, right: String) {
            adj[node] = left to right
        }

        operator fun get(node: String): Pair<String, String> = adj.getValue(node)
    }

    private fun parseInput(lines: List<String>): Pair<Instructions, Graph> {
        val instructions = Instructions(lines[0].map { c -> Instruction.entries.first { it.c == c } })

        val graph = Graph()
        lines.subList(2, lines.size).forEach { line ->
            val (_, from, left, right) = lineRegex.matchEntire(line)
                ?.groupValues
                ?: error("invalid input: $line")
            graph.putAdjacent(from, left, right)
        }

        return instructions to graph
    }

    override fun solveLevel1(): Any {
        val (instr, graph) = parseInput(input.lines)

        var cur = "AAA"
        var steps = 0
        while (cur != "ZZZ") {
            val instruction = instr[steps]
            val (left, right) = graph[cur]
            cur = when (instruction) {
                Instruction.Left -> left
                Instruction.Right -> right
            }

            steps++
        }

        return steps
    }

    data class Cycle(
        val start: String,
        val offset: Int,
        val len: Int,
        val endNodes: List<Int>,
    ) {
        fun isEnd(step: Long): Boolean {
            val cycleIdx = (step - offset) % len
            return cycleIdx.toInt() in endNodes
        }
    }

    private fun findCycle(instructions: Instructions, graph: Graph, start: String): Cycle {
        // Map of <Node, InstructionIndex> to step.
        val visited = mutableMapOf<Pair<String, Int>, Int>()

        var cur = start to 0
        var step = 0
        while (cur !in visited) {
            visited[cur] = step

            val instruction = instructions[step]
            val (left, right) = graph[cur.first]
            val next = when (instruction) {
                Instruction.Left -> left
                Instruction.Right -> right
            }
            cur = next to ((step + 1) % instructions.list.size)

            step++
        }

        // Found start of cycle
        val cycleStart = cur
        val cycleOffset = visited.getValue(cur)
        val len = step - cycleOffset

        val endNodes = visited.entries.filter { it.key.first.endsWith("Z") }
        val endOffsets = endNodes.map { (_, step) -> step - cycleOffset }
        return Cycle(cycleStart.first, cycleOffset, len, endOffsets)
    }

    data class SimpleCycle(val offset: Long, val len: Long)
    private fun combinedCycle(c1: SimpleCycle, c2: SimpleCycle): SimpleCycle {
        val (g, x, _) = extendedGcd(c1.len, c2.len)

        if ((c1.offset - c2.offset) % g != 0L) {
            error("impossible!")
        }
        val d = (c1.offset - c2.offset) / g

        val combinedLen = c1.len / g * c2.len
        val combinedOffset = (c1.offset - x * d * c1.len) % combinedLen
        return SimpleCycle(combinedOffset, combinedLen)
    }

    override fun solveLevel2(): Any {
        val (instr, graph) = parseInput(input.lines)
        val startingNodes = graph.adj.keys.filter { it.endsWith("A") }
        val cycles = startingNodes.map { findCycle(instr, graph, it) }
            .also { println(it) }
            .map { cycle ->
                val offset = cycle.offset + cycle.endNodes.requireSingleElement()
                val len = cycle.len
                SimpleCycle(offset.toLong(), len.toLong())
            }

        val cycle = cycles.reduce { acc, cycle ->
            combinedCycle(acc, cycle)
        }
        return cycle.len // offset == 0
//        }
    }
}
