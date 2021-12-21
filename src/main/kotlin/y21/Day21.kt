package y21

import common.ext.invert
import common.ext.other
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 21, 2) { Day21(it) }

data class Player(
    val id: String,
    var pos: Int,
    var score: Int = 0,
) {
    fun move(n: Int, boardSize: Int) {
        pos = ((pos + n - 1) % boardSize) + 1 // 1-indexed.
        score += pos
    }
}

sealed class Dice {
    abstract val totalRolls: Long
    abstract fun roll(): Int

    class Deterministic(val sides: Int, var nextRoll: Int = 1) : Dice() {
        private var rollCount = 0L

        override val totalRolls: Long
            get() = rollCount

        override fun roll(): Int {
            rollCount++
            val roll = nextRoll
            nextRoll = (nextRoll % sides) + 1 // 1-indexed.
            return roll
        }
    }
}

class Day21(val input: Input) : Puzzle {
    private val players = input.lines.map { line ->
        val (_, id, _, _, pos) = line.split(" ")
        Player(id, pos.toInt())
    }

    override fun solveLevel1(): Any {
        val boardSize = 10
        val winningScore = 1000
        val dice = Dice.Deterministic(sides = 100)

        while (true)  {
            players.forEach { player ->
                val rolls = listOf(dice.roll(), dice.roll(), dice.roll())
                player.move(rolls.sum(), boardSize)
                if (player.score >= winningScore) {
                    return players.other(player).score * dice.totalRolls
                }
            }
        }
    }

    class DiracGame(val boardSize: Int, val winningScore: Int) {
        // Memoize number of winning games based on position and scores of the players.
        private val memo = Array(boardSize + 1) { // Position p1
            Array(boardSize + 1) { // Position p2
                Array(winningScore) { // Score p1
                    Array<Pair<Long, Long>?>(winningScore) { // Score p2
                        null
                    }
                }
            }
        }

        fun play(turn1: Boolean, pos1: Int, pos2: Int, score1: Int = 0, score2: Int = 0): Pair<Long, Long> {
            if (!turn1) {
                // Invert roles, and swap results afterwards.
                return play(true, pos2, pos1, score2, score1).invert()
            }

            when {
                score1 >= winningScore -> return 1L to 0L
                score2 >= winningScore -> return 0L to 1L
            }

            // Return if memoized already.
            memo[pos1][pos2][score1][score2]
                ?.let { return it }

            var totalWins1 = 0L
            var totalWins2 = 0L

            for (d1 in 1..3) {
                for (d2 in 1..3) {
                    for (d3 in 1..3) {
                        // New universe: dice roll (d1, d2, d3).
                        val moves = d1 + d2 + d3
                        val newPos1 = (pos1 + moves - 1) % boardSize + 1
                        val newScore1 = score1 + newPos1
                        val (wins1, wins2) = play(!turn1, newPos1, pos2, newScore1, score2)
                        totalWins1 += wins1
                        totalWins2 += wins2
                    }
                }
            }

            // Memoize result.
            val result = totalWins1 to totalWins2
            memo[pos1][pos2][score1][score2] = result
            return result
        }
    }

    override fun solveLevel2(): Any {
        val game = DiracGame(boardSize = 10, winningScore = 21)
        val (wins1, wins2) = game.play(
            turn1 = true,
            pos1 = players[0].pos,
            pos2 = players[1].pos,
            score1 = 0,
            score2 = 0,
        )

        return maxOf(wins1, wins2)
    }
}
