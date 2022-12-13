package y22

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.toPoint
import common.datastructures.Cell
import common.datastructures.Grid
import common.datastructures.Point
import common.ext.product
import java.util.PriorityQueue


fun main() = solvePuzzle(year = 2022, day = 13) { Day13(it) }

sealed class PacketNode : Comparable<PacketNode> {
    class L(val elems: List<PacketNode>) : PacketNode()
    class I(val i: Int) : PacketNode()

    override operator fun compareTo(other: PacketNode): Int {
        if (this is I && other is I) {
            return i.compareTo(other.i)
        }

        if (this is L && other is L) {
            for (i in 0 until minOf(this.elems.size, other.elems.size)) {
                val cmp = this.elems[i].compareTo(other.elems[i])
                if (cmp != 0) {
                    return cmp
                }
            }
            return this.elems.size.compareTo(other.elems.size)
        }

        fun PacketNode.toL() = if (this is L) this else PacketNode.L(listOf(this))
        return this.toL().compareTo(other.toL())
    }
}

fun parsePacket(line: String, from: Int = 0): Pair<PacketNode, Int> {
    return when (line[from]) {
        '[' -> {
            val elems = mutableListOf<PacketNode>()
            var cur = from + 1
            while (line[cur] != ']') {
                // Parse next node.
                val (node, length) = parsePacket(line, cur)
                elems += node
                cur += length

                if (line[cur] == ',') {
                    cur++
                }
            }
            PacketNode.L(elems) to (cur - from + 1)
        }
        else -> {
            var to = from + 1
            while (to < line.length && line[to].isDigit()) {
                to++
            }
            PacketNode.I(line.substring(from, to).toInt()) to (to - from)
        }
    }
}

fun parsePackets(lines: List<String>): List<Pair<PacketNode, PacketNode>> {
    val n = (lines.size + 1) / 3
    return (0 until n).map { i ->
        parsePacket(lines[i * 3]).first to parsePacket(lines[i * 3 + 1]).first
    }
}

class Day13(val input: Input) : Puzzle {
    override fun solveLevel1(): Any {
        return parsePackets(input.lines)
            .withIndex()
            .sumOf { (i, pair) ->
                val (left, right) = pair
                if (left < right) {
                    i + 1
                } else {
                    0
                }
            }
    }

    override fun solveLevel2(): Any {
        val dividers = listOf(
            parsePacket("[[2]]").first,
            parsePacket("[[6]]").first,
        )

        val allPackets = parsePackets(input.lines).flatMap { it.toList() } + dividers
        val sorted = allPackets.sorted()

        val dividerIndices = dividers.map { d -> sorted.indexOf(d) + 1 }
        return dividerIndices.product()
    }
}
