package y23

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2023, day = 2) { Day2(it) }

class Day2(val input: Input) : Puzzle {

    data class Game(
        val id: Int,
        val sets: List<CubeSet>,
    ) {
        fun isPossible(cubes: Map<String, Int>): Boolean {
            return sets.all { it.isPossible(cubes) }
        }

        fun minCubes(): CubeSet {
            val minSet = mutableMapOf<String, Int>()
            sets.forEach { set ->
                set.cubes.forEach { (cube, n) ->
                    minSet[cube] = minSet[cube]
                        ?.let { m -> maxOf(m, n) }
                        ?: n
                }
            }

            return CubeSet(minSet)
        }
    }

    data class CubeSet(
        val cubes: Map<String, Int>,
    ) {
        fun isPossible(cubes: Map<String, Int>): Boolean {
            return this.cubes.all { (col, count) ->
                (cubes[col] ?: 0) >= count
            }
        }

        fun power() = cubes.values.fold(1) { a, b -> a * b }
    }

    private fun parseGame(line: String): Game {
        val id = line.substring("Game ".length, line.indexOf(':'))
            .toInt()

        val sets = line.substringAfter(": ")
            .split("; ")
            .map { set ->
                val cubes = set.split(", ")
                    .associate { cube ->
                        val (count, col) = cube.split(" ")
                        col to count.toInt()
                    }
                CubeSet(cubes)
            }

        return Game(id, sets)
    }

    override fun solveLevel1(): Any {
        val games = input.lines.map(::parseGame)
        val bag = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14,
        )
        return games.filter { game -> game.isPossible(bag) }
            .sumOf { it.id }
    }

    override fun solveLevel2(): Any {
        val games = input.lines.map(::parseGame)
        return games.map { game -> game.minCubes() }
            .sumOf { set -> set.power() }
    }
}
