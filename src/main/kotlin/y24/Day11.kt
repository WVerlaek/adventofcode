package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.puzzle.splitToLongs
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2024, day = 11, dryRun = false) { Day11(it) }

class Day11(val input: Input) : Puzzle {

    data class Stone(
        val stone: Long,
        val amount: Long,
    )

    private fun blinkOnce(stone: Long): List<Long> {
        if (stone == 0L) {
            return listOf(1L)
        }
        val digits = stone.numDigits()
        if (digits % 2 == 0) {
            // Split
            val str = stone.toString()
            val left = str.substring(0, digits / 2).toLong()
            val right = str.substring(digits / 2).toLong()
            return listOf(left, right)
        }

        return listOf(stone * 2024)
    }

    private fun blinkStones(stones: List<Stone>, n: Int): List<Stone> {
        if (n == 0) return stones

        val newStones = mutableListOf<Stone>()
        stones.forEach { stone ->
            val blinked = blinkOnce(stone.stone)
            blinked.map { Stone(it, stone.amount) }.forEach { newStones += it }
        }

        // Merge newStones
        val grouped = newStones
            .groupBy { it.stone }
            .map { (stone, group) ->
                Stone(stone, group.sumOf { it.amount })
            }

        return blinkStones(grouped, n - 1)
    }

    private val inputStones = input.lines[0]
        .splitToLongs(" ")
        .map { Stone(it, 1L) }

    override fun solveLevel1(): Any {
        return blinkStones(inputStones, 25).sumOf { it.amount }
    }

    override fun solveLevel2(): Any {
        return blinkStones(inputStones, 75).sumOf { it.amount }
    }
}
