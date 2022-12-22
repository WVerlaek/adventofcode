package y22

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2022, day = 22) { Day22(it) }

class Day22(val input: Input) : Puzzle {
    sealed class Direction {
        class Forward(val n: Int) : Direction()
        object Left : Direction()
        object Right : Direction()
    }

    enum class Tile(val c: Char) { Nothing(' '), Empty('.'), Wall('#') }

    fun parseMap(lines: List<String>): Grid<Tile> {
        val rows = lines.size
        val cols = lines.maxOf { it.length }
        return Grid(rows, cols) { r, c ->
            lines[r].getOrNull(c)
                ?.let { ch -> Tile.values().find { it.c == ch } }
                ?: Tile.Nothing
        }
    }

    fun parseDirections(line: String): List<Direction> {
        val result = mutableListOf<Direction>()
        var cur = 0
        while (cur < line.length) {
            when (line[cur]) {
                'R' -> {
                    result += Direction.Right
                    cur++
                }
                'L' -> {
                    result += Direction.Left
                    cur++
                }
                else -> {
                    var until = cur + 1
                    while (until < line.length && line[until].isDigit()) {
                        until++
                    }
                    result += Direction.Forward(line.substring(cur, until).toInt())
                    cur = until
                }
            }
        }

        return result
    }

    fun parseInput(lines: List<String>) = parseMap(lines.dropLast(2)) to parseDirections(lines.last())

    data class Pos(val coor: Point, val dir: Int)

    fun Pos.password() = 1000 * (coor.y + 1) + 4 * (coor.x + 1) + (-dir + 6) % 4

    fun move(map: Grid<Tile>, curPos: Pos, direction: Direction, wrapRules: WrapRules): Pos {
        return when (direction) {
            is Direction.Right -> curPos.copy(dir = (curPos.dir + 1) % 4)
            is Direction.Left -> curPos.copy(dir = (curPos.dir + 3) % 4)
            is Direction.Forward -> {
                var cur = curPos
                for (i in 0 until direction.n) {
                    val d = directions[cur.dir]
                    val nextCoor = Point(cur.coor.x + d.dCol, cur.coor.y + d.dRow)
                    val next = wrapRules.wrap(map, cur.copy(coor = nextCoor))

                    try {
                        if (map[next.coor].value == Tile.Wall) {
                            break
                        }
                        if (map[next.coor].value == Tile.Nothing) {
                            error("Should not be reached")
                        }
                    } finally {
                        println("Cur: $cur, next: $next")
                    }
                    cur = next
                }
                cur
            }
        }
    }

    fun interface WrapRules {
        fun wrap(map: Grid<Tile>, p: Pos): Pos
    }

    val wrapLevel1 = WrapRules { map, p ->
        fun Point.wrapMap() = Point((x + map.numCols) % map.numCols, (y + map.numRows) % (map.numRows))
        var next = p.copy(coor = p.coor.wrapMap())
        while (map[next.coor].value == Tile.Nothing) {
            val d = directions[next.dir]
            val nextCoor = Point(next.coor.x + d.dCol, next.coor.y + d.dRow).wrapMap()
            next = next.copy(coor = nextCoor)
        }
        next
    }

    // val wrapLevel2Sample = WrapRules { rows, cols, p ->
    //     p
    // }
    val wrapLevel2 = WrapRules { _, p ->
        val (x, y) = p.coor
        when {
            // 1
            x == 50 && y in 150..199 && p.dir == 2 -> Pos(Point(50 + y - 150, 149), 1)
            x in 50..99 && y == 150 && p.dir == 3 -> Pos(Point(49, 150 + x - 50), 0)
            // 2
            x in 100..149 && y == -1 -> Pos(Point(x - 100, 199), 1)
            x in 0..49 && y == 200 -> Pos(Point(x + 100, 0), 3)
            // 3
            x in 50..99 && y == -1 -> Pos(Point(0, 150 + x - 50), 2)
            x == -1 && y in 150..199 -> Pos(Point(50 + y - 150, 0), 3)
            // 4
            x == 49 && y in 50..99 && p.dir == 0 -> Pos(Point(y - 50, 100), 3)
            x in 0..49 && y == 99 && p.dir == 1 -> Pos(Point(50, 50 + x), 2)
            // 5
            x in 100..149 && y == 50 && p.dir == 3 -> Pos(Point(99, 50 + x - 100), 0)
            x == 100 && y in 50..99 && p.dir == 2 -> Pos(Point(100 + y - 50, 49), 1)
            // 6
            x == 150 && y in 0..49 -> Pos(Point(99, 149 - y), 0)
            x == 100 && y in 100..149 -> Pos(Point(149, 149 - y), 0)
            // 7
            x == 49 && y in 0..49 -> Pos(Point(0, 149 - y), 2)
            x == -1 && y in 100..149 -> Pos(Point(50, 149 - y), 2)

            else -> p
        }
    }


    override fun solveLevel1(): Any {
        val (map, directions) = parseInput(input.lines)
        val start = Pos(Point(map[0].indexOfFirst { it.value == Tile.Empty }, 0), 2)

        var cur = start
        directions.forEach { d ->
            cur = move(map, cur, d, wrapLevel1)
        }

        return cur.password()
    }

    override fun solveLevel2(): Any {
        val (map, directions) = parseInput(input.lines)
        val start = Pos(Point(map[0].indexOfFirst { it.value == Tile.Empty }, 0), 2)

        var cur = start
        directions.forEach { d ->
            cur = move(map, cur, d, wrapLevel2)
        }

        return cur.password()
    }
}
