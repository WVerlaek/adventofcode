package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.puzzle.splitToInts
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2024, day = 24, dryRun = true) { Day24(it) }

private typealias Operator = (Boolean, Boolean) -> Boolean
private val and: Operator = { a, b -> a && b }
private val or: Operator = { a, b -> a || b }
private val xor: Operator = { a, b -> a xor b }

class Day24(val input: Input, val swaps: Int = 4) : Puzzle {
    private val nodes = mutableMapOf<String, Node>()
    sealed class Node {
        class Wire(val value: Boolean) : Node()
        class Gate(val a: String, val b: String, val operator: Operator) : Node()
    }

    fun Node.value(visited: Set<String>): Boolean? {
        return when (this) {
            is Node.Wire -> value
            is Node.Gate -> {
                if (a in visited || b in visited) {
                    null
                } else {
                    val aValue = nodes.getValue(a).value(visited + listOf(a, b)) ?: return null
                    val bValue = nodes.getValue(b).value(visited + listOf(a, b)) ?: return null
                    operator(aValue, bValue)
                }
            }
        }
    }

    init {
        var isGate = false
        input.lines.forEach { line ->
            if (line.isEmpty()) {
                isGate = true
                return@forEach
            }

            if (isGate) {
                val (a, op, b, _, c) = line.split(" ")
                nodes[c] = Node.Gate(
                    a = a,
                    b = b,
                    operator = when (op) {
                        "AND" -> and
                        "OR" -> or
                        "XOR" -> xor
                        else -> error("unknown operator $op")
                    }
                )
            } else {
                val (a, value) = line.split(" ")
                nodes[a.substringBefore(":")] = Node.Wire(value == "1")
            }
        }
    }

    private fun longValue(nodeKeys: List<String>): Long? {
        val resultValues = nodeKeys.sorted()
        return resultValues.foldRightIndexed(0L) { i, z, acc ->
            val b = nodes.getValue(z).value(mutableSetOf()) ?: return null
            acc + (b.toInt().toLong() shl i)
        }
    }

    override fun solveLevel1(): Any {
        return longValue(nodes.keys.filter { it.startsWith("z") })!!
    }

    // dgr x
    // dtv x
    // fgc x
    // gdr - (AND of 2 xors)
    // gdv - (fine, goes into xor and and)
    // jkb - same as above
    // mtj x
    // vtc: - dgr AND rrd -> vtc (AND before OR, carry out)
    //      wng(AND) OR gtw(AND) -> rrd
    //      y33 AND x33 -> dgr x
    // vvm x
    // wrd - (depends on vtc)
    // z12 x
    // z29 x
    // z37 x

    override fun solveLevel2(): Any {
        val gates = nodes.filter { it.value is Node.Gate }
            .mapValues { it.value as Node.Gate }

        fun isXY(a: String) = a.startsWith('x') || a.startsWith('y')
        fun isZ(a: String) = a.startsWith('z')

        val largestZ = gates.filter { it.key.startsWith('z') }.keys.maxOf { it }

        val wrong = mutableSetOf<String>()
        gates.forEach { (out, gate) ->
            val in1 = gate.a
            val in2 = gate.b
            val usedInGates = gates.entries.filter { (_, v) -> v.a == out || v.b == out }
            when (gate.operator) {
                xor -> {
                    if (!isZ(out)) {
                        if (!isXY(in1) || !isXY(in2)) {
                            wrong += out
                        } else {
                            // Should go to another xor
                            if (usedInGates.none { it.value.operator == xor }) {
                                println("xy xor not going to xor $out")
                                wrong += out
                            }
                        }
                    } else {
                        // Output is z
                        if (isXY(in1) || isXY(in2)) {
                            // Ignore x00 XOR y00 -> z00
                            if (out != "z00") {
                                wrong += out
                            }
                        }
                    }
                }
                or -> {
                    if (isZ(out) && out != largestZ) {
                        wrong += out
                        return@forEach
                    }
                    if (isXY(in1) || isXY(in2)) {
                        wrong += out
                        return@forEach
                    }
                    val gate1 = nodes[in1] as Node.Gate
                    val gate2 = nodes[in2] as Node.Gate
                    if (gate1.operator != and) {
                        wrong += in1
                    }
                    if (gate2.operator != and) {
                        wrong += in2
                    }

                    // Check or goes into AND or XOR
                    if (usedInGates.any { it.value.operator == or }) {
                        println("or going into or")
                        wrong += out
                    }
                }
                and -> {
                    if (isXY(in1) && isXY(in2)) {
                        if (isZ(out)) {
                            wrong += out
                        }

                        // should go to or
                        if (usedInGates.none { it.value.operator == or }) {
                            if (in1 !in listOf("x00", "y00")) {
                                println("xy and not going to or $out")
                                wrong += out
                            }
                        }
                        return@forEach
                    }
                    if (isXY(in1) || isXY(in2)) {
                        wrong += out
                        return@forEach
                    }
                    if (isZ(out)) {
                        wrong += out
                        return@forEach
                    }

                    // Check that out is used in OR gate
                    if (gates.entries.any { (_, v) ->
                            (v.a == out || v.b == out) && v.operator != or
                    }) {
                        println("and not going into or $out")
                        wrong += out
                        return@forEach
                    }
                }
            }
        }

        println(wrong.size)
        return wrong.sorted().joinToString(",")
    }
}
