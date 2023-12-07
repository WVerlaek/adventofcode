package y23

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.Comparator
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2023, day = 7, dryRun = false) { Day7(it) }

class Day7(val input: Input) : Puzzle {

    data class Card(val c: Char) {
        fun value(withJoker: Boolean) = when (c) {
            'A' -> 'E'
            'K' -> 'D'
            'Q' -> 'C'
            'J' -> if (withJoker) '1' else 'B'
            'T' -> 'A'
            else -> c
        }

        companion object {
            val Joker = Card('J')
        }
    }

    enum class Type { HighCard, OnePair, TwoPair, ThreeKind, FullHouse, FourKind, FiveKind }

    data class Hand(val cards: List<Card>, val withJoker: Boolean) {
        init {
            require(cards.size == 5)
        }

        val str = cards.joinToString("") { it.value(withJoker).toString() }

        fun handType(): Type {
            val counts = cards.groupBy { it.value(withJoker) }.toMutableMap()
            val jokerCount = counts[Card.Joker.value(withJoker)]?.size ?: 0
            if (withJoker) {
                counts -= Card.Joker.value(withJoker)
            }

            val sets = IntArray(6)
            counts.forEach { (_, cards) -> sets[cards.size]++ }

            if (!withJoker || jokerCount == 0) {
                return when {
                    sets[5] == 1 -> Type.FiveKind
                    sets[4] == 1 -> Type.FourKind
                    sets[3] == 1 && sets[2] == 1 -> Type.FullHouse
                    sets[3] == 1 -> Type.ThreeKind
                    sets[2] == 2 -> Type.TwoPair
                    sets[2] == 1 -> Type.OnePair
                    else -> Type.HighCard
                }
            }

            return when {
                jokerCount == 5 || sets[5 - jokerCount] >= 1 -> Type.FiveKind
                sets[4 - jokerCount] >= 1 -> Type.FourKind
                // jokerCount is 1 or 2. Can't make full house with 2, you'd be able to get at least a FourKind or higher instead.
                jokerCount == 1 && sets[2] == 2 -> Type.FullHouse
                jokerCount == 2 -> Type.ThreeKind
                // jokerCount is 1
                sets[2] == 1 -> Type.ThreeKind
                // 1 joker, 4 unique cards
                else -> Type.OnePair
            }
        }

        companion object {
            val Comparator : Comparator<Hand> = compareBy({ it.handType() }, { it.str } )
        }
    }

    data class Player(val hand: Hand, val bid: Int)

    private fun parseInput(lines: List<String>, withJoker: Boolean): List<Player> {
        return lines.map { line ->
            val (cards, bid) = line.split(" ")
            Player(
                Hand(cards.map(::Card), withJoker),
                bid.toInt(),
            )
        }
    }

    private fun totalWinnings(players: List<Player>): Int {
        return players.sortedWith(compareBy(Hand.Comparator) { it.hand })
            .mapIndexed { index, player ->
                (index + 1) * player.bid
            }
            .sum()
    }

    override fun solveLevel1(): Any {
        val players = parseInput(input.lines, withJoker = false)
        return totalWinnings(players)
    }

    override fun solveLevel2(): Any {
        val players = parseInput(input.lines, withJoker = true)
        return totalWinnings(players)
    }
}
