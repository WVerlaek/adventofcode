@file:Suppress("UnstableApiUsage")

package common.datastructures.flow

import com.google.common.graph.ValueGraph
import java.util.*

object Dinics {
    fun <T> maxFlow(graph: ValueGraph<T & Any, Int>, src: T, sink: T): Int {
        return maxFlowGraph(graph, src, sink).first
    }

    data class MinCut<T>(
        val srcSet: Set<T>,
        val cut: List<Pair<T, T>>,
    )

    fun <T> minCut(graph: ValueGraph<T & Any, Int>, src: T, sink: T): MinCut<T> {
        val (_, nodes) = maxFlowGraph(graph, src, sink)

        val reachable = mutableSetOf<T>()
        val queue = LinkedList<Node<T & Any>>().also { it.add(nodes[src]!!) }
        while (queue.isNotEmpty()) {
            val cur = queue.removeFirst()
            if (cur.node in reachable) {
                continue
            }
            reachable += cur.node

            cur.edges.forEach { edge ->
                if (edge.flow == edge.capacity) {
                    return@forEach
                }

                queue += edge.to
            }
        }

        val cut = graph.edges().mapNotNull { edge ->
            val isMinCut = edge.nodeU() in reachable != edge.nodeV() in reachable
            if (isMinCut) {
                return@mapNotNull edge.nodeU() to edge.nodeV()
            }
            return@mapNotNull null
        }

        return MinCut(reachable, cut)
    }

    private fun <T> maxFlowGraph(graph: ValueGraph<T & Any, Int>, src: T, sink: T): Pair<Int, Map<T & Any, Node<T & Any>>> {
        val nodes = graph.toFlowGraph()
        val srcNode = nodes[src]!!
        val sinkNode = nodes[sink]!!
        var maxFlow = 0
        while (levelGraph(nodes, srcNode, sinkNode)) {
            var flow: Int
            do {
                flow = sendFlow(srcNode, sinkNode, Int.MAX_VALUE)
                maxFlow += flow
            } while (flow > 0)
        }
        return maxFlow to nodes
    }

    class Node<T>(
        val node: T,
        var level: Int,
        var edges: List<Edge<T>>,
    )
    data class Edge<T>(
        val to: Node<T>,
        val capacity: Int,
        var flow: Int,
    ) {
        lateinit var reverseEdge: Edge<T>
    }

    private fun <T> sendFlow(from: Node<T>, sink: Node<T>, flow: Int): Int {
        if (from == sink) {
            return flow
        }
        from.edges.forEach { edge ->
            if (edge.to.level == from.level + 1 && edge.capacity > edge.flow) {
                val minFlow = minOf(flow, edge.capacity - edge.flow)
                val resultFlow = sendFlow(edge.to, sink, minFlow)
                if (resultFlow > 0) {
                    edge.flow += resultFlow
                    edge.reverseEdge.flow -= resultFlow
                    return resultFlow
                }
            }
        }
        return 0
    }

    private fun <T> ValueGraph<T & Any, Int>.toFlowGraph(): Map<T, Node<T>> {
        val nodes = nodes().toList().associateWith { node ->
            Node(node, -1, emptyList())
        }

        fun addEdge(from: Node<T>, to: Node<T>, capacity: Int) {
            val edge = Edge(to, capacity, 0)
            val reverse = Edge(from, capacity, capacity)
            edge.reverseEdge = reverse
            reverse.reverseEdge = edge
            from.edges += edge
            to.edges += reverse
        }

        edges().forEach { edge ->
            val from = nodes[edge.nodeU()]!!
            val to = nodes[edge.nodeV()]!!
            val value = edgeValue(edge).get()
            addEdge(from, to, value)
            if (!edge.isOrdered) {
                addEdge(to, from, value)
            }
        }

        return nodes
    }

    private fun <T> levelGraph(nodes: Map<T, Node<T>>, src: Node<T>, sink: Node<T>): Boolean {
        nodes.values.forEach { it.level = -1 }
        src.level = 0

        val queue = LinkedList<Node<T>>().also { it.add(src) }
        while (queue.isNotEmpty()) {
            val cur = queue.removeFirst()
            for (e in cur.edges) {
                if (e.flow < e.capacity && e.to.level == -1) {
                    e.to.level = cur.level + 1
                    queue += e.to
                }
            }
        }

        return sink.level > 0
    }
}