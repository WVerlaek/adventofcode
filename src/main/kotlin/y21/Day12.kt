@file:Suppress("UnstableApiUsage")

package y21

import com.google.common.graph.Graph
import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import common.Day

fun main() = Day12(2)

private typealias Path = List<Cave>

private data class Cave(val s: String) {
    val isSmall = s[0].isLowerCase()
    val isLarge = s[0].isUpperCase()

    companion object {
        val start = Cave("start")
        val end = Cave("end")
    }
}

object Day12 : Day(2021, 12) {
    init {
//        useSampleInput {
//            """
//                start-A
//                start-b
//                A-c
//                A-b
//                b-d
//                A-end
//                b-end
//            """.trimIndent()
//        }
        dryRun = true
    }

    private val graph: MutableGraph<Cave> = GraphBuilder.undirected().build()

    init {
        lines.forEach { line ->
            val (a, b) = line.split("-")
            val (na, nb) = Cave(a) to Cave(b)
            graph.addNode(na)
            graph.addNode(nb)
            graph.putEdge(na, nb)
        }
    }

    override fun level1(): String {
        return graph.pathsToEnd(Cave.start, listOf(Cave.start), mutableSetOf(Cave.start), true)
            .also { println(it.map { path -> path.joinToString(",", prefix = "[", postfix = "]") { cave -> cave.s } }) }
            .size
            .toString()
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

    override fun level2(): String {
        return graph.pathsToEnd(Cave.start, listOf(Cave.start), mutableSetOf(Cave.start), false)
            .also { println(it.map { path -> path.joinToString(",", prefix = "[", postfix = "]") { cave -> cave.s } }) }
            .size
            .toString()
    }
}
