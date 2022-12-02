package y22

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(year = 2022, day = 2, level = 2) { Day2(it) }

class Day2(val input: Input) : Puzzle {

    enum class Move(val score: Int) {
        Rock(1), Paper(2), Scissors(3);

        fun add(i: Int): Move {
            return Move.values()[(ordinal + i + 3) % 3]
        }
    }

    data class Round(val opp: Move, val you: Move) {
        fun score(): Int {
            val outcome = when {
                opp == you -> 3
                you.score == opp.score + 1 || (you == Move.Rock && opp == Move.Scissors) -> 6
                else -> 0
            }
            return you.score + outcome
        }
    }

    fun parse1(lines: List<String>): List<Round> {
        return lines.map { line ->
            val (opp, you) = line.split(" ")
            Round(when (opp) {
                "A" -> Move.Rock
                "B" -> Move.Paper
                "C" -> Move.Scissors
                else -> throw IllegalArgumentException("Invalid move $opp")
            }, when (you) {
                "X" -> Move.Rock
                "Y" -> Move.Paper
                "Z" -> Move.Scissors
                else -> throw IllegalArgumentException("Invalid move $you")
            })
        }
    }

    fun parse2(lines: List<String>): List<Round> {
        return lines.map { line ->
            val (opp, you) = line.split(" ")
            val oppMove = when (opp) {
                "A" -> Move.Rock
                "B" -> Move.Paper
                "C" -> Move.Scissors
                else -> throw IllegalArgumentException("Invalid move $opp")
            }
            val toAdd = you[0] - 'Y'
            Round(oppMove, oppMove.add(toAdd))
        }
    }

    override fun solveLevel1(): Any {
        return parse1(input.lines).sumOf { it.score() }
    }

    override fun solveLevel2(): Any {
        return parse2(input.lines).sumOf { it.score() }
    }
}
