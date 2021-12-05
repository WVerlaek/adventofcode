package y21

import common.Day

fun main() = Day4(2)

object Day4 : Day(2021, 4) {
    init {
//        useSampleInput {
//            """
//                7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1
//
//                22 13 17 11  0
//                 8  2 23  4 24
//                21  9 14 16  7
//                 6 10  3 18  5
//                 1 12 20 15 19
//
//                 3 15  0  2 22
//                 9 18 13 17  5
//                19  8  7 25 23
//                20 11 10 24  4
//                14 21 16 12  6
//
//                14 21 17 24  4
//                10 16 15  9 19
//                18  8 23 26 20
//                22 11 13  6  5
//                 2  0 12  3  7
//            """.trimIndent()
//        }
    }
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

    val draw = lines[0].split(",").map { it.toInt() }

    val nBoards = lines.size / 6
    val boards = (0 until nBoards).map { i ->
        val rows = lines.subList(2 + i*6, 7 + i*6)
        Board(rows.map { row -> row.split(" ").filter { it != "" }.map { Cell(it.toInt()) } })
    }

    override fun level1(): String {
        draw.forEach { number ->
            boards.forEach { board ->
                board.markNumber(number)
                if (board.hasBingo()) {
                    return "${board.unmarkedSum() * number}"
                }
            }
        }
        return ""
    }

    override fun level2(): String {
        val finishedBoards = HashSet<Board>()
        draw.forEach { number ->
            boards.forEach nested@ { board ->
                if (board in finishedBoards) return@nested

                board.markNumber(number)
                if (board.hasBingo()) {
                    finishedBoards += board

                    if (finishedBoards.size == nBoards) {
                        // Was the last one to finish.
                        return "${board.unmarkedSum() * number}"
                    }
                }
            }
        }
        return ""
    }
}
