@file:Suppress("UnstableApiUsage")

package y21

import com.google.common.graph.MutableValueGraph
import com.google.common.graph.ValueGraph
import com.google.common.graph.ValueGraphBuilder
import common.datastructures.Point
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import java.util.*

fun main() = solvePuzzle(2021, 23, 1) { Day23(it) }

// #############
// #01.4.7.A.DE#
// ###2#5#8#B###
//   #3#6#9#C#
//   #########

enum class NodeType {
    Hall, A, B, C, D
}

val NodeType.energy: Int
    get() = when (this) {
        NodeType.A -> 1
        NodeType.B -> 10
        NodeType.C -> 100
        NodeType.D -> 1000
        else -> throw IllegalStateException()
    }

data class Node(val id: Int, val type: NodeType)

val nodes = listOf(
    NodeType.Hall,
    NodeType.Hall,
    NodeType.A,
    NodeType.A,
    NodeType.Hall,
    NodeType.B,
    NodeType.B,
    NodeType.Hall,
    NodeType.C,
    NodeType.C,
    NodeType.Hall,
    NodeType.D,
    NodeType.D,
    NodeType.Hall,
    NodeType.Hall,
).mapIndexed { index, nodeType -> Node(index, nodeType) }

val firstRoomForType = mapOf(
    NodeType.A to 2,
    NodeType.B to 5,
    NodeType.C to 8,
    NodeType.D to 11,
)

fun newBurrow(): ValueGraph<Node, Int> {
    // # # # # # # # # # # # # #
    // # 0 1 . 4 . 7 . 10. 1314#
    // # # # 2 # 5 # 8 # 11# # #
    //     # 3 # 6 # 9 # 12#
    //     # # # # # # # # #
    val graph: MutableValueGraph<Node, Int> = ValueGraphBuilder.undirected().build()
    nodes.forEach { graph.addNode(it) }
    graph.putEdgeValue(nodes[0], nodes[1], 1)
    graph.putEdgeValue(nodes[1], nodes[2], 2)
    graph.putEdgeValue(nodes[1], nodes[4], 2)
    graph.putEdgeValue(nodes[2], nodes[3], 1)
    graph.putEdgeValue(nodes[2], nodes[4], 2)
    graph.putEdgeValue(nodes[4], nodes[5], 2)
    graph.putEdgeValue(nodes[4], nodes[7], 2)
    graph.putEdgeValue(nodes[5], nodes[6], 1)
    graph.putEdgeValue(nodes[5], nodes[7], 2)
    graph.putEdgeValue(nodes[7], nodes[8], 2)
    graph.putEdgeValue(nodes[7], nodes[10], 2)
    graph.putEdgeValue(nodes[8], nodes[9], 1)
    graph.putEdgeValue(nodes[8], nodes[10], 2)
    graph.putEdgeValue(nodes[10], nodes[11], 2)
    graph.putEdgeValue(nodes[10], nodes[13], 2)
    graph.putEdgeValue(nodes[11], nodes[12], 1)
    graph.putEdgeValue(nodes[11], nodes[13], 2)
    graph.putEdgeValue(nodes[13], nodes[14], 1)
    return graph
}

class Day23(val input: Input) : Puzzle {
    data class State(val aa: List<Int>, val bb: List<Int>, val cc: List<Int>, val dd: List<Int>, val lastMoved: Int?) {
        init {
            // TODO Remove
//            require(aa[0] <= aa[1])
//            require(bb[0] <= bb[1])
//            require(cc[0] <= cc[1])
//            require(dd[0] <= dd[1])
        }
        val occupiedNodes = (aa + bb + cc + dd) //.toSet()

        fun isTarget() = aa[0] == 2 && aa[1] == 3 && bb[0] == 5 && bb[1] == 6 && cc[0] == 8 && cc[1] == 9 && dd[0] == 11 && dd[1] == 12

//        fun isTarget() = aa.all { nodes[it].type == NodeType.A } && bb.all { nodes[it].type == NodeType.B } && cc.all { nodes[it].type == NodeType.C } && dd.all { nodes[it].type == NodeType.D }

