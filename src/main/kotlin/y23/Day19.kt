package y23

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2023, day = 19) { Day19(it) }

class Day19(val input: Input) : Puzzle {

    data class Workflow(
        val name: String,
        val rules: List<Rule>,
    )

    sealed class Destination {
        data class Workflow(val name: String): Destination()
        data object Accept : Destination()
        data object Reject : Destination()
    }

    sealed class Rule {
        data class Condition(
            val field: Char,
            val operator: Char,
            val value: Int,
            val dst: Destination,
        ) : Rule()
        data class Always(val dst: Destination): Rule()
    }

    data class Part(
        val x: Int,
        val m: Int,
        val a: Int,
        val s: Int,
    )

    private val workflows = input.lines
        .subList(0, input.lines.indexOf(""))
        .map { line ->
            val name = line.substringBefore("{")
            val rules = line.substringAfter("{")
                .substringBefore("}")
                .split(",")
                .map { ruleStr ->
                    val split = ruleStr.split(":")

                    fun destOf(s: String) = when (s) {
                        "A" -> Destination.Accept
                        "R" -> Destination.Reject
                        else -> Destination.Workflow(s)
                    }

                    val rule = if (split.size == 1) {
                        Rule.Always(destOf(split[0]))
                    } else {
                        val field = split[0][0]
                        val op = split[0][1]
                        val value = split[0].substring(2).toInt()
                        Rule.Condition(field, op, value, destOf(split[1]))
                    }
                    rule
                }
            Workflow(name, rules)
        }
        .associateBy { it.name }

    val parts = input.lines.subList(input.lines.indexOf("") + 1, input.lines.size)
        .map { line ->
            val (x, m, a, s) = line.substring(1, line.length - 1)
                .split(",")
                .map { it.substring(2).toInt() }
            Part(x, m, a, s)
        }

    fun Part.isAccepted(workflows: Map<String, Workflow>): Boolean {
        var w = workflows.getValue("in")
        var ruleIdx = 0

        while (true) {
            val rule = w.rules[ruleIdx]
            val dst = when (rule) {
                is Rule.Always -> rule.dst
                is Rule.Condition -> {
                    val actual = when (rule.field) {
                        'x' -> x
                        'm' -> m
                        'a' -> a
                        's' -> s
                        else -> error("")
                    }
                    val satisfies = when (rule.operator) {
                        '>' -> actual > rule.value
                        '<' -> actual < rule.value
                        else -> error("")
                    }
                    if (satisfies) rule.dst else null
                }
            }
            when (dst) {
                null -> ruleIdx++
                is Destination.Accept -> return true
                is Destination.Reject -> return false
                is Destination.Workflow -> {
                   w = workflows.getValue(dst.name)
                   ruleIdx = 0
                }
            }
        }
    }

    data class PartRange(
        val fields: Map<Char, IntRange>,
    ) {
        val combinations = fields.values.fold(1L) { acc, range -> acc * range.size }
    }

    fun distinctCombinations(workflows: Map<String, Workflow>, workflow: String, ruleIdx: Int, range: PartRange): Long {
        if (range.combinations == 0L) {
            return 0L
        }

        var w = workflows.getValue(workflow)
        val rule = w.rules[ruleIdx]

        when (rule) {
            is Rule.Always -> {
                return when (rule.dst) {
                    is Destination.Reject -> 0L
                    is Destination.Accept -> range.combinations
                    is Destination.Workflow -> distinctCombinations(workflows, rule.dst.name, 0, range)
                }
            }
            is Rule.Condition -> {
                val field = rule.field
                val r = range.fields.getValue(field)
                val rangeSatisfies = when (rule.operator) {
                    '>' -> maxOf(rule.value + 1, r.first)..r.last
                    '<' -> r.first..minOf(rule.value - 1, r.last)
                    else -> error("")
                }.let { intRange -> PartRange(range.fields.toMutableMap().also { it[field] = intRange }) }
                val rangeNotSatisfies = when (rule.operator) {
                    '>' /* <= */ -> r.first..minOf(rule.value, r.last)
                    '<' /* >= */ -> maxOf(rule.value, r.first)..r.last
                    else -> error("")
                }.let { intRange -> PartRange(range.fields.toMutableMap().also { it[field] = intRange }) }

                val satisfiesCombinations = when (rule.dst) {
                    is Destination.Reject -> 0L
                    is Destination.Accept -> rangeSatisfies.combinations
                    is Destination.Workflow -> distinctCombinations(workflows, rule.dst.name, 0, rangeSatisfies)
                }
                val notSatisfiesCombinations = distinctCombinations(workflows, workflow, ruleIdx + 1, rangeNotSatisfies)
                return satisfiesCombinations + notSatisfiesCombinations
            }
        }
    }

    override fun solveLevel1(): Any {
        return parts.filter { it.isAccepted(workflows) }
            .sumOf { it.x + it.m + it.a + it.s }
    }

    override fun solveLevel2(): Any {
        return distinctCombinations(workflows, "in", 0, PartRange(mapOf(
            'x' to 1..4000,
            'm' to 1..4000,
            'a' to 1..4000,
            's' to 1..4000,
        )))
    }
}
