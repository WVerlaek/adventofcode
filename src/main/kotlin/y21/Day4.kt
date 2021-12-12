package y21

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import common.puzzle.splitToInts

fun main() = solvePuzzle(2021, 4, 2) { Day4(it) }

class Day4(val input: Input) : Puzzle {
    class Cell(val v: Int, var marked: Boolean = false)
    class Board(val rows: List<List<Cell>>) {
        fun hasBingo(): Boolean {
            return (0 until 5).any { i ->
                rows[i].all { it.marked }
                    || rows.map { it[i] }.all { it.marked }
            }
        }

        fun markNumber(v: Int) {
            rows.forEach { row ->
                row.forEach { cell ->
                    if (cell.v == v) cell.marked = true
                }
            }
        }

        fun unmarkedSum(): Int {
            return rows.sumOf { row -> row.sumOf { if (it.marked) 0 else it.v } }
        }
    }

    val draw = input.lines[0].splitToInts()

    val nBoards = input.lines.size / 6
    val boards = (0 until nBoards).map { i ->
        val rows = input.lines.subList(2 + i*6, 7 + i*6)
        Board(rows.map { row -> row.split(" ").filter { it != "" }.map { Cell(it.toInt()) } })
    }

    override fun solveLevel1(): Any {
        draw.forEach { number ->
            boards.forEach { board ->
                board.markNumber(number)
                if (board.hasBingo()) {
                    return board.unmarkedSum() * number
                }
            }
        }
        return ""
    }

    override fun solveLevel2(): Any {
        val finishedBoards = HashSet<Board>()
        draw.forEach { number ->
            boards.forEach nested@ { board ->
                if (board in finishedBoards) return@nested

                board.markNumber(number)
                if (board.hasBingo()) {
                    finishedBoards += board

                    if (finishedBoards.size == nBoards) {
                        // Was the last one to finish.
                        return board.unmarkedSum() * number
                    }
                }
            }
        }
        return ""
    }
}
