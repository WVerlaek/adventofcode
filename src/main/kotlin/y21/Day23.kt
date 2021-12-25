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

// 2 took 1153979ms
//         301578ms
fun main() = solvePuzzle(2021, 23, 2) { Day23(it) }

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
    NodeType.A,
    NodeType.A,
    NodeType.Hall,
    NodeType.B,
    NodeType.B,
    NodeType.B,
    NodeType.B,
    NodeType.Hall,
    NodeType.C,
    NodeType.C,
    NodeType.C,
    NodeType.C,
    NodeType.Hall,
    NodeType.D,
    NodeType.D,
    NodeType.D,
    NodeType.D,
    NodeType.Hall,
    NodeType.Hall,
).mapIndexed { index, nodeType -> Node(index, nodeType) }

val firstRoomForType = mapOf(
    NodeType.A to 2,
    NodeType.B to 7,
    NodeType.C to 12,
    NodeType.D to 17,
)

val deeperRoomsForType = mapOf(
    NodeType.A to intArrayOf(3, 4, 5),
    NodeType.B to intArrayOf(8, 9, 10),
    NodeType.C to intArrayOf(13, 14, 15),
    NodeType.D to intArrayOf(18, 19, 20),
)

fun hallwayGraph(): ValueGraph<Node, Int> {
    // # # # # # # # # # # # # #
    // # 0 1 . 6 . 11. 16. 2122#
    // # # # 2 # 7 # 12# 17# # #
    val graph: MutableValueGraph<Node, Int> = ValueGraphBuilder.undirected().build()
    listOf(0, 1, 2, 6, 7, 11, 12, 16, 17, 21, 22).map { nodes[it] }.forEach { graph.addNode(it) }
    graph.putEdgeValue(nodes[0], nodes[1], 1)
    graph.putEdgeValue(nodes[1], nodes[2], 2)
    graph.putEdgeValue(nodes[1], nodes[6], 2)
    graph.putEdgeValue(nodes[2], nodes[6], 2)
    graph.putEdgeValue(nodes[6], nodes[7], 2)
    graph.putEdgeValue(nodes[6], nodes[11], 2)
    graph.putEdgeValue(nodes[7], nodes[11], 2)
    graph.putEdgeValue(nodes[11], nodes[12], 2)
    graph.putEdgeValue(nodes[11], nodes[16], 2)
    graph.putEdgeValue(nodes[12], nodes[16], 2)
    graph.putEdgeValue(nodes[16], nodes[17], 2)
    graph.putEdgeValue(nodes[16], nodes[21], 2)
    graph.putEdgeValue(nodes[17], nodes[21], 2)
    graph.putEdgeValue(nodes[21], nodes[22], 1)
    return graph
}

fun newBurrow(): ValueGraph<Node, Int> {
    // # # # # # # # # # # # # #
    // # 0 1 . 6 . 11. 16. 2122#
    // # # # 2 # 7 # 12# 17# # #
    //     # 3 # 8 # 13# 18#
    //     # 4 # 9 # 14# 19#
    //     # 5 # 10# 15# 20#
    //     # # # # # # # # #
    val graph: MutableValueGraph<Node, Int> = ValueGraphBuilder.undirected().build()
    nodes.forEach { graph.addNode(it) }
    graph.putEdgeValue(nodes[0], nodes[1], 1)
    graph.putEdgeValue(nodes[1], nodes[2], 2)
    graph.putEdgeValue(nodes[1], nodes[6], 2)
    graph.putEdgeValue(nodes[2], nodes[3], 1)
    graph.putEdgeValue(nodes[2], nodes[6], 2)
    graph.putEdgeValue(nodes[3], nodes[4], 1)
    graph.putEdgeValue(nodes[4], nodes[5], 1)
    graph.putEdgeValue(nodes[6], nodes[7], 2)
    graph.putEdgeValue(nodes[6], nodes[11], 2)
    graph.putEdgeValue(nodes[7], nodes[8], 1)
    graph.putEdgeValue(nodes[7], nodes[11], 2)
    graph.putEdgeValue(nodes[8], nodes[9], 1)
    graph.putEdgeValue(nodes[9], nodes[10], 1)
    graph.putEdgeValue(nodes[11], nodes[12], 2)
    graph.putEdgeValue(nodes[11], nodes[16], 2)
    graph.putEdgeValue(nodes[12], nodes[13], 1)
    graph.putEdgeValue(nodes[12], nodes[16], 2)
    graph.putEdgeValue(nodes[13], nodes[14], 1)
    graph.putEdgeValue(nodes[14], nodes[15], 1)
    graph.putEdgeValue(nodes[16], nodes[17], 2)
    graph.putEdgeValue(nodes[16], nodes[21], 2)
    graph.putEdgeValue(nodes[17], nodes[18], 1)
    graph.putEdgeValue(nodes[17], nodes[21], 2)
    graph.putEdgeValue(nodes[18], nodes[19], 1)
    graph.putEdgeValue(nodes[19], nodes[20], 1)
    graph.putEdgeValue(nodes[21], nodes[22], 1)
    return graph
}

