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


fun main() = solvePuzzle(year = 2022, day = 15) { Day15(it) }

class Day15(
    val input: Input,
    val level1Y: Int = 2000000,
    val level2Range: Int = 4000000,
) : Puzzle {
    data class Sensor(
        val p: Point,
        val closestBeacon: Point,
    ) {
        val distToBeacon = p.manhattanDistTo(closestBeacon)
    }

    data class ExclusionZone(
        val sensors: List<Sensor>,
        val beacons: List<Point>,
    ) {
        val largestDist = sensors.maxOf { it.distToBeacon }
        val minX = sensors.minOf { it.p.x }
        val maxX = sensors.maxOf { it.p.x }
    }

    fun parse(lines: List<String>): ExclusionZone {
        val sensors = mutableListOf<Sensor>()
        val beacons = mutableSetOf<Point>()
        lines.forEach { line ->
            fun String.toPoint(): Point {
                val x = substring(2, indexOf(", "))
                val y = substringAfter("y=")
                return Point(x.toInt(), y.toInt())
            }
            val sensor = line.substring(10, line.indexOf(": ")).toPoint()
            val beacon = line.substringAfter(" is at ").toPoint()

            sensors += Sensor(sensor, beacon)
            beacons += beacon
        }

        return ExclusionZone(sensors, beacons.toList())
    }

    override fun solveLevel1(): Any {
        val zone = parse(input.lines)

        val xRange =(zone.minX - zone.largestDist)..(zone.maxX + zone.largestDist)
        val sensorPoints = zone.sensors.map { it.p }.toSet()
        return xRange.count { x ->
            val p = Point(x, level1Y)
            if (p in zone.beacons || p in sensorPoints) {
                return@count false
            }

            zone.sensors.any { sensor ->
                p.manhattanDistTo(sensor.p) <= sensor.distToBeacon
            }
        }
    }

    fun Point.frequency() = x * 4000000L + y

    override fun solveLevel2(): Any {
        val zone = parse(input.lines)
        val sensorPoints = zone.sensors.map { it.p to it }.toMap()
        for (y in 0 until level2Range) {
            var x = 0
            while (x < level2Range) {
                val p = Point(x, y)
                if (p in zone.beacons) {
                    x++
                    continue
                }
                if (p in sensorPoints) {
                    val sensor = sensorPoints.getValue(p)
                    x += sensor.distToBeacon
                    continue
                }

                val sensor = zone.sensors.find { sensor ->
                    p.manhattanDistTo(sensor.p) <= sensor.distToBeacon
                } ?: return p.frequency()

                // Advance x to end of sensor area.
                x = sensor.p.x + sensor.distToBeacon - abs(p.y - sensor.p.y) + 1
            }
        }
        
        return -1
    }
}
