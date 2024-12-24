@file:Suppress("UnstableApiUsage")
package y24

import com.google.common.graph.Graph
import com.google.common.graph.GraphBuilder
import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2024, day = 23) { Day23(it) }

class Day23(val input: Input) : Puzzle {
    private val links = input.lines.map {
        val (a, b) = it.split("-")
        a to b
    }

    private fun largestFullyConnectedSet(graph: Graph<String>, edges: List<String>, curSet: MutableSet<String>, i: Int): Set<String> {
        // Try adding edges i+
        var maxSet = curSet.toSet()
        for (j in i until edges.size) {
            // Try adding j
            if (curSet.any { it !in graph.adjacentNodes(edges[j]) }) {
                continue
            }

            curSet += edges[j]
            val set = largestFullyConnectedSet(graph, edges, curSet, j + 1)
            if (set.size > maxSet.size) {
                maxSet = set.toSet()
            }
            curSet -= edges[j]
        }

        return maxSet
    }

    override fun solveLevel1(): Any {
        val graph = GraphBuilder.undirected().build<String>()
        links.forEach { (a, b) ->
            graph.putEdge(a, b)
        }

        val uniqueSets = mutableSetOf<List<String>>()

        graph.nodes().forEach { node ->
            val edges = graph.adjacentNodes(node).toList()
            for (i in edges.indices) {
                for (j in i + 1 until edges.size) {
                    if (edges[j] !in graph.adjacentNodes(edges[i])) {
                        continue
                    }

                    val set = listOf(node, edges[i], edges[j])
                    if (set.none { it.startsWith('t') }) {
                        continue
                    }

                    uniqueSets += set.sorted()
                }
            }
        }

        return uniqueSets.size
    }

    override fun solveLevel2(): Any {
        val graph = GraphBuilder.undirected().build<String>()
        links.forEach { (a, b) ->
            graph.putEdge(a, b)
        }

        var maxSet: Set<String>? = null
        graph.nodes().forEach { node ->
            val max = largestFullyConnectedSet(graph, graph.adjacentNodes(node).toList(), mutableSetOf(node), 0)
            if (maxSet == null || max.size > maxSet!!.size) {
                maxSet = max
            }
        }

        return maxSet!!.sorted().joinToString(",")
    }
}
