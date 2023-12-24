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
import com.microsoft.z3.*


fun main() = solvePuzzle(year = 2023, day = 24, dryRun = false) { Day24(it) }

// For Z3: will need to set the env var DYLD_LIBRARY_PATH=libs/z3
class Day24(val input: Input) : Puzzle {

    data class Point(val x: Long, val y: Long, val z: Long) {
        operator fun minus(p: Point) = Point(x - p.x, y - p.y, z - p.z)
    }

    data class Hailstone(val point: Point, val velocity: Point)

    val hailstones = input.lines.map { line ->
        val (p, v) = line.split(" @ ")
        val (px, py, pz) = p.split(",").map { it.trim().toLong() }
        val (vx, vy, vz) = v.split(",").map { it.trim().toLong() }
        Hailstone(Point(px, py, pz), Point(vx, vy, vz))
    }

    fun intersects(h1: Hailstone, h2: Hailstone, min: Long, max: Long): Boolean {
        val p1 = h1.point
        val p2 = h2.point

        // xy
        val det = h1.velocity.x * -h2.velocity.y + h2.velocity.x * h1.velocity.y
        if (det == 0L) {
            return false
        }

        val t1 = ((p2.x - p1.x) * -h2.velocity.y - -h2.velocity.x * (p2.y - p1.y))
        val t2 = (h1.velocity.x * (p2.y - p1.y) - (p2.x - p1.x) * h1.velocity.y)

        if (t1 / det.toDouble() < 0 || t2 / det.toDouble() < 0) {
            return false
        }

        val ix1 = det.toBigInteger() * p1.x.toBigInteger() + t1.toBigInteger() * h1.velocity.x.toBigInteger()
        val iy1 = det.toBigInteger() * p1.y.toBigInteger() + t1.toBigInteger() * h1.velocity.y.toBigInteger()

        val minRange = min.toBigInteger()
        val maxRange = max.toBigInteger()
        if (ix1 / det.toBigInteger() < minRange || ix1 / det.toBigInteger() > maxRange || iy1 / det.toBigInteger() < minRange || iy1 / det.toBigInteger() > maxRange) {
            return false
        }

        return true
    }

    fun numIntersections(minXY: Long, maxXY: Long): Int {
        var count = 0
        hailstones.forEachIndexed { i, hi ->
            for (j in i + 1 until hailstones.size) {
                val hj = hailstones[j]
                if (intersects(hi, hj, minXY, maxXY)) {
                    count++
                }
            }
        }
        return count
    }

    override fun solveLevel1(): Any {
        return numIntersections(200000000000000, 400000000000000)
    }

    override fun solveLevel2(): Any {
        val ctx = Context()

        val n = 10

        // Variables
        val rx = ctx.mkRealConst("rx")
        val ry = ctx.mkRealConst("ry")
        val rz = ctx.mkRealConst("rz")
        val vx = ctx.mkRealConst("vx")
        val vy = ctx.mkRealConst("vy")
        val vz = ctx.mkRealConst("vz")
        val t = (0 until n).map { ctx.mkRealConst("t$it") }

        val constraints = mutableListOf<BoolExpr>()
        hailstones.take(n).forEachIndexed { i, h ->
            constraints.add(ctx.mkEq(
                ctx.mkAdd(
                    rx,
                    // +
                    ctx.mkMul(t[i], vx),
                ),
                // ==
                ctx.mkAdd(
                    ctx.mkReal(h.point.x),
                    // +
                    ctx.mkMul(t[i], ctx.mkReal(h.velocity.x))
                )
            ))
            constraints.add(ctx.mkEq(
                ctx.mkAdd(
                    ry,
                    // +
                    ctx.mkMul(t[i], vy),
                ),
                // ==
                ctx.mkAdd(
                    ctx.mkReal(h.point.y),
                    // +
                    ctx.mkMul(t[i], ctx.mkReal(h.velocity.y))
                )
            ))
            constraints.add(ctx.mkEq(
                ctx.mkAdd(
                    rz,
                    // +
                    ctx.mkMul(t[i], vz),
                ),
                // ==
                ctx.mkAdd(
                    ctx.mkReal(h.point.z),
                    // +
                    ctx.mkMul(t[i], ctx.mkReal(h.velocity.z))
                )
            ))
        }

        val solver = ctx.mkSolver()
        constraints.forEach { solver.add(it) }

        val result = solver.check()
        if (result == Status.SATISFIABLE) {
            println("Satisfiable")
            val model = solver.model
            val rxVal = model.eval(rx, true)
            val ryVal = model.eval(ry, true)
            val rzVal = model.eval(rz, true)
            println("rx: $rxVal, ry: $ryVal, rz: $rzVal")
            return rxVal.toString().toLong() + ryVal.toString().toLong() + rzVal.toString().toLong()
        }

        println("Not satisfiable")
        return -1
    }
}
