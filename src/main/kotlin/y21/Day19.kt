@file:Suppress("UnstableApiUsage")

package y21

import com.google.common.graph.Graph
import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import common.datastructures.Point3
import common.ext.dfs
import common.ext.reduceGraphBFS
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 19, 2) { Day19(it) }

data class Scanner(val id: String, val beacons: List<Point3>, var translateToParent: Point3 = Point3.Zero) {
    fun allTransformations(): List<Scanner> {
        val beaconTransformations = beacons.map { it.allTransformations() }
        return (0 until 24).map { i ->
            Scanner(id, beacons.indices.map { beaconIndex -> beaconTransformations[beaconIndex][i] })
        }
    }

    /**
     * Returns a Point3 translation for the [other] beacons to overlap with
     * this scanner's beacons, or null if there's not a significant overlap.
     */
    fun findOverlappingTranslation(other: Scanner, minOverlap: Int = 12): Point3? {
        for (myBeacon in beacons) {
            for (otherBeacon in other.beacons) {
                // Try moving otherBeacon to myBeacon, and check for overlap.
                val translation = myBeacon - otherBeacon
                val translated = other.beacons.map { it + translation }
                val overlap = beacons.intersect(translated)
                if (overlap.size >= minOverlap) {
                    // Found translation with overlap!
                    return translation
                }
            }
        }
        return null
    }

    fun findOverlap(other: Scanner): Scanner? {
        other.allTransformations().forEach { otherTransformed ->
            val translation = findOverlappingTranslation(otherTransformed)
                ?: return@forEach

            // Found overlap! Return with offset.
            return Scanner(otherTransformed.id, otherTransformed.beacons, translation)
        }

        // No overlapping transformation found.
        return null
    }

    fun mergeWith(other: Scanner): Scanner {
        return Scanner(
            combineIDs(id, other.id),
            beacons.union(other.beacons.map { it + other.translateToParent }).toList(),
        )
    }

    companion object {
        fun parseScanners(lines: List<String>): List<Scanner> {
            val scanners = mutableListOf<Scanner>()
            var from = 0
            while (from < lines.size) {
                val id = lines[from].removePrefix("--- scanner ").removeSuffix(" ---")
                from++
                val beacons = mutableListOf<Point3>()
                while (from < lines.size && lines[from].isNotEmpty()) {
                    beacons += Point3.parse(lines[from])
                    from++
                }
                scanners += Scanner(id, beacons)
                from += 1 // Skip empty line.
            }

            return scanners
        }

        fun combineIDs(id1: String, id2: String) = "[$id1,$id2]"
    }
}

class Day19(val input: Input) : Puzzle {
    val scanners = Scanner.parseScanners(input.lines)

    class ScannerNode(var scanner: Scanner) {
        override fun equals(other: Any?) = other is ScannerNode && scanner.id == other.scanner.id
        override fun hashCode() = scanner.id.hashCode()
        override fun toString() = scanner.id
    }

    fun solveOverlaps(scanners: List<Scanner>): Graph<ScannerNode> {
        // Build a tree, where each node has edges to child scanners that overlap
        // with this scanner.
        val graph: MutableGraph<ScannerNode> = GraphBuilder.directed().build()
        val nodes = scanners.map { ScannerNode(it) }
        nodes.forEach(graph::addNode)
        for (i in 1 until nodes.size) {
            for (j in nodes.indices) {
                if (i == j) continue

                // Try see if i can be connected to j.
                val scannerI = nodes[i].scanner
                val scannerJ = nodes[j].scanner

                scannerJ.findOverlap(scannerI)
                    // No overlap found, continue.
                    ?: continue

                graph.putEdge(nodes[j], nodes[i])
            }
        }

        return graph
    }

    fun Graph<ScannerNode>.distinctBeacons(start: Scanner): Int {
        // Reduce graph by merging all Scanners into the first Scanner.
        val merged = reduceGraphBFS(ScannerNode(start)) { node, reducedChildren: List<Scanner> ->
            var result = node.scanner
            for (child in reducedChildren) {
                val transformedChild = node.scanner.findOverlap(child)!!
                result = result.mergeWith(transformedChild)
            }
            result
        }
        return merged.beacons.size
    }

    fun Graph<ScannerNode>.scannerPositions(start: Scanner): Map<Scanner, Point3> {
        val result = HashMap<Scanner, Point3>()
        dfs(ScannerNode(start)) { parent: Pair<Scanner, Point3>?, node ->
            val relativeToFirstScanner = when (parent) {
                null -> node.scanner
                else -> {
                    parent.first.findOverlap(node.scanner)!!
                }
            }
            val totalTranslation = (parent?.second ?: Point3.Zero) + relativeToFirstScanner.translateToParent
            result[node.scanner] = totalTranslation
            relativeToFirstScanner to totalTranslation
        }
        return result
    }

    override fun solveLevel1(): Any {
        val graph = solveOverlaps(scanners)
        return graph.distinctBeacons(scanners.first())
    }

    override fun solveLevel2(): Any {
        val graph = solveOverlaps(scanners)
        val positions = graph.scannerPositions(scanners.first())
            .values.toList()
        return sequence {
            for (i in positions.indices) {
                for (j in i + 1 until positions.size) {
                    yield(positions[i].manhattanDistTo(positions[j]))
                }
            }
        }.maxOf { it }
    }
}
