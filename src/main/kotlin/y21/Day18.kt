package y21

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 18, 2) { Day18(it) }

sealed class Element(var parent: Element?) {
    data class Number(var n: Int) : Element(null) {
        override fun toString() = "$n"
    }
    data class Pair(var left: Element, var right: Element) : Element(null) {
        override fun toString() = "[$left,$right]"
    }

    operator fun plus(other: Element): Element {
        require(parent == null)
        require(other.parent == null)
        val added = Pair(this, other)
        parent = added
        other.parent = added
        added.fullyReduce()
        return added
    }

    fun findNested(level: Int): Pair? {
        return when (this) {
            is Number -> null
            is Pair -> {
                if (level == 0 && left is Number && right is Number) {
                    this
                } else {
                    left.findNested(level - 1) ?: right.findNested(level - 1)
                }
            }
        }
    }

    fun reduce(): Boolean {
        val explode = findNested(4)
        if (explode != null) {
            val explodeRight = explode.right as Number
            val explodeLeft = explode.left as Number
            // Go up tree until parent is on the left.
            val leftParent = explode.traverseUpUntilSideOfParent(left = false)?.parent as Pair?
            leftParent?.left?.addNumber(explodeLeft.n, addToLeft = false)
            val rightParent = explode.traverseUpUntilSideOfParent(left = true)?.parent as Pair?
            rightParent?.right?.addNumber(explodeRight.n, addToLeft = true)

            val parent = explode.parent as Pair
            if (parent.left is Pair) {
                // Exploding pair is left of parent.
                parent.left = Number(0).also { it.parent = parent }
            } else {
                // Exploding pair is right of parent.
                parent.right = Number(0).also { it.parent = parent }
            }

            return true
        }

        return splitTen()
    }

    fun traverseUpUntilSideOfParent(left: Boolean): Element? {
        var cur = this
        while (true) {
            val p = cur.parent as Pair?
                ?: return null
            val checkFor = if (left) p.left else p.right
            if (cur === checkFor) {
                return cur
            }
            cur = p
        }
    }

    @Suppress("ControlFlowWithEmptyBody")
    fun fullyReduce() {
        while (reduce()) {}
    }

    fun addNumber(i: Int, addToLeft: Boolean) {
        return when (this) {
            is Number -> n += i
            is Pair -> if (addToLeft) left.addNumber(i, addToLeft) else right.addNumber(i, addToLeft)
        }
    }

    fun splitTen(): Boolean {
        return when (this) {
            is Number -> {
                if (n >= 10) {
                    val left = n / 2
                    val right = (n + 1) / 2
                    val pair = Pair(Number(left), Number(right))
                    pair.left.parent = pair
                    pair.right.parent = pair

                    val par = parent as Pair
                    if (par.left == this) {
                        // Split if left of parent.
                        par.left = pair
                    } else {
                        par.right = pair
                    }
                    pair.parent = par
                    true
                } else {
                    false
                }
            }
            is Pair -> {
                if (left.splitTen()) {
                    true
                } else {
                    right.splitTen()
                }
            }
        }
    }

    fun magnitude(): Long {
        return when (this) {
            is Number -> n.toLong()
            is Pair -> 3L * left.magnitude() + 2L * right.magnitude()
        }
    }

    companion object {
        fun parse(input: String): Element {
            return parseLeft(input, 0).first
        }

        private fun parseLeft(input: String, from: Int): kotlin.Pair<Element, Int> {
            return when (input[from]) {
                '[' -> {
                    val (left, leftLen) = parseLeft(input, from + 1) // Include [
                    val (right, rightLen) = parseLeft(input, from + leftLen + 2) // Include [,
                    val pair = Pair(left, right)
                    left.parent = pair
                    right.parent = pair
                    return pair to leftLen + rightLen + 3 // Include [,]
                }
                else -> {
                    var to = from + 1
                    while (to < input.length && input[to].isDigit()) to++
                    val numStr = input.substring(from, to)
                    Number(numStr.toInt()) to numStr.length
                }
            }
        }
    }
}

class Day18(val input: Input) : Puzzle {
    override fun solveLevel1(): Any {
        val snailfishNumbers = input.lines.map { Element.parse(it) }
        var result = snailfishNumbers[0]
        for (i in 1 until snailfishNumbers.size) {
            result += snailfishNumbers[i]
        }
        return result.magnitude()
    }

    override fun solveLevel2(): Any {
        var maxMagnitude = 0L
        for (i in 0 until input.lines.size) {
            for (j in 0 until input.lines.size) {
                if (i == j) continue

                val numI = Element.parse(input.lines[i])
                val numJ = Element.parse(input.lines[j])
                val mag = (numI + numJ).magnitude()
                maxMagnitude = maxOf(maxMagnitude, mag)
            }
        }
        return maxMagnitude
    }
}
