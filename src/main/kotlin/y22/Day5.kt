package y22

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import common.ext.intersectAll
import common.ext.removeLast

fun main() = solvePuzzle(year = 2022, day = 5, level = 2) { Day5(it) }

data class Move(
    val from: Int,
    val to: Int,
    val n: Int,
)

class Day5(val input: Input) : Puzzle {
    fun parseCrates(lines: List<String>): List<MutableList<Char>> {
        val stacks = mutableListOf<MutableList<Char>>()
        lines.reversed().forEach { line ->
            for (i in 0 until (line.length + 1) / 4) {
                if (line[i*4] != '[') continue

                val c = line[i*4+1]

                // Ensure stack i exists.
                while (i >= stacks.size) {
                    stacks.add(mutableListOf())
                }

                stacks[i].add(c)
            }
        }

        return stacks
    }

    fun parseMoves(lines: List<String>): List<Move> {
        return lines.map { line ->
            val split = line.split(" ")
            Move(split[3].toInt(), split[5].toInt(), split[1].toInt())
        }
    }

    fun parseInput(lines: List<String>): Pair<List<MutableList<Char>>, List<Move>> {
        val crates = lines.filter { "[" in it }
        val moves = lines.filter { "move" in it }

        return parseCrates(crates) to parseMoves(moves)
    }

    override fun solveLevel1(): Any {
        val (stacks, moves) = parseInput(input.lines)
        moves.forEach { move ->
            for (i in 0 until move.n) {
                val moved = stacks[move.from - 1].removeLast()
                stacks[move.to - 1].add(moved)
            }
        }

        return stacks.mapNotNull { stack -> stack.lastOrNull() }
            .joinToString("")
    }

    override fun solveLevel2(): Any {
        val (stacks, moves) = parseInput(input.lines)
        moves.forEach { move ->
            val moved = stacks[move.from - 1].removeLast(move.n)
            stacks[move.to - 1].addAll(moved)
        }

        return stacks.mapNotNull { stack -> stack.lastOrNull() }
            .joinToString("")
    }
}
