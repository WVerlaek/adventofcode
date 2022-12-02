package y22

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(year = 2022, day = 1, level = 2) { Day1(it) }

class Day1(val input: Input) : Puzzle {
    override fun solveLevel1(): Any {
        return getTopCalories(parse(input.lines), 1)
            .sumOf { it.totalCalories }
    }

    override fun solveLevel2(): Any {
        return getTopCalories(parse(input.lines), 3)
            .sumOf { it.totalCalories }
    }

    data class Elf(val food: List<Int>) {
        val totalCalories = food.sum()
    }

    fun parse(lines: List<String>): List<Elf> {
        val elves = mutableListOf<Elf>()
        val food = mutableListOf<Int>()
        lines.forEach { line ->
            if (line.isEmpty()) {
                elves += Elf(food.toList())
                food.clear()
            } else {
                food += line.toInt()
            }
        }
        if (food.isNotEmpty()) {
            elves += Elf(food.toList())
        }
        return elves
    }

    fun getTopCalories(elves: List<Elf>, n: Int): List<Elf> {
        return elves.sortedByDescending { elf -> elf.totalCalories }
            .take(n)
    }
}
