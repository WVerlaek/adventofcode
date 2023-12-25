@file:Suppress("UnstableApiUsage")

package y23

import com.google.common.graph.*
import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.datastructures.flow.Dinics
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2023, day = 25) { Day25(it) }

class Day25(val input: Input) : Puzzle {

    private fun createGraph(lines: List<String>): MutableValueGraph<String, Int> {
        val graph = ValueGraphBuilder.undirected()
            .build<String, Int>()
        lines.forEach { line ->
            val from = line.substringBefore(":")
            val to = line.substringAfter(": ").split(" ")
            to.forEach { graph.putEdgeValue(from, it, 1) }
        }
        return graph
    }

    override fun solveLevel1(): Any {
        val graph = createGraph(input.lines)
        val nodes = graph.nodes().toList()
        for (a in nodes.indices) {
            for (b in a + 1 until nodes.size) {
                val maxFlow = Dinics.maxFlow(graph, nodes[a], nodes[b])
                if (maxFlow == 3) {
                    val (srcSet, _) = Dinics.minCut(graph, nodes[a], nodes[b])
                    return srcSet.size * (nodes.size - srcSet.size)
                }
            }
        }

        error("did not find cut of size 3")
    }

    override fun solveLevel2(): Any {
        return 42
    }
}