class Day23(val input: Input) : Puzzle {
    // Each list must have ascending values.
    data class State(val aa: IntArray, val bb: IntArray, val cc: IntArray, val dd: IntArray, val lastMoved: Int?) {
        val occupiedNodes: IntArray = (aa + bb + cc + dd)
        val hash = occupiedNodes.contentHashCode()

        override fun equals(other: Any?): Boolean {
            return other is State && hash == other.hash && occupiedNodes.contentEquals(other.occupiedNodes)
        }

        override fun hashCode(): Int {
            return hash
        }

        fun isTarget() = hash == targetHash && occupiedNodes.contentEquals(target)
//            aa[0] == 2 && aa[1] == 3 && aa[2] == 4 && aa[3] == 5 &&
//            bb[0] == 7 && bb[1] == 8 && bb[2] == 9 && bb[3] == 10 &&
//            cc[0] == 12 && cc[1] == 13 && cc[2] == 14 && cc[3] == 15 &&
//            dd[0] == 17 && dd[1] == 18 && dd[2] == 19 && dd[3] == 20

        companion object {
            val target = intArrayOf(2, 3, 4, 5, 7, 8, 9, 10, 12, 13, 14, 15, 17, 18, 19, 20)
            val targetHash = target.contentHashCode()

            fun parse(lines: List<String>): State {
                val posToNode = mapOf(
                    Point(3, 2) to 2,
                    Point(3, 3) to 3,
                    Point(3, 4) to 4,
                    Point(3, 5) to 5,
                    Point(5, 2) to 7,
                    Point(5, 3) to 8,
                    Point(5, 4) to 9,
                    Point(5, 5) to 10,
                    Point(7, 2) to 12,
                    Point(7, 3) to 13,
                    Point(7, 4) to 14,
                    Point(7, 5) to 15,
                    Point(9, 2) to 17,
                    Point(9, 3) to 18,
                    Point(9, 4) to 19,
                    Point(9, 5) to 20,
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
                return State(aa.sorted().toIntArray(), bb.sorted().toIntArray(), cc.sorted().toIntArray(), dd.sorted().toIntArray(), null)
            }
        }
    }

    override fun solveLevel1(): Any {
        val initialState = State.parse(input.lines)
        return solve(initialState)
    }

