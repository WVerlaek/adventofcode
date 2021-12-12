package y21

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 6, 2) { Day6(it) }

class Day6(val input: Input) : Puzzle {
    val fish = input.splitToInts()

    override fun solveLevel1(): Any {
        var generation = fish
        val days = 80
        for (i in 0 until days) {
            val nextGen = generation.toMutableList()
            var newFish = 0
            nextGen.forEachIndexed { index, fish ->
                when (fish) {
                    0 -> {
                        nextGen[index] = 6
                        newFish++
                    }
                    else -> nextGen[index] = fish - 1
                }
            }
            for (j in 0 until newFish) nextGen.add(8)
            generation = nextGen
        }

        return generation.size
    }

    override fun solveLevel2(): Any {
        val days = 256
        val cache = FishCache(days)
        return numFishAfterDaysWithCache(fish, days, cache)
    }

    class FishCache(private val maxDays: Int) {
        // fish -> days
        private val cache = HashMap<Int, LongArray>()

        fun has(fish: Int, days: Int): Boolean {
            return fish in cache && cache.getValue(fish)[days] >= 0L
        }

        fun get(fish: Int, days: Int): Long {
            return cache.getValue(fish)[days]
        }

        fun set(fish: Int, days: Int, value: Long) {
            if (fish !in cache) {
                cache[fish] = LongArray(maxDays + 1) { -1L }
            }
            cache.getValue(fish)[days] = value
        }

    }

    private fun numFishAfterDaysWithCache(fish: List<Int>, days: Int, cache: FishCache): Long {
        var total = 0L
        for (f in fish) {
            // For each fish, compute how many fish it will produce in given days.
            // Check if the value has already been cached.
            if (!cache.has(f, days)) {
                val num = when (days) {
                    // No more days, we have one fish.
                    0 -> 1
                    // Compute next generation of fish, and get numFish with days - 1.
                    else -> {
                        val nextFish = when (f) {
                            0 -> listOf(6, 8)
                            else -> listOf(f - 1)
                        }
                        numFishAfterDaysWithCache(nextFish, days - 1, cache)
                    }
                }

                // Store in cache.
                cache.set(f, days, num)
            }
            total += cache.get(f, days)
        }
        return total
    }
}
