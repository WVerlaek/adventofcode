package y22

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2022, day = 19, dryRun = false) { Day19(it) }

class Day19(val input: Input) : Puzzle {

    enum class Material { Ore, Clay, Obsidian, Geode }

    data class Factory(
        val materials: MutableMap<Material, Int>,
        val robots: MutableMap<Material, Int>,
    ) {
        fun timeToPurchase(costs: Map<Material, Int>): Int {
            return costs.maxOf { (m, amount) ->
                val waitFor = amount - materials.getOrDefault(m, 0)
                if (waitFor <= 0) {
                    return@maxOf 0
                }

                val rate = robots.getOrDefault(m, 0)
                if (rate == 0) {
                    Int.MAX_VALUE
                } else {
                    waitFor.divideRoundUp(rate)
                }
            }
        }

        fun materialsAfterTime(t: Int): MutableMap<Material, Int> {
            val newMaterials = materials.toMutableMap()
            robots.forEach { (m, amount) ->
                newMaterials[m] = newMaterials.getOrDefault(m, 0) + amount * t
            }
            return newMaterials
        }
    }

    data class Blueprint(
        val id: Int,
        val robotCosts: Map<Material, Map<Material, Int>>,
    ) {
        val maxNeededRate = Material.values().map { material ->
            val rate = robotCosts.values.mapNotNull { map -> map[material] }
                .maxOrNull() ?: Int.MAX_VALUE
            material to rate
        }.toMap()
    }

    val regex = "Blueprint ([0-9]+): Each ore robot costs ([0-9]+) ore. Each clay robot costs ([0-9]+) ore. Each obsidian robot costs ([0-9]+) ore and ([0-9]+) clay. Each geode robot costs ([0-9]+) ore and ([0-9]+) obsidian.".toRegex()
    fun parseBlueprints(lines: List<String>): List<Blueprint> {
        return lines.map { line ->
            val match = regex.find(line)!!
            val groups = match.groupValues
            Blueprint(
                groups[1].toInt(),
                mapOf(
                    Material.Ore to mapOf(Material.Ore to groups[2].toInt()),
                    Material.Clay to mapOf(Material.Ore to groups[3].toInt()),
                    Material.Obsidian to mapOf(
                        Material.Ore to groups[4].toInt(),
                        Material.Clay to groups[5].toInt(),
                    ),
                    Material.Geode to mapOf(
                        Material.Ore to groups[6].toInt(),
                        Material.Obsidian to groups[7].toInt(),
                    )
                )
            )
        }
    }

    fun runFactory(blueprint: Blueprint, factory: Factory = Factory(mutableMapOf(), mutableMapOf(Material.Ore to 1)), minutes: Int = 24): Int {
        if (minutes <= 0) {
            return factory.materials[Material.Geode] ?: 0
        }

        var max = factory.materials.getOrDefault(Material.Geode, 0) + factory.robots.getOrDefault(Material.Geode, 0) * minutes
        for (robot in Material.values()) {
            val cost = blueprint.robotCosts[robot]!!
            if (factory.robots.getOrDefault(robot, 0) >= blueprint.maxNeededRate[robot]!!) {
                continue
            }

            val t = factory.timeToPurchase(cost)
            if (t >= minutes) {
                continue
            }

            factory.robots.forEach { (m, amount) ->
                factory.materials[m] = factory.materials.getOrDefault(m, 0) + amount * (t + 1)
            }
            cost.forEach { (m, amount) ->
                factory.materials[m] = factory.materials[m]!! - amount
            }
            factory.robots[robot] = factory.robots.getOrDefault(robot, 0) + 1

            // Recurse.
            max = maxOf(max, runFactory(blueprint, factory, minutes - t - 1))

            // Undo.
            factory.robots[robot] = factory.robots[robot]!! - 1
            cost.forEach { (m, amount) ->
                factory.materials[m] = factory.materials[m]!! + amount
            }
            factory.robots.forEach { (m, amount) ->
                factory.materials[m] = factory.materials.getOrDefault(m, 0) - amount * (t + 1)
            }
        }

        return max
    }

    override fun solveLevel1(): Any {
        val blueprints = parseBlueprints(input.lines)
        return blueprints.map { b ->
            println(b.maxNeededRate)
            b.id * runFactory(b).also { println("Factory ${b.id}: $it")}
        }.sum()
    }

    override fun solveLevel2(): Any {
        val blueprints = parseBlueprints(input.lines).take(3)
        return blueprints.map { b ->
            println(b.maxNeededRate)
            runFactory(b, minutes = 32).also { println("Factory ${b.id}: $it")}
        }.product()
    }
}
