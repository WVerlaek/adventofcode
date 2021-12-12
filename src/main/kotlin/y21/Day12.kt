@file:Suppress("UnstableApiUsage")

package y21

import com.google.common.graph.Graph
import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 12, 2) { Day12(it) }

private typealias Path = List<Cave>

private data class Cave(val s: String) {
    val isSmall = s[0].isLowerCase()
    val isLarge = s[0].isUpperCase()

    companion object {
        val start = Cave("start")
        val end = Cave("end")
    }
}

class Day12(val input: Input) : Puzzle {
    private val graph: MutableGraph<Cave> = GraphBuilder.undirected().build()

    init {
        input.lines.forEach { line ->
            val (a, b) = line.split("-")
            val (na, nb) = Cave(a) to Cave(b)
            graph.addNode(na)
            graph.addNode(nb)
            graph.putEdge(na, nb)
        }
    }

    override fun solveLevel1(): Any {
        return graph.pathsToEnd(Cave.start, listOf(Cave.start), mutableSetOf(Cave.start), true)
            .also { println(it.map { path -> path.joinToString(",", prefix = "[", postfix = "]") { cave -> cave.s } }) }
            .size
    }

    private fun Graph<Cave>.pathsToEnd(from: Cave, pathInclFrom: Path, visitedSmallCaves: MutableSet<Cave>, visitedOneTwice: Boolean): List<Path> {
        if (from == Cave.end) {
            return listOf(pathInclFrom)
        }

        return this.adjacentNodes(from).flatMap { neigh ->
            if (neigh in visitedSmallCaves) {
                return@flatMap if (!visitedOneTwice && neigh != Cave.start) {
                    pathsToEnd(neigh, pathInclFrom + neigh, visitedSmallCaves, true)
                } else {
                    emptyList()
                }
            }

            // Go to neigh.
            if (neigh.isSmall) {
                visitedSmallCaves += neigh
            }

            val paths = pathsToEnd(neigh, pathInclFrom + neigh, visitedSmallCaves, visitedOneTwice)

            // Undo.
            visitedSmallCaves -= neigh

            paths
        }
    }

    override fun solveLevel2(): Any {
        return graph.pathsToEnd(Cave.start, listOf(Cave.start), mutableSetOf(Cave.start), false)
            .also { println(it.map { path -> path.joinToString(",", prefix = "[", postfix = "]") { cave -> cave.s } }) }
            .size
    }
}
