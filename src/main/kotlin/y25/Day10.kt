package y25

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess
import com.microsoft.z3.*


fun main() = solvePuzzle(year = 2025, day = 10) { Day10(it) }

class Day10(val input: Input) : Puzzle {

    class Machine(
        val diagram: BooleanArray,
        val buttons: List<List<Int>>,
        val joltage: IntArray,
    )

    private fun parseInput(): List<Machine> {
        return input.lines.map { parseMachine(it) }
    }

    private fun parseMachine(line: String): Machine {
        val regex = "\\[(.+)] (.+) \\{(.*)}".toRegex()
        val result = regex.find(line)!!
        val diagram = result.groups[1]!!.value.map { it == '#' }.toBooleanArray()
        val buttons = result.groups[2]!!.value.split(" ").map { button ->
            button.trim('(', ')').split(",").map { it.toInt() }
        }
        val joltage = result.groups[3]!!.value.split(",").map { it.toInt() }.toIntArray()
        return Machine(diagram, buttons, joltage)
    }

    private fun fewestPresses(machine: Machine): Int {
        class Node(val lights: BooleanArray, val dist: Int) {
            override fun equals(other: Any?): Boolean {
                return other is Node && other.lights.contentEquals(lights)
            }

            override fun hashCode(): Int {
                return lights.contentHashCode()
            }
        }
        val visited = mutableSetOf<Node>()
        val queue = PriorityQueue<Node>(compareBy { it.dist })
        queue += Node(BooleanArray(machine.diagram.size), 0)

        while (queue.isNotEmpty()) {
            val next = queue.poll()
            if (next in visited) {
                continue
            }

            if (next.lights.contentEquals(machine.diagram)) {
                return next.dist
            }
            visited += next

            machine.buttons.forEach { button ->
                val newLights = next.lights.copyOf()
                button.forEach { i -> newLights[i] = !newLights[i] }
                queue.add(Node(newLights, next.dist + 1))
            }
        }

        return -1
    }

    private fun fewestJoltagePressesLinearSolver(machine: Machine): Int {
        val ctx = Context()

        // Variables
        // n_i = amount of button presses of machine.buttons[i]
        // For each counter c_i, sum of buttons b_j with c_i * n_j = z_i
        val n = List(machine.buttons.size) { index ->
            ctx.mkIntConst("n_$index")
        }

        val constraints = mutableListOf<BoolExpr>()
        machine.joltage.forEachIndexed { joltIndex, jolt ->
            val buttonVars = machine.buttons
                .mapIndexed { btnIndex, btn ->
                    btnIndex to btn
                }
                .filter { (_, btn) -> joltIndex in btn }
                .map { (btnIndex, _) -> n[btnIndex] }
            val sum = ctx.mkAdd(*buttonVars.toTypedArray())
            val constraint = ctx.mkEq(
                ctx.mkReal(jolt),
                sum,
            )
            constraints += constraint
        }

        val zero = ctx.mkReal(0)
        n.forEach { btn ->
            constraints += ctx.mkGe(btn, zero)
        }

        val sum = ctx.mkAdd(*n.toTypedArray())

        val optimize = ctx.mkOptimize()
        constraints.forEach { optimize.Add(it) }
        optimize.MkMinimize(sum)

        val result = optimize.Check()
        if (result == Status.SATISFIABLE) {
            val model = optimize.model
            val result = model.evaluate(sum, false) as IntNum
            return result.int
        }

        error("not satisfiable")
    }

    override fun solveLevel1(): Any {
        val machines = parseInput()
        return machines.sumOf { fewestPresses(it) }
    }

    override fun solveLevel2(): Any {
        val machines = parseInput()
        return machines.sumOf {
            fewestJoltagePressesLinearSolver(it).also { println(it) }
        }
    }
}
