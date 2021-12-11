package y21

import common.Day
import common.util.intersectAll
import common.util.unionAll

fun main() = Day8(2)

typealias Pattern = String
typealias Signal = Char
typealias Segment = Char

object Day8 : Day(2021, 8) {
    init {
        useSampleInput {
            """
                be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
                edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
                fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
                fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
                aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
                fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
                dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
                bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
                egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
                gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
            """.trimIndent()
        }
    }

    class Entry(val patterns: List<String>, val outputDigits: List<String>) {
        constructor(line: String) : this(
            line.split(" | ")[0].split(" "),
            line.split(" | ")[1].split(" "),
        )
    }

    val entries = lines.map { Entry(it) }

    // If there's a 1 and 7, the unique digit in 7 is an 'a'.
    // If there's a
    val digits = listOf(
        "abcefg",
        "cf", // 1: 2 segments
        "acdeg",
        "acdfg",
        "bcdf", // 4: 4 segments
        "abdfg",
        "abdefg",
        "acf", // 7: 3 segments
        "abcdefg",
        "abcdfg"
    )

    val lengthsToDigits = digits.let {
        val result = HashMap<Int, MutableList<String>>()
        it.forEach { d ->
            val len = d.length
            if (len !in result) {
                result[len] = mutableListOf()
            }
            result.getValue(len) += d
        }
        result
    }

    override fun level1(): String {
        return entries.sumOf { entry ->
            entry.outputDigits.filter {
                it.length in setOf(2, 4, 3, 7)
            }.size
        }.toString()
    }

    override fun level2(): String {
        return entries.sumOf { entry ->
            val signalMapping = PatternSolver(entry.patterns).solve()
            entry.outputDigits
                .map { pattern -> signalMapping.getDigit(pattern) }
                .joinToString("") { it.toString() }
                .toInt()
        }.toString()
    }

    class SignalMapping(val mapping: Map<Signal, Segment>) {
        fun getDigit(pattern: Pattern): Int {
            val segments = pattern.map { signal -> mapping.getValue(signal) }
                .sorted()
            return digits.indexOf(segments.joinToString(""))
        }
    }

    class PatternSolver(val patterns: List<Pattern>) {
        fun solve(): SignalMapping {
            val mapping = mutableMapOf<Signal, Segment>()
            val patternToPossibleDigits = mutableMapOf<Pattern, List<String>>()
            patterns.forEach { pattern ->
                val digits = lengthsToDigits.getValue(pattern.length)
                patternToPossibleDigits[pattern] = digits.toList()
            }

            val allSignals = patterns.flatMap { it.toCharArray().toList() }.distinct()
            return solveInternal(mapping, patternToPossibleDigits, allSignals)!!
        }

        private fun solveInternal(signalMapping: MutableMap<Signal, Segment>, patternToDigitMapping: Map<Pattern, List<String>>, remainingSignals: List<Signal>): SignalMapping? {
            if (remainingSignals.isEmpty()) {
                // Check that all patterns are valid.
                val result = SignalMapping(signalMapping)
                if (patterns.any { pattern -> result.getDigit(pattern) < 0 }) {
                    return null
                }

                return SignalMapping(signalMapping)
            }

            val signal = remainingSignals.first()
            val newRemainingSignals = remainingSignals.drop(1)

            val patternsWithSignal = patternToDigitMapping.keys.filter { pattern -> signal in pattern }
            val signalDigits = patternsWithSignal.map { pattern -> patternToDigitMapping.getValue(pattern) }
            val flattenedUnioned = signalDigits.map { it.map { digit -> digit.toCharArray().toSet() }.unionAll() }
            val possibleSegments = flattenedUnioned.intersectAll()

            for (segment in possibleSegments) {
                // Try mapping signal to segment.
                signalMapping[signal] = segment

                // Remove signal->segment pairs from patternToDigit mapping.
                val newPatternMapping = patternToDigitMapping.map { (pattern, digits) ->
                    pattern.replace("$signal", "") to digits.map { digit -> digit.replace("$segment", "") }.filter { it.isNotEmpty() }
                }.filter { (key, _) -> key.isNotEmpty() }.toMap()

                val solution = solveInternal(signalMapping, newPatternMapping, newRemainingSignals)
                if (solution != null) {
                    return solution
                }

                // Undo.
                signalMapping.remove(signal)
            }

            return null
        }
    }
}
