package y21

import common.Day

fun main() = Day2(2)

object Day2 : Day(2021, 2) {
    init {
        useSampleInput {
            """
                forward 5
                down 5
                forward 8
                up 3
                down 8
                forward 2
            """.trimIndent()
        }
    }

    override fun level1(): String {
        var horizontal = 0L
        var depth = 0L
        lines.forEach { command ->
            val amount = command.substringAfter(" ").toInt()
            when {
                command.startsWith("forward") -> {
                    horizontal += amount
                }
                command.startsWith("down") -> {
                    depth += amount
                }
                command.startsWith("up") -> {
                    depth -= amount
                }
            }
        }
        return "${depth * horizontal}"
    }

    override fun level2(): String {
        var aim = 0L
        var horizontal = 0L
        var depth = 0L
        lines.forEach { command ->
            val amount = command.substringAfter(" ").toInt()
            when {
                command.startsWith("forward") -> {
                    horizontal += amount
                    depth += aim * amount
                }
                command.startsWith("down") -> {
                    aim += amount
                }
                command.startsWith("up") -> {
                    aim -= amount
                }
            }
        }
        return "${depth * horizontal}"
    }
}
