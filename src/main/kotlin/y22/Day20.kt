package y22

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2022, day = 20, dryRun = false) { Day20(it) }

class Day20(val input: Input) : Puzzle {

    data class Node(val i: Long) {
        lateinit var prev: Node
        lateinit var next: Node
    }

    class CircularList(
        val nodes: List<Node>,
        var start: Node,
    ) {
        fun moveForward(node: Node) {
            if (node == start) {
                start = node.next
            }

            val (prev, next, nextNext) = listOf(node.prev, node.next, node.next.next)
            prev.next = next
            nextNext.prev = node
            next.next = node
            next.prev = prev
            node.prev = next
            node.next = nextNext
        }

        fun moveBackward(node: Node) {
            if (node == start) {
                start = node.prev
            }

            val (prevPrev, prev, next) = listOf(node.prev.prev, node.prev, node.next)
            prevPrev.next = node
            prev.prev = node
            prev.next = next
            node.next = prev
            node.prev = prevPrev
            next.prev = prev
        }

        fun mix(node: Node) {
            var toMove = node.i % (nodes.size - 1)
            while (toMove != 0L) {
                if (toMove > 0) {
                    moveForward(node)
                    toMove--
                } else {
                    moveBackward(node)
                    toMove++
                }
            }
        }

        fun coordinates(): List<Long> {
            var cur = start
            while (cur.i != 0L) {
                cur = cur.next
            }

            val coords = mutableListOf<Long>()
            for (i in 1..3000) {
                cur = cur.next
                if (i % 1000 == 0) {
                    coords += cur.i
                }
            }

            return coords
        }
    }

    fun circularListOf(numbers: List<Long>): CircularList {
        val nodes = numbers.map(::Node)
        nodes.forEachIndexed { i, node ->
            node.next = nodes[(i + 1) % nodes.size]
            node.prev = nodes[(i - 1 + nodes.size) % nodes.size]
        }

        return CircularList(nodes, nodes.first())
    }

    override fun solveLevel1(): Any {
        val numbers = input.lines.map { it.toLong() }
        val list = circularListOf(numbers)

        val toMix = list.nodes
        toMix.forEach { n -> list.mix(n) }

        return list.coordinates().sum()
    }

    override fun solveLevel2(): Any {
        val key = 811589153L
        val numbers = input.lines.map { it.toLong() * key }
        val list = circularListOf(numbers)

        val toMix = mutableListOf<Node>()
        repeat(10) { toMix += list.nodes }
        toMix.forEach { n -> list.mix(n) }

        return list.coordinates().sum()
    }
}
