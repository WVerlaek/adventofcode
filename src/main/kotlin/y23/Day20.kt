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


fun main() = solvePuzzle(year = 2023, day = 20) { Day20(it) }

class Day20(val input: Input) : Puzzle {

    data class Pulse(val high: Boolean, val from: String, val to: String) {
        companion object {
            val Button = Pulse(false, "button", "broadcaster")
        }
    }

    sealed class Module(val name: String) {
        lateinit var incoming: List<String>
        lateinit var outgoing: List<String>

        abstract fun process(pulse: Pulse): List<Pulse>

        fun sendPulse(high: Boolean): List<Pulse> {
            return outgoing.map { Pulse(high, name, it) }
        }

        class Broadcast(name: String) : Module(name) {
            override fun process(pulse: Pulse): List<Pulse> {
                return sendPulse(pulse.high)
            }
        }

        class FlipFlop(name: String) : Module(name) {
            var on = false
            override fun process(pulse: Pulse): List<Pulse> {
                if (pulse.high) {
                    return emptyList()
                }

                on = !on
                return sendPulse(on)
            }
        }

        class Conjunction(name: String) : Module(name) {
            val lastPulse by lazy { incoming.associateWith { false }.toMutableMap() }
            override fun process(pulse: Pulse): List<Pulse> {
                lastPulse[pulse.from] = pulse.high
                return sendPulse(lastPulse.any { (_, high) -> !high })
            }
        }
    }

    private fun parseModules(lines: List<String>): Map<String, Module> {
        val incoming = mutableMapOf<String, List<String>>()
        val outgoing = mutableMapOf<String, List<String>>()
        val modules = mutableMapOf<String, Module>()

        lines.forEach { line ->
            val (mod, to) = line.split(" -> ")
            val module = when {
                mod == "broadcaster" -> Module.Broadcast("broadcaster")
                mod.startsWith("%") -> Module.FlipFlop(mod.substring(1))
                mod.startsWith("&") -> Module.Conjunction(mod.substring(1))
                else -> error("unknown module $mod")
            }

            val name = module.name
            val dests = to.split(", ")
            dests.forEach { d ->
                incoming[d] = (incoming[d] ?: emptyList()) + name
                outgoing[name] = (outgoing[name] ?: emptyList()) + d
            }

            modules[name] = module
        }

        return modules.mapValues { (name, module) ->
            module.also {
                it.incoming = incoming[name] ?: emptyList()
                it.outgoing = outgoing[name] ?: emptyList()
            }
        }
    }

    fun simulatePulse(modules: Map<String, Module>, pulse: Pulse): List<Pulse> {
        val pulses = mutableListOf(pulse)
        var i = 0
        while (i < pulses.size) {
            val next = pulses[i]
            i++

            val module = modules[next.to] ?: continue
            pulses += module.process(next)
        }

        return pulses
    }

    override fun solveLevel1(): Any {
        val modules = parseModules(input.lines)

        val allPulses = (0 until 1000).flatMap {
            simulatePulse(modules, Pulse.Button)
        }

        val high = allPulses.count { it.high }.toLong()
        val low = allPulses.size - high
        return high * low
    }

    fun simulateUntil1(module: String): Long {
        val modules = parseModules(input.lines)
        var i = 0L
        while (true) {
            val pulses = simulatePulse(modules, Pulse.Button)
            i++

            if (pulses.any { it.to == module && !it.high }) {
                return i
            }
        }
    }

    override fun solveLevel2(): Any {
        // See tests for 4 cycles.
        return lcm(3761L, 3739L, 3797L, 3889L)
    }
}
