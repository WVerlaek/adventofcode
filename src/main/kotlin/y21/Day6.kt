package y21

import common.Day
import common.util.splitToInts

fun main() = Day6(2)

object Day6 : Day(2021, 6) {
    init {
//        useSampleInput { "3,4,3,1,2" }
        dryRun = true
    }

    val fish = lines[0].splitToInts()

    override fun level1(): String {
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

        return generation.size.toString()
    }

    override fun level2(): String {
        val days = 256
        val cache = FishCache(days)
        return numFishAfterDaysWithCache(fish, days, cache).toString()
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
