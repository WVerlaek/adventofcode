package y22

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2022, day = 18) { Day18(it) }

class Day18(val input: Input) : Puzzle {

    data class Surface(val origin: Point3, val axis: Axis) {
        var dAxis = 1

        fun adjacent(allSurfaces: Set<Surface>): List<Surface> {
            val result = mutableListOf<Surface>()

            // for (side in 0..3) {
            //     for (i in 0..2) {
            //         // axis
            //         Surface(origin, )
            //     }
            // }

            // 4 surfaces. Extensions of surface in both directions.
            // Axis.values().forEach { a ->
            //     if (a == axis) return@forEach
            //     result += listOf(
            //         Surface(origin.add(a, -1), axis),
            //         Surface(origin.add(a, 1), axis),
            //     )
            // }

            // Axis.values().forEach { a ->
            //     if (a == axis) return@forEach
            //     result += listOf(
            //         Surface(origin, a),
            //         Surface(origin.add(a, 1), a),
            //         Surface(origin.add(axis, -1), a),
            //         Surface(origin.add(axis, -1).add(a, 1), a),
            //     )
            // }

            return result
        }
    }

    fun countSurfaces(cubes: List<Point3>): Int {
        val surfaceCount = HashMap<Surface, Int>()

        cubes.forEach { cube ->
            Axis.values().forEach { axis ->
                surfaceCount.addOrSet(Surface(cube, axis), 1)
                surfaceCount.addOrSet(Surface(cube.add(axis, 1), axis), 1)
            }
        }

        return surfaceCount.count { (s, count) -> count == 1 }
    }

    override fun solveLevel1(): Any {
        val cubes = input.lines.map(Point3::parse)
        return countSurfaces(cubes)
    }

    fun adjacentCubes(c: Point3): List<Point3> {
        return Axis.values().flatMap { a ->
            listOf(
                c.add(a, -1),
                c.add(a, 1),
            )
        }
    }

    override fun solveLevel2(): Any {
        val cubes = input.lines.map(Point3::parse)

        val visited = HashSet<Point3>()
        val containedCubes = HashSet<Point3>()
        val xRange = cubes.minOf { c -> c.x }..cubes.maxOf { c -> c.x }
        val yRange = cubes.minOf { c -> c.y }..cubes.maxOf { c -> c.y }
        val zRange = cubes.minOf { c -> c.z }..cubes.maxOf { c -> c.z }

        fun floodFill(c: Point3, allCubes: Set<Point3>): Set<Point3> {
            val queue = mutableListOf<Point3>()
            queue += c
            val toFill = mutableSetOf<Point3>()

            var isEnclosed = true

            var i = 0
            while (i < queue.size) {
                val next = queue[i]

                if (next in allCubes) {
                    i++
                    continue
                }

                if (next in visited) {
                    i++
                    continue
                }
                visited += next

                if (next.x !in xRange || next.y !in yRange || next.z !in zRange) {
                    // Flood fill is external, not enclosed.
                    isEnclosed = false
                    i++
                    continue
                }

                toFill += next

                adjacentCubes(next).forEach { adj ->
                    queue += adj
                }
                i++
            }

            if (isEnclosed) {
                return toFill
            }
            return emptySet()
        }

        val cubeSet = cubes.toSet()
        cubes.forEach { c ->
            adjacentCubes(c).forEach { adj ->
                containedCubes += floodFill(adj, cubeSet)
            }
        }

        return countSurfaces(cubes + containedCubes)

        // val cubes = input.lines.map(Point3::parse)
        // val surfaceCount = HashMap<Surface, Int>()

        // cubes.forEach { cube ->
        //     Axis.values().forEach { axis ->
        //         surfaceCount.addOrSet(Surface(cube, axis).also { it.dAxis = -1 }, 1)
        //         surfaceCount.addOrSet(Surface(cube.add(axis, 1), axis), 1)
        //     }
        // }

        // val edges = surfaceCount.filter { (k, v) -> v == 1 }.keys

        // val disjointSets = DisjointSets<Surface>()
        // edges.forEach { edge ->
        //     edge.adjacent(surfaceCount.keys).forEach { adj ->
        //         if (adj in edges) {
        //             disjointSets.union(edge, adj)
        //         }
        //     }
        // }

        // val groups = edges.groupBy { edge -> disjointSets.find(edge) }
        // println(groups)
        // return groups.maxOf { (_, group) -> group.size }
    }
}
