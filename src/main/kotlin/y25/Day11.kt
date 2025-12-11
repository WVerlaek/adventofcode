package y25

import com.google.common.graph.Graph
import com.google.common.graph.GraphBuilder
import com.google.common.graph.Graphs
import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2025, day = 11) { Day11(it) }

@Suppress("UnstableApiUsage")
class Day11(val input: Input) : Puzzle {

    private fun parseInput(): Graph<String> {
        val graph = GraphBuilder<String>.directed().build<String>()

        input.lines.forEach { line ->
            val (node, outputs) = line.split(": ")
            val outputNodes = outputs.split(" ")
            outputNodes.forEach { outputNode ->
                graph.putEdge(node, outputNode)
            }
        }

        return graph
    }

    data class Memoized(
        val node: String,
        val missingNodes: Set<String>,
    )

    private fun Graph<String>.pathsToNode(
        from: String,
        target: String,
        requiredNodes: MutableSet<String>,
        visited: MutableSet<String> = mutableSetOf(),
        cache: MutableMap<Memoized, Long> = mutableMapOf(),
    ): Long {
        if (from == target) return if (requiredNodes.isEmpty()) 1 else 0

        if (from in visited) {
            // Found cycle, ignore.
            return 0
        }
        visited += from

        var addBack = false
        if (from in requiredNodes) {
            requiredNodes -= from
            addBack = true
        }

        val memo = Memoized(from, requiredNodes.toSet())
        val result = cache[memo] ?: successors(from).sumOf { neigh ->
            pathsToNode(neigh, target, requiredNodes, visited, cache)
        }.also { cache[memo] = it }

        if (addBack) {
            requiredNodes += from
        }
        visited -= from

        return result
    }

    override fun solveLevel1(): Any {
        val graph = parseInput()
        return graph.pathsToNode("you", "out", mutableSetOf())
    }

    override fun solveLevel2(): Any {
        val graph = parseInput()
        return graph.pathsToNode("svr", "out", mutableSetOf("dac", "fft"))
    }
}
