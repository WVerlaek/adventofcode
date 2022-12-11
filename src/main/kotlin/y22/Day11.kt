package y22

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import common.ext.intersectAll
import common.ext.removeLast
import common.ext.product
import common.datastructures.Dir
import common.datastructures.Grid
import common.datastructures.directions
import common.datastructures.Point
import common.datastructures.toPoint
import common.datastructures.toGrid
import common.util.lcm
import java.util.*
import kotlin.math.sign

fun main() = solvePuzzle(year = 2022, day = 11) { Day11(it) }

data class Item(var worryLevel: Long)

data class Monkey(
    val items: MutableList<Item>,
    val operation: (old: Long) -> Long,
    val divisibleBy: Int,
    val throwTo: (Item) -> Int,
) {
    var inspected = 0L
}

data class Round(
    val monkeys: List<Monkey>,
    var decreaseWorryLevelBy: Int = 3,
) {
    val commonMultiple = monkeys.map { it.divisibleBy }.lcm()

    fun next() {
        monkeys.forEach { m ->
            m.items.forEach { item ->
                m.inspected++
                item.worryLevel = m.operation(item.worryLevel) % commonMultiple
                item.worryLevel = item.worryLevel / decreaseWorryLevelBy

                val throwTo = m.throwTo(item)
                monkeys[throwTo].items.add(item)
            }
            m.items.clear()
        }
    }
}

class Day11(val input: Input) : Puzzle {
    fun parseMonkeys(lines: List<String>): List<Monkey> {
        val monkeys = mutableListOf<Monkey>()
        var i = 0
        while (i < lines.size) {
            i++ // Skip first line of monkey.

            val startingItems = lines[i].substringAfter("Starting items: ")
            val items = startingItems.split(", ").map { Item(it.toLong()) }.toMutableList()
            i++

            val o = lines[i].substringAfter("new = ")
            val (left, op, right) = o.split(" ")
            val operation = { old: Long ->
                val l = if (left == "old") old else left.toLong()
                val r = if (right == "old") old else right.toLong()
                when (op) {
                    "*" -> l * r
                    "+" -> l + r
                    else -> throw IllegalArgumentException("Invalid operator: $op")
                }
            }
            i++

            val divisibleBy = lines[i].substringAfter("divisible by ").toLong()
            i++
            val ifTrue = lines[i].substringAfter("throw to monkey ").toInt()
            i++
            val ifFalse = lines[i].substringAfter("throw to monkey ").toInt()
            i++
            val throwTo = { item: Item ->
                if (item.worryLevel % divisibleBy == 0L) {
                    ifTrue
                } else {
                    ifFalse
                }
            }

            monkeys += Monkey(items, operation, divisibleBy.toInt(), throwTo)
            i++
        }

        return monkeys
    }

    fun Round.monkeyBusiness() = this.monkeys
        .map { m -> m.inspected }
        .sorted()
        .takeLast(2)
        .product()

    override fun solveLevel1(): Any {
        val round = Round(parseMonkeys(input.lines))

        repeat(20) {
            round.next()
        }

        return round.monkeyBusiness()
    }

    override fun solveLevel2(): Any {
        val round = Round(parseMonkeys(input.lines), 1)

        repeat(10000) {
            round.next()
        }

        return round.monkeyBusiness()
    }
}
