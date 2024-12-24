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


fun main() = solvePuzzle(year = 2024, day = 22, dryRun = true) { Day22(it) }

// 2^24=16777216
class Day22(val input: Input) : Puzzle {

    fun evolve(secret: Long): Long {
        var s = secret
        s = (s shl 6) xor s
        s = s and 16777215 // 2^24 - 1

        s = (s shr 5) xor s
        s = s and 16777215 // 2^24 - 1

        s = (s shl 11) xor s
        s = s and 16777215 // 2^24 - 1

        return s
    }

    fun sequence(secret: Long, n: Int): List<Long> {
        var s = secret
        return (0..n).map {
            s.also { s = evolve(s) }
        }
    }

    override fun solveLevel1(): Any {
        return input.lines.sumOf { sequence(it.toLong(), 2000).last() }
    }

    data class Sequence(
        val priceDiffs: List<Long>,
    )

    private fun computeBananas(prices: List<Long>, diffs: List<Long>): Map<Sequence, Long> {
        val result = mutableMapOf<Sequence, Long>()
        for (i in 0 until diffs.size - 3) {
            val sequence = Sequence(diffs.subList(i, i + 4))
            val bananas = prices[i + 4]
            if (sequence !in result) {
                result[sequence] = bananas
            }
        }

        return result
    }

    override fun solveLevel2(): Any {
        val sequences = input.lines.map { sequence(it.toLong(), 2000) }
        val sequencePrices = sequences.map { sequence ->
            sequence.map { l -> l % 10 }
        }
        val diffs = sequencePrices.map { prices ->
            prices.mapIndexedNotNull { index, price ->
                if (index == 0) {
                    null
                } else {
                    price - prices[index - 1]
                }
            }
        }

        val bananas = List(sequences.size) { i ->
            computeBananas(sequencePrices[i], diffs[i])
        }

        val allSequences = bananas.flatMap { it.keys }.distinct()
        return allSequences.maxOf { seq ->
            bananas.sumOf { it[seq] ?: 0 }
        }
    }
}