        companion object {
            fun parse(lines: List<String>): State {
                val posToNode = mapOf(
                    Point(3, 2) to 2,
                    Point(3, 3) to 3,
                    Point(5, 2) to 5,
                    Point(5, 3) to 6,
                    Point(7, 2) to 8,
                    Point(7, 3) to 9,
                    Point(9, 2) to 11,
                    Point(9, 3) to 12,
                )
                val aa = mutableListOf<Int>()
                val bb = mutableListOf<Int>()
                val cc = mutableListOf<Int>()
                val dd = mutableListOf<Int>()
                posToNode.forEach { (p, node) ->
                    when (val c = lines[p.row][p.col]) {
                        'A' -> aa += node
                        'B' -> bb += node
                        'C' -> cc += node
                        'D' -> dd += node
                        else -> throw IllegalArgumentException("Invalid char $c at $p")
                    }
                }
                return State(aa.sorted(), bb.sorted(), cc.sorted(), dd.sorted(), null)
            }
        }
    }

    val initialState = State.parse(input.lines)

    override fun solveLevel1(): Any {
        val burrow = newBurrow()

        data class Cost(val cost: Int, val state: State)

        val queue = PriorityQueue<Cost>(compareBy { it.cost })
        queue += Cost(0, initialState)
        val visited = mutableSetOf<State>()

        var pathFindCount = 0

        while (queue.isNotEmpty()) {
            val cur = queue.poll()
            val state = cur.state

            if (state.isTarget()) {
                return cur.cost
            }

            if (!visited.add(cur.state)) {
                continue
            }

            if (cur.cost % 100 == 0) {
                println("${cur.cost} pathFind $pathFindCount")
            }

            data class Next(val newNodes: List<Int>, val cost: Int, val lastMoved: Int?)
            fun tryMoves(nodeIds: List<Int>, target: NodeType, state: State): Sequence<Next> {
                return sequence {
                    nodeIds.forEachIndexed { index, nodeIdx ->
                        val node = nodes[nodeIdx]
                        if (node.type == NodeType.Hall && state.lastMoved != nodeIdx) {
                            // Locked, can only move directly into a room.

                            // Try moving directly into type's room.
                            val targetNodeIdx = firstRoomForType.getValue(target)
                            if (targetNodeIdx in state.occupiedNodes) {
                                // Can't move, room is occupied.
                                return@forEachIndexed
                            }

                            // Check if room neighbour doesn't contain different type.
                            val toCheck = when (targetNodeIdx) {
                                2 -> 3
                                5 -> 6
                                8 -> 9
                                11 -> 12
                                else -> -1
                            }
                            if (toCheck in state.occupiedNodes && toCheck !in nodeIds) {
                                // Other type is already in this room. Can't move in.
                                return@forEachIndexed
                            }

                            val pathToNode = burrow.findShortestUnoccupiedPath(nodeIdx, targetNodeIdx, state.occupiedNodes)
                                ?: return@forEachIndexed // No path found.
                            pathFindCount++

                            // Try moving into room.
                            var cost = 0
                            for (i in 0 until pathToNode.size - 1) {
                                val edgeCost = burrow.edgeValue(nodes[pathToNode[i]], nodes[pathToNode[i+1]]).get()
                                cost += edgeCost
                            }
                            val totalCost = cost * target.energy
                            val newList = nodeIds.toMutableList()
                            newList[index] = targetNodeIdx
                            yield(Next(newList.sorted(), totalCost, targetNodeIdx))
                            return@forEachIndexed
                        }

                        for (adj in burrow.adjacentNodes(node)) {
                            val adjIdx = adj.id
                            if (adjIdx in state.occupiedNodes) continue

                            if (adj.type != target && adj.type != node.type && adj.type != NodeType.Hall) {
                                // Trying to move into another room type, and wasn't there already
                                continue
                            }

                            if (node.type == target && adj.type != target) {
                                // Cannot move out of target room.
                                val toCheck = when (node.id) {
                                    2 -> 3
                                    5 -> 6
                                    8 -> 9
                                    11 -> 12
                                    else -> -1
                                }
                                // todo...
                                if (toCheck in nodeIds || toCheck !in state.occupiedNodes) {
                                    // Can't move out, all nodes in this room already of the right type.
                                    continue
                                }
                            }

                            if (node.type == target && adjIdx < node.id) {
                                // Don't move towards door if already in correct room.
                                continue
                            }

                            if (node.type == NodeType.Hall && adj.type == target) {
                                // Check if room doesn't contain different type.
                                val toCheck = when (adj.id) {
                                    2 -> 3
                                    5 -> 6
                                    8 -> 9
                                    11 -> 12
                                    else -> -1
                                }
                                if (toCheck in state.occupiedNodes && toCheck !in nodeIds) {
                                    // Other type is already in this room. Can't move in.
                                    continue
                                }
                            }

                            // TODO Other rules

                            val cost = burrow.edgeValue(node, adj).get() * target.energy
                            // Move node to adj.
                            val newList = nodeIds.toMutableList()
                            newList[index] = adjIdx
                            yield(Next(newList.sorted(), cost, adjIdx))
                        }
                    }
                }
            }

            for ((newAA, cost, lastMoved) in tryMoves(state.aa, NodeType.A, state)) {
                val newState = state.copy(aa = newAA, lastMoved = lastMoved)
                queue += Cost(cur.cost + cost, newState)
            }
            for ((newBB, cost, lastMoved) in tryMoves(state.bb, NodeType.B, state)) {
                val newState = state.copy(bb = newBB, lastMoved = lastMoved)
                queue += Cost(cur.cost + cost, newState)
            }
            for ((newCC, cost, lastMoved) in tryMoves(state.cc, NodeType.C, state)) {
                val newState = state.copy(cc = newCC, lastMoved = lastMoved)
                queue += Cost(cur.cost + cost, newState)
            }
            for ((newDD, cost, lastMoved) in tryMoves(state.dd, NodeType.D, state)) {
                val newState = state.copy(dd = newDD, lastMoved = lastMoved)
                queue += Cost(cur.cost + cost, newState)
            }
        }

        throw IllegalStateException("Didn't find solution.")
    }

