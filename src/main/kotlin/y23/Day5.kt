package y23

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle


fun main() = solvePuzzle(year = 2023, day = 5) { Day5(it) }

class Day5(val input: Input) : Puzzle {

    data class Range(val dst: Long, val src: Long, val len: Long)

    class RangeMap(ranges: List<Range>) {
        private val sortedSrc = ranges.sortedBy { it.src }
        private val sortedDst = ranges.sortedBy { it.dst }

        private fun findRange(ranges: List<Range>, e: Long, src: Boolean): Range? {
            val index = ranges.binarySearch { range ->
                val start = if (src) range.src else range.dst
                when {
                    e < start -> 1
                    e >= start + range.len -> -1
                    else -> 0
                }
            }

            return ranges.getOrNull(index)
        }

        fun mapSrc(e: Long): Long {
            val range = findRange(sortedSrc, e, src = true)
                ?: return e

            val iInRange = e - range.src
            return range.dst + iInRange
        }

        fun mapDst(e: Long): Long? {
            val range = findRange(sortedDst, e, src = false)
            if (range == null) {
                // Only maps if there is no src range covering this number.
                val hasSrcRange = findRange(sortedSrc, e, src = true) != null
                if (hasSrcRange) {
                    return null
                }

                // No src range, so there's mapping e->e
                return e
            }

            val iInRange = e - range.dst
            return range.src + iInRange
        }
    }

    class Almanac(private val maps: List<RangeMap>) {
        fun mapSrc(e: Long) = maps.fold(e) { next, map -> map.mapSrc(next) }
        fun mapDst(e: Long): Long? {
            var acc = e
            maps.asReversed().forEach { map ->
                acc = map.mapDst(acc) ?: return null
            }
            return acc
        }
    }

    data class In(val seeds: List<Long>, val almanac: Almanac)

    private fun parse(lines: List<String>): In {
        val seeds = lines[0]
            .substringAfter(": ")
            .split(" ")
            .map { it.toLong() }

        val maps = mutableListOf<RangeMap>()
        var curRanges = mutableListOf<Range>()
        lines.subList(2, lines.size).forEach { line ->
            if (line.isEmpty()) {
                maps += RangeMap(curRanges)
                curRanges = mutableListOf()
                return@forEach
            }

            if (!line[0].isDigit()) {
                // Map name, ignore.
                return@forEach
            }

            val (dst, src, len) = line.split(" ")
                .map { it.toLong() }
            curRanges += Range(dst, src, len)
        }

        maps += RangeMap(curRanges)

        return In(seeds, Almanac(maps))
    }

    override fun solveLevel1(): Any {
        val (seeds, almanac) = parse(input.lines)

        return seeds.minOf { seed ->
            almanac.mapSrc(seed)
        }
    }

    data class SeedRange(val start: Long, val len: Long)

    override fun solveLevel2(): Any {
        val (seeds, almanac) = parse(input.lines)
        val seedRanges = seeds.chunked(2)
            .map { (start, len) -> SeedRange(start, len) }
            .sortedBy { it.start }

        var i = 0L
        while (true) {
            val seed = almanac.mapDst(i)
            if (seed != null) {
                // Potential seed, check if it's in the src range of seeds.
                if (seedRanges.binarySearch { range ->
                    when {
                        seed < range.start -> 1
                        seed >= range.start + range.len -> -1
                        else -> 0
                    }
                } >= 0) {
                    // seed is in input range, found smallest i!
                    return i
                }
            }

            i++
        }
    }
}
