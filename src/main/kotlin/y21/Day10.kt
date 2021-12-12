package y21

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 10, 2) { Day10(it) }

class Day10(val input: Input) : Puzzle {
    enum class Chunk(val open: Char, val close: Char) {
        A('(', ')'),
        B('[', ']'),
        C('{', '}'),
        D('<', '>');

        companion object {
            fun openChunk(char: Char): Chunk? = values().find { it.open == char }
            fun closeChunk(char: Char): Chunk? = values().find { it.close == char }
        }

        val pointsP1
            get() = when (this) {
                A -> 3
                B -> 57
                C -> 1197
                D -> 25137
            }
        val pointsP2
            get() = when (this) {
                A -> 1
                B -> 2
                C -> 3
                D -> 4
            }
    }

    override fun solveLevel1(): Any {
        return input.lines.sumOf { line ->
            parseLine(line.toCharArray().toList()).illegal?.pointsP1 ?: 0
        }
    }

    class ParseResult(val illegal: Chunk?, val remainingStack: ArrayDeque<Chunk>)

    fun parseLine(chars: List<Char>): ParseResult {
        val stack = ArrayDeque<Chunk>()
        chars.forEach { char ->
            Chunk.openChunk(char)?.let { chunk ->
                stack += chunk
                return@forEach
            }

            Chunk.closeChunk(char)?.let { chunk ->
                val onStack = stack.removeLastOrNull()
                    // If stack is empty, this is the first illegal char.
                    ?: return ParseResult(chunk, stack)
                if (onStack != chunk) {
                    return ParseResult(chunk, stack)
                }
            }
        }
        return ParseResult(null, stack)
    }

    override fun solveLevel2(): Any {
        return input.lines.map { it.toCharArray().toList() }
            .map { parseLine(it) }
            .filter { it.illegal == null }
            .map { it.remainingStack.foldRight(0L) { chunk, acc -> acc * 5L + chunk.pointsP2 } }
            .sorted()
            .let { it[it.size / 2] }
    }
}