    override fun solveLevel2(): Any {
        TODO()
    }
}

data class Dist(val dist: Int, val node: Int, val parent: Dist?) {
    override fun hashCode(): Int {
        return Objects.hash(dist, node)
    }

    override fun equals(other: Any?): Boolean {
        return other is Dist && dist == other.dist && node == other.node
    }
}
val reusableQueue = PriorityQueue<Dist>(compareBy { it.dist })
val reusableVisited = mutableSetOf<Int>()
val reusableParents = Array<Dist?>(nodes.size) { null }

fun ValueGraph<Node, Int>.findShortestUnoccupiedPath(from: Int, to: Int, occupied: List<Int>): List<Int>? {
    reusableQueue.clear()
    reusableVisited.clear()
    reusableParents.forEachIndexed { index, _ -> reusableParents[index] = null }
    reusableQueue += Dist(0, from, null)

    fun path(d: Dist): List<Int> {
        return (d.parent?.let { path(it) } ?: emptyList()) + d.node
    }

    while (reusableQueue.isNotEmpty()) {
        val cur = reusableQueue.poll()
        if (!reusableVisited.add(cur.node)) {
            continue
        }

        if (cur.node == to) {
            return path(cur)
        }

        val curNode = nodes[cur.node]
        cur.parent?.let { reusableParents[cur.node] = it }
        for (adj in adjacentNodes(curNode)) {
            if (adj.id in occupied) {
                continue
            }
            val cost = edgeValue(curNode, adj).get()
            reusableQueue += Dist(cur.dist + cost, adj.id, cur)
        }
    }

    return null
}
