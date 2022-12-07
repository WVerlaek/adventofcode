package y22

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import common.ext.intersectAll
import common.ext.removeLast

fun main() = solvePuzzle(year = 2022, day = 7) { Day7(it) }

class FileSystem(val root: File.Directory)

sealed class File(var parent: File.Directory? = null) {
    class PlainData(val size: Int) : File()
    class Directory(val children: MutableMap<String, File> = mutableMapOf()): File() {
        var totalSize = -1
        fun computeTotalSize(): Int {
            totalSize = children.values.sumOf { child ->
                when (child) {
                    is PlainData -> child.size
                    is Directory -> child.computeTotalSize()
                }
            }
            return totalSize
        }
    }
}

fun <R> File.reduce(operation: (file: File, childrenReduced: List<R>) -> R): R {
    return when (this) {
        is File.PlainData -> operation(this, emptyList())
        is File.Directory -> operation(this, children.values.map { child -> child.reduce(operation) })
    }
}

fun <R> File.Directory.flattenDirs(operation: (f: File.Directory) -> R): List<R> {
    return children.values.mapNotNull { it as? File.Directory }
        .flatMap { childDir -> childDir.flattenDirs(operation) } + listOf(operation(this))
}

class Day7(val input: Input) : Puzzle {
    val fs = parseFileSystem(input.lines)
    fun parseFileSystem(lines: List<String>): FileSystem {
        val root = File.Directory()

        lateinit var curDir: File.Directory
        lines.forEach { line ->
            when {
                line.startsWith("$ cd ") -> {
                    val target = line.substringAfter("$ cd ")
                    when (target) {
                        "/" -> curDir = root
                        ".." -> curDir = curDir.parent!!
                        else -> curDir = curDir.children.getValue(target) as File.Directory
                    }
                }
                line.startsWith("$ ls") -> {
                    // Ignore.
                }
                else -> {
                    // Line is output of $ ls.
                    val (sizeOrDir, fileName) = line.split(" ")
                    curDir.children[fileName] = when (sizeOrDir) {
                        "dir" -> File.Directory()
                        else -> File.PlainData(sizeOrDir.toInt())
                    }.also { it.parent = curDir }

                }
            }
        }

        root.computeTotalSize()
        return FileSystem(root)
    }

    override fun solveLevel1(): Any {
        val maxSize = 100000
        return fs.root.reduce { f: File, children: List<Int> ->
            when (f) {
                is File.PlainData -> 0
                is File.Directory -> {
                    var sum = children.sum()
                    if (f.totalSize <= maxSize) {
                        sum += f.totalSize
                    }
                    sum
                }
            }
        }
    }

    override fun solveLevel2(): Any {
        val toDelete = fs.root.totalSize - 40000000
        require(toDelete > 0)

        val flattened = fs.root
            .flattenDirs { dir -> dir.totalSize }
            .sorted()
        val idx = flattened.binarySearch(toDelete)
        return if (idx < 0) {
            // When negative, idx == (-insertion point - 1).
            flattened[-idx - 1]
        } else {
            flattened[idx]
        }
    }
}
