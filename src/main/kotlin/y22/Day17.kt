package y22

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2022, day = 17) { Day17(it) }

typealias Rock = Grid<Boolean>

class Day17(val input: Input) : Puzzle {

    val defaultRocks: List<Rock> = listOf(
        """
            ####
        """.toGrid(),
        """
            .#.
            ###
            .#.
        """.toGrid(),
        """
            ###
            ..#
            ..#
        """.toGrid(), // Reversed rows, falling "up".
        """
            #
            #
            #
            #
        """.toGrid(),
        """
            ##
            ##
        """.toGrid(),
    )

    enum class Dir(val c: Char, val dx: Int) { Left('<', -1), Right('>', 1) }
    class NotTetris(val rocks: List<Rock>, val directions: List<Dir>) {
        val grid = Grid(2100 * 4, 9) { r, c ->
            r == 0 || c == 0 || c == 8
        }
        var highestRock = 0

        var nextRock = 0
        var nextDir = 0

        fun heightMap(): List<Int> {
            return IntArray(7) { i ->
                val col = i + 1

                var topRow = highestRock
                while (!grid[topRow][col].value) topRow--
                highestRock - topRow
            }.toList()
        }

        fun dropCache(): Pair<CacheKey, CacheValue> {
            val key = CacheKey(nextRock % rocks.size, nextDir % directions.size, heightMap())


            val rock = rocks[nextRock++ % rocks.size]

            // println(Grid(70, 9) { r, c -> grid[r][c].value})
            // println()

            var topLeft = Point(col = 3, row = highestRock + 4)
            while (true) {
                // 1. Move in direction.
                val dir = directions[nextDir++ % directions.size]
                var moved = topLeft + Point(dir.dx, 0)
                if (!collides(rock, moved)) {
                    topLeft = moved
                } // Otherwise, ignore collision.

                // 2. Move up.
                moved = topLeft + Point(0, -1)
                if (collides(rock, moved)) {
                    addToGrid(rock, topLeft)
                    return key to CacheValue(nextRock - 1, highestRock)
                }
                topLeft = moved
            }
        }

        fun drop() {
            dropCache()
        }

        fun collides(rock: Rock, topLeft: Point): Boolean {
            return rock.cells().any { cell ->
                val x = cell.col + topLeft.col
                val y = cell.row + topLeft.row
                cell.value && grid[y][x].value
            }
        }

        fun addToGrid(rock: Rock, topLeft: Point) {
            rock.cells().forEach { cell ->
                if (cell.value) {
                    val x = cell.col + topLeft.col
                    val y = cell.row + topLeft.row
                    grid[y][x].value = true
                }
            }
            highestRock = maxOf(highestRock, topLeft.row + rock.numRows - 1)
        }
    }


    fun parse(directions: String): NotTetris {
        return NotTetris(defaultRocks, directions.map { c -> Dir.values().find { d -> d.c == c }!! })
    }

    override fun solveLevel1(): Any {
        val nt = parse(input.lines.single())
        repeat(2022) {
            nt.drop()
        }

        return nt.highestRock
    }

    data class CacheKey(val rock: Int, val direction: Int, val heightMap: List<Int>) {

    }
    data class CacheValue(val n: Int, val height: Int) {
        var next: CacheKey? = null
    }
    /**
     * TODO:
     * Repeat dropping, until a cache hit occurs.
     * Then:
     *  val cycle = n - cacheHit.n
     *  val cycles = (remainingDrops / cycle)
     *  val height = h + cycles * (h - cacheHit.h)
     *
     *  val remainder = remainingDrops % cycle
     *  Create new grid, with heightMap as base, and drop [remainder] rocks. Add to total height.
     */

    override fun solveLevel2(): Any {
        val nt = parse(input.lines.single())
        val n = 1000000000000L

        val cache = HashMap<CacheKey, CacheValue>()
        var i = 0
        lateinit var cycleFrom: CacheKey
        var prevKey: CacheKey? = null
        while (true) {
            val (key, value) = nt.dropCache()
            if (key in cache) {
                val cached = cache.getValue(key)
                cycleFrom = key
                println("Found cache at $i, cycle from ${cached.n}")
                break
            }

            prevKey?.let { prev ->
                cache.getValue(prev).next = key
            }
            prevKey = key

            cache[key] = value
            i++
        }

        // [0..i] is a cycle.
        val cycle = i - cache.getValue(cycleFrom).n
        val dHeight = nt.highestRock - cache.getValue(cycleFrom).height
        val remainingCycles = (n - i - 1) / cycle

        var totalHeight = nt.highestRock + remainingCycles * dHeight

        // TODO: Simulate remainder of cycle
        val remainderRocks = (n - i - 1) % cycle

        var until = cycleFrom
        repeat(remainderRocks.toInt()) {
            until = cache[until]!!.next!!
        }

        return totalHeight + (cache[until]!!.height - cache[cycleFrom]!!.height)


        return totalHeight
    }
}
