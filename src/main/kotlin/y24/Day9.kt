package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2024, day = 9) { Day9(it) }

class Day9(val input: Input) : Puzzle {

    data class Block(val at: Int, val size: Int)

    data class Disk(val disk: List<Int?>) {
        override fun toString(): String {
            return disk.joinToString("") { it?.toString() ?: "."  }
        }
    }

    data class Disk2(val blocks: List<Block>, val freeSpace: List<Block>) {
        val size = blocks.sumOf { it.size } + freeSpace.sumOf { it.size }
    }

    fun newDisk(line: String): Disk {
        val result = mutableListOf<Int?>()
        var isBlock = true
        var blockId = 0
        line.forEachIndexed { _, c ->
            val n = c.digitToInt()
            repeat(n) {
                result += if (isBlock) blockId else null
            }
            if (isBlock) {
                blockId++
            }
            isBlock = !isBlock
        }

        return Disk(result)
    }

    fun newDisk2(line: String): Disk2 {
        val blocks = mutableListOf<Block>()
        val freeSpace = mutableListOf<Block>()

        var isBlock = true
        var idx = 0
        line.forEachIndexed { _, c ->
            val n = c.digitToInt()
            if (isBlock) {
                blocks += Block(idx, n)
            } else {
                freeSpace += Block(idx, n)
            }
            idx += n
            isBlock = !isBlock
        }

        return Disk2(blocks, freeSpace)
    }

    fun compact(d: Disk): Disk {
        val disk = d.disk
        var left = 0
        var right = disk.size - 1

        val newDisk = disk.toMutableList()
        while (left < right) {
            while (disk[right] == null && left < right) right--
            while(disk[left] != null && left < right) left++

            if (left == right) break

            // Left is empty, right is block. Swap
            newDisk[left] = newDisk[right]
            newDisk[right] = null
            left++
            right--
        }

        return Disk(newDisk)
    }

    fun compactWholeBlocks(d: Disk2): Disk {
        val gaps = TreeMap<Int, Int>()
        d.freeSpace.forEach { (at, size) ->
            gaps[at] = size
        }

        val newDisk = Array<Int?>(d.size) { null }
        d.blocks.reversed().forEachIndexed { blockIdReversed, block ->
            val blockId = d.blocks.size - blockIdReversed - 1
            var writeAt = block.at
            for ((at, size) in gaps) {
                if (at >= block.at) {
                    // No more gaps
                    break
                }

                if (size < block.size) {
                    // Too small
                    continue
                }

                // Found free space
                writeAt = at
                gaps.remove(at)
                val newGap = size - block.size
                if (newGap > 0) {
                    gaps[at + block.size] = newGap
                }
                break
            }

            for (i in 0 until block.size) {
                newDisk[writeAt + i] = blockId
            }
        }

        return Disk(newDisk.toList())
    }

    fun checkSum(disk: Disk): Long {
        var sum = 0L
        disk.disk.forEachIndexed { index, block ->
            if (block != null) {
                sum += index * block
            }
        }

        return sum
    }

    override fun solveLevel1(): Any {
        val disk = newDisk(input.lines[0])
        val compacted = compact(disk)
        return checkSum(compacted)
    }

    override fun solveLevel2(): Any {
        val disk = newDisk2(input.lines[0])
        val compacted = compactWholeBlocks(disk)
        return checkSum(compacted)
    }
}
