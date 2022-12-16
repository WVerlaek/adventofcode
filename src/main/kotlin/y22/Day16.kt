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
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2022, day = 16) { Day16(it) }

class Day16(val input: Input) : Puzzle {
    val regex = "Valve (.*) has flow rate=([0-9]*); tunnels? leads? to valves? (.*)".toRegex()

    class Volcano(
        val valves: Map<String, Valve>,
        val start: String,
    ) {
        val nonEmptyValves = valves.filter { (_, v) -> v.flow > 0 }

        data class Path(val valves: List<String>) {
            val dist = valves.size
        }

        val cachedPaths = HashMap<Pair<String, String>, Path>()
        fun path(from: String, to: String): Path {
            cachedPaths[Pair(from, to)]?.let { return it }

            val p = computePath(from, to)
            cachedPaths[Pair(from, to)] = p
            return p
        }

        fun dist(from: String, to: String): Int = path(from, to).dist

        fun computePath(from: String, to: String): Path {
            val visited = mutableSetOf<String>()
            data class Dist(val valve: String, val path: List<String>)

            val queue = ArrayList<Dist>()
            queue += Dist(from, emptyList())
            var i = 0
            while (i < queue.size) {
                val cur = queue[i]
                if (cur.valve in visited) {
                    i++
                    continue
                }
                visited += cur.valve

                if (cur.valve == to) {
                    return Path(cur.path)
                }

                valves.getValue(cur.valve).neighs.forEach { neigh ->
                    queue += Dist(neigh, cur.path + neigh)
                }

                i++
            }

            throw RuntimeException("$to is not reachable from $from")
        }
    }

    data class Valve(
        val id: String,
        val flow: Int,
        val neighs: List<String>,
    )

    fun parse(lines: List<String>): Volcano {
        lateinit var start: String
        val valves = lines.map { line ->
            val match = regex.find(line)!!
            val (_, id, rate, neighs) = match.groupValues
            id to Valve(id, rate.toInt(), neighs.split(", "))
        }
        start = "AA" // valves.first().first

        return Volcano(valves.toMap(), start)
    }

    override fun solveLevel1(): Any {
        val volcano = parse(input.lines)

        fun backtrack(cur: String, minutesLeft: Int, opened: HashSet<String>, total: Int): Int {
            if (minutesLeft <= 0) return total
            val targets = volcano.nonEmptyValves.keys - cur - opened
            data class Target(val valve: String, val movesToOpen: Int, val totalFlow: Int)
            val targetScores = targets.map { t ->
                val d = volcano.dist(cur, t)
                val movesToOpen = d + 1
                val flowMinutes = maxOf(0, minutesLeft - movesToOpen)
                Target(t, movesToOpen, flowMinutes * volcano.valves.getValue(t).flow)
            }


            return targetScores.maxOfOrNull { target ->
                // Try move to target.
                opened += target.valve
                val result = backtrack(target.valve, minutesLeft - target.movesToOpen, opened, total + target.totalFlow)
                opened -= target.valve
                result
            } ?: total
        }

        return backtrack(volcano.start, 30, HashSet(), 0)
    }

    override fun solveLevel2(): Any {
        val volcano = parse(input.lines)

        data class Player(val cur: String, val minutesToTarget: Int)

        fun backtrack(p1: Player, p2: Player, minutesLeft: Int, opened: HashSet<String>, total: Int): Int {
            if (minutesLeft <= 0) return total
            val targets = volcano.nonEmptyValves.keys - /*cur -*/ opened
            data class Target(val valve: String, val movesToOpen: Int, val totalFlow: Int)

            fun targetScores(p: Player): List<Target> {
                return targets.map { t ->
                    val d = volcano.dist(p.cur, t)
                    val movesToOpen = d + 1
                    val flowMinutes = maxOf(0, minutesLeft - movesToOpen)
                    Target(t, movesToOpen, flowMinutes * volcano.valves.getValue(t).flow)
                }
            }

            if (p1.minutesToTarget == 0) {
                // Find a new target.
                return targetScores(p1).maxOfOrNull { target ->
                    opened += target.valve
                    val mins = minOf(target.movesToOpen, p2.minutesToTarget)
                    val r = backtrack(
                        p1.copy(cur = target.valve, minutesToTarget = target.movesToOpen - mins),
                        p2.copy(minutesToTarget = p2.minutesToTarget - mins),
                        minutesLeft - mins, opened, total + target.totalFlow)
                    opened -= target.valve
                    r
                } ?: total
            }

            if (p2.minutesToTarget == 0) {
                // Find a new target.
                return targetScores(p2).maxOfOrNull { target ->
                    opened += target.valve
                    val mins = minOf(target.movesToOpen, p1.minutesToTarget)
                    val r = backtrack(
                        p1.copy(minutesToTarget = p1.minutesToTarget - mins),
                        p2.copy(cur = target.valve, minutesToTarget = target.movesToOpen - mins),
                        minutesLeft - mins, opened, total + target.totalFlow)
                    opened -= target.valve
                    r
                } ?: total
            }

            return total
        }

        return backtrack(Player("AA", 0), Player("AA", 0), 26, HashSet(), 0)
    }
}
