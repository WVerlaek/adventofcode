package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2024, day = 8) { Day8(it) }

class Day8(val input: Input) : Puzzle {

    data class Antenna(val c: Char, val p: Point)

    data class CityMap(
        val antennas: Map<Char, List<Antenna>>,
        val rows: Int,
        val cols: Int,
    )

    private fun parseInput(lines: List<String>): CityMap {
        val antennas = mutableListOf<Antenna>()
        input.lines.forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                if (c != '.') {
                    antennas += Antenna(c, Point(col, row))
                }
            }
        }

        val grouped = antennas.groupBy { it.c }
        return CityMap(grouped, lines.size, lines[0].length)
    }

    fun antiNodes(a1: Point, a2: Point, rows: Int, cols: Int, onlyOne: Boolean): List<Point> {
        val delta = a2 - a1
        fun computeForPoint(p: Point, delta: Point): List<Point> {
            val result = mutableListOf<Point>()
            var i = if (onlyOne) 1 else 0
            while (true) {
                val node = p + (delta * i)
                if (!node.inBounds(rows, cols)) {
                    return result
                }

                result += node
                i++

                if (onlyOne) {
                    return result
                }
            }
        }

        return computeForPoint(a1, delta * -1) + computeForPoint(a2, delta)
    }

    fun allAntiNodes(map: CityMap, onlyOne: Boolean): Set<Point> {
        val antiNodes = mutableSetOf<Point>()
        map.antennas.values.forEach { group ->
            for (i in group.indices) {
                for (j in i + 1 until group.size) {
                    antiNodes += antiNodes(group[i].p, group[j].p, map.rows, map.cols, onlyOne)
                }
            }
        }
        return antiNodes
    }

    override fun solveLevel1(): Any {
        val map = parseInput(input.lines)
        return allAntiNodes(map, onlyOne = true).size
    }

    override fun solveLevel2(): Any {
        val map = parseInput(input.lines)
        return allAntiNodes(map, onlyOne = false).size
    }
}
