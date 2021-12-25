@file:Suppress("UnstableApiUsage")

package common.ext

import com.google.common.graph.Graph
import com.google.common.graph.ValueGraph
import java.util.*

fun <T,S> Graph<T>.reduceGraphDfs(node: T, visited: MutableSet<T> = HashSet(), reduction: (node: T, reducedChildren: List<S>) -> S): S {
    val children = successors(node) - visited
    visited += children
    val reduced = children.map { reduceGraphDfs(it, visited, reduction) }
    return reduction(node, reduced)
}

fun <T,S> Graph<T>.dfs(node: T, visited: MutableSet<T> = HashSet(), parent: S? = null, transform: (parent: S?, node: T) -> S) {
    val children = successors(node) - visited
    visited += children
    val transformed = transform(parent, node)
    children.forEach { child ->
        dfs(child, visited, transformed, transform)
    }
}

fun <T> ValueGraph<T, Int>.findShortestPath(from: T, to: T): List<T> {
    data class Dist(val dist: Int, val node: T, val parent: Dist?)
    val queue = PriorityQueue<Dist>(compareBy { it.dist })
    val visited = mutableSetOf<Dist>()
    val parents = mutableMapOf<T, Dist>()
    queue += Dist(0, from, null)

    fun path(d: Dist): List<T> {
        return (d.parent?.let { path(it) } ?: emptyList()) + d.node
    }

    while (queue.isNotEmpty()) {
        val cur = queue.poll()
        if (!visited.add(cur)) {
            continue
        }

        if (cur.node == to) {
            return path(cur)
        }

        cur.parent?.let { parents[cur.node] = it }
        for (adj in adjacentNodes(cur.node)) {
            val cost = edgeValue(cur.node, adj).get()
            queue += Dist(cur.dist + cost, adj, cur)
        }
    }

    throw IllegalStateException("No path found")
}