    private fun solve(initialState: State): Int {
        val hallway = hallwayGraph()
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

            data class Next(val newNodes: IntArray, val cost: Int, val lastMoved: Int?)
            fun tryMoves(nodeIds: IntArray, target: NodeType, state: State): Sequence<Next> {
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
                            val roomsToCheck = deeperRoomsForType.getValue(target)
                            if (roomsToCheck.any { it in state.occupiedNodes && it !in nodeIds }) {
                                // Other type is already in this room. Can't move in.
                                return@forEachIndexed
                            }

                            val pathToNode = hallway.findShortestUnoccupiedPath(nodeIdx, targetNodeIdx, state.occupiedNodes)
                                ?: return@forEachIndexed // No path found.
                            pathFindCount++

                            // Try moving into room.
                            var cost = 0
                            for (i in 0 until pathToNode.size - 1) {
                                val edgeCost = burrow.edgeValue(nodes[pathToNode[i]], nodes[pathToNode[i+1]]).get()
                                cost += edgeCost
                            }
                            val totalCost = cost * target.energy
                            val newList = nodeIds.copyOf()
                            newList[index] = targetNodeIdx
                            newList.sort()
                            yield(Next(newList, totalCost, targetNodeIdx))
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
                                // Check if we should consider moving out of target room. Only do if there are other types in this room that need to go out.
                                val roomsToCheck = deeperRoomsForType.getValue(target)
                                if (roomsToCheck.none { it in state.occupiedNodes && it !in nodeIds }) {
                                    // Can't move out, all nodes in this room already of the right type.
                                    continue
                                }
                            }

                            if (node.type == target && adjIdx < node.id) {
                                // Don't move towards door if already in correct room and no other nodes behind me.
                                if (deeperRoomsForType.getValue(target).none { it > nodeIdx && it in state.occupiedNodes && it !in nodeIds }) {
                                    continue
                                }
                            }

                            if (node.type == NodeType.Hall && adj.type == target) {
                                // Check if room doesn't contain different type.
                                val roomsToCheck = deeperRoomsForType.getValue(target)
                                if (roomsToCheck.any { it in state.occupiedNodes && it !in nodeIds }) {
                                    // Other type is already in this room. Can't move in.
                                    continue
                                }
                            }

                            val cost = burrow.edgeValue(node, adj).get() * target.energy
                            // Move node to adj.
                            val newList = nodeIds.copyOf()
                            newList[index] = adjIdx
                            newList.sort()
                            yield(Next(newList, cost, adjIdx))
                        }
                    }
                }
            }

            for ((newAA, cost, lastMoved) in tryMoves(state.aa, NodeType.A, state)) {
                val newState = state.copy(aa = newAA, lastMoved = lastMoved)
                Cost(cur.cost + cost, newState).let { if (it.state !in visited) queue += it }
            }
            for ((newBB, cost, lastMoved) in tryMoves(state.bb, NodeType.B, state)) {
                val newState = state.copy(bb = newBB, lastMoved = lastMoved)
                Cost(cur.cost + cost, newState).let { if (it.state !in visited) queue += it }
            }
            for ((newCC, cost, lastMoved) in tryMoves(state.cc, NodeType.C, state)) {
                val newState = state.copy(cc = newCC, lastMoved = lastMoved)
                Cost(cur.cost + cost, newState).let { if (it.state !in visited) queue += it }
            }
            for ((newDD, cost, lastMoved) in tryMoves(state.dd, NodeType.D, state)) {
                val newState = state.copy(dd = newDD, lastMoved = lastMoved)
                Cost(cur.cost + cost, newState).let { if (it.state !in visited) queue += it }
            }
        }

        throw IllegalStateException("Didn't find solution.")
    }

    override fun solveLevel2(): Any {
        val initialState = input.lines.subList(0, 3) + listOf("  #D#C#B#A#", "  #D#B#A#C#") + input.lines.subList(3, 5)
        return solve(State.parse(initialState))
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

fun ValueGraph<Node, Int>.findShortestUnoccupiedPath(from: Int, to: Int, occupied: IntArray): IntArray? {
    reusableQueue.clear()
    reusableVisited.clear()
    reusableParents.forEachIndexed { index, _ -> reusableParents[index] = null }
    reusableQueue += Dist(0, from, null)

    fun path(d: Dist): IntArray {
        val route = mutableListOf<Int>()
        var cur: Dist? = d
        while (cur != null) {
            route += cur.node
            cur = cur.parent
        }
        return route.toIntArray()
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
