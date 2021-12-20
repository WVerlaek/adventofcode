@file:Suppress("UnstableApiUsage")

package common.ext

import com.google.common.graph.Graph

fun <T,S> Graph<T>.reduceGraphBFS(node: T, visited: MutableSet<T> = HashSet(), reduction: (node: T, reducedChildren: List<S>) -> S): S {
    val children = successors(node) - visited
    visited += children
    val reduced = children.map { reduceGraphBFS(it, visited, reduction) }
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
