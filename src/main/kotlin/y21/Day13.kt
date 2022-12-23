package y21

import common.datastructures.fitGrid
import common.datastructures.Point
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 13, 2) { Day13(it) }

sealed class Fold(val n: Int) {
    class Up(n: Int) : Fold(n)
    class Left(n: Int) : Fold(n)
}

class Day13(val input: Input) : Puzzle {
    val startFolds = input.lines.indexOfFirst { it.startsWith("fold") }
    val points = input.lines.subList(0, startFolds - 1)
        .map { Point.parse(it) }
    val folds = input.lines.subList(startFolds, input.lines.size)
        .map { line ->
            val n = line.substringAfter("=").toInt()
            if ("x" in line) {
                Fold.Left(n)
            } else {
                Fold.Up(n)
            }
        }

    fun List<Point>.fold(fold: Fold): List<Point> {
        return map { p ->
            when (fold) {
                is Fold.Up -> {
                    p.copy(row = if (p.row > fold.n) 2 * fold.n - p.row else p.row)
                }
                is Fold.Left -> {
                    p.copy(col = if (p.col > fold.n) 2 * fold.n - p.col else p.col)
                }
            }
        }.distinct()
    }

    override fun solveLevel1(): Any {
        return points.fold(folds[0]).size
    }

    override fun solveLevel2(): Any {
        var points = this.points
        folds.forEach { points = points.fold(it) }
        println(points.fitGrid())
        return "EFJKZLBL"
    }
}
