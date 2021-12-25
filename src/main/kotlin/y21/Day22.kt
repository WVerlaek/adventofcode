package y21

import common.datastructures.Grid
import common.ext.intersect
import common.ext.size
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 22, 2) { Day22(it) }

data class Region(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {
    fun size(): Long = xRange.size.toLong() * yRange.size.toLong() * zRange.size.toLong()
    fun intersect(other: Region): Region {
        return Region(xRange.intersect(other.xRange), yRange.intersect(other.yRange), zRange.intersect(other.zRange))
    }

    fun removeAndSplit(toRemove: Region): List<Region> {
        val intersect = intersect(toRemove)
        if (intersect.size() == 0L) {
            // No overlap.
            return listOf(this)
        }
        if (intersect == this) {
            // toRemove fully contains this region.
            return emptyList()
        }

        // Naively split intersected region into subregions. Could combine some to end up with less regions
        // Split 3D cube into at most (3x3x3=) 27 sub-regions, max 3 splits per dimension.
        // Remove all empty subregions.
        // These are non-overlapping regions that fully contain [this] region.
        // One of these sub-regions is the intersection with [toRemove], which we will remove
        // later.
        fun splitRange(range: IntRange, intersect: IntRange): List<IntRange> {
            // Consider sortedX = (R, I) where R=range, I=intersect:
            // [--------]
            //    [--]
            // Returns:
            // [--)
            //    [--]
            //       (--]
            return listOf(
                range.first until intersect.first,
                intersect,
                (intersect.last + 1)..range.last
            ).filter { !it.isEmpty() }
        }

        return splitRange(xRange, intersect.xRange).flatMap { xr ->
            splitRange(yRange, intersect.yRange).flatMap { yr ->
                splitRange(zRange, intersect.zRange).map { zr ->
                    Region(xr, yr, zr)
                }
            }
        } - intersect // Finally, remove the intersection subregion.
    }

    companion object {
        fun parse(s: String): Region {
            val (xRange, yRange, zRange) = s.split(",")
            fun String.parseRange() = substring(2)
                .split("..")
                .let { (from, to) -> from.toInt()..to.toInt() }
            return Region(xRange.parseRange(), yRange.parseRange(), zRange.parseRange())
        }
    }
}

data class RebootStep(val turnOn: Boolean, val region: Region) {
    companion object {
        fun parse(line: String): RebootStep {
            val (onStr, ranges) = line.split(" ")
            return RebootStep(onStr == "on", Region.parse(ranges))
        }
    }
}

class Day22(val input: Input) : Puzzle {
    private val rebootSteps = input.lines.map { RebootStep.parse(it) }

    override fun solveLevel1(): Any {
        val level1Range = -50..50
        val cubes = Array(101) { Grid(101, 101) { _, _ -> false } }
        rebootSteps.forEach { step ->
            for (x in step.region.xRange.intersect(level1Range)) {
                for (y in step.region.yRange.intersect(level1Range)) {
                    for (z in step.region.zRange.intersect(level1Range)) {
                        cubes[x + 50][y + 50][z + 50].value = step.turnOn
                    }
                }
            }
        }
        return cubes.sumOf { it.cells().count { cell -> cell.value } }
    }

    override fun solveLevel2(): Any {
        fun Region.turnOff(region: Region): List<Region> {
            return removeAndSplit(region)
        }

        fun turnOn(region: Region, onRegions: List<Region>): List<Region> {
            return onRegions.flatMap { it.removeAndSplit(region) } + region
        }

        var onRegions = emptyList<Region>()
        rebootSteps.forEach { step ->
            onRegions = if (step.turnOn) {
                turnOn(step.region, onRegions)
            } else {
                onRegions.flatMap { it.turnOff(step.region) }
            }
        }

        return onRegions.sumOf { it.size() }
    }
}
