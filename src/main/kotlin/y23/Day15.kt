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


fun main() = solvePuzzle(year = 2023, day = 15) { Day15(it) }

class Day15(val input: Input) : Puzzle {

    data class Lens(val label: String, val focalLen: Int)

    private fun hash(s: String): Int {
        var h = 0
        s.forEach { c ->
            h = ((h + c.code) * 17) % 256
        }
        return h
    }

    override fun solveLevel1(): Any {
        return input.lines
            .requireSingleElement()
            .split(",")
            .sumOf { hash(it) }
    }

    override fun solveLevel2(): Any {
        val boxes = Array<MutableList<Lens>>(256) { mutableListOf() }

        input.lines
            .requireSingleElement()
            .split(",")
            .forEach { instr ->
                if (instr.endsWith("-")) {
                    val label = instr.dropLast(1)
                    val box = hash(label)
                    boxes[box].removeIf { it.label == label }
                } else {
                    val (label, len) = instr.split("=")
                    val box = hash(label)
                    val lens = Lens(label, len.toInt())
                    val existing = boxes[box].indexOfFirst { it.label == label }
                    if (existing >= 0) {
                        boxes[box][existing] = lens
                    } else {
                        boxes[box] += lens
                    }
                }
            }

        return boxes.mapIndexed { boxIdx, box ->
            box.mapIndexed { lensIdx, lens ->
                (1 + boxIdx) * (1 + lensIdx) * lens.focalLen
            }.sum()
        }.sum()
    }
}
