package common.datastructures

import common.ext.other
import kotlin.math.abs

enum class Axis {
    X, Y, Z;
    fun next(): Axis = values()[(ordinal + 1) % values().size]
}

data class Point3(val x: Int, val y: Int, val z: Int) {
    companion object {
        val Zero = Point3(0, 0, 0)

        fun parse(line: String): Point3 {
            val (x, y, z) = line.split(",").map { it.toInt() }
            return Point3(x, y, z)
        }
    }

    operator fun plus(other: Point3) = Point3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Point3) = Point3(x - other.x, y - other.y, z - other.z)
    operator fun get(axis: Axis) = when (axis) {
        Axis.X -> x
        Axis.Y -> y
        Axis.Z -> z
    }
    fun add(axis: Axis, i: Int): Point3 {
        return map { a, j ->
            if (a == axis) j + i
            else j
        }
    }

    inline fun map(transform: (Axis, Int) -> Int): Point3 {
        return Point3(
            transform(Axis.X, x),
            transform(Axis.Y, y),
            transform(Axis.Z, z),
        )
    }

    fun rotated90(axis: Axis, amount: Int = 1): Point3 {
        require(amount >= 0)
        var rotated = this
        val rotatingAxes = Axis.values().toList() - axis
        val flipSignFromAxis = axis.next()
        for (i in 0 until amount) {
            rotated = rotated.map { vAxis, v ->
                when (vAxis) {
                    axis -> v
                    else -> {
                        val valueFromAxis = rotatingAxes.other(vAxis)
                        val flipSign = valueFromAxis == flipSignFromAxis
                        if (flipSign) {
                            -rotated[valueFromAxis]
                        } else {
                            rotated[valueFromAxis]
                        }
                    }
                }
            }
        }
        return rotated
    }

    fun flip(axis: Axis): Point3 {
        return map { a, i ->
            when (a) {
                axis -> -i
                else -> i
            }
        }
    }

    fun allTransformations(): List<Point3> {
        // https://stackoverflow.com/questions/16452383/how-to-get-all-24-rotations-of-a-3-dimensional-array
        var p = this
        return sequence {
            for (cycle in 0 until 2) {
                for (step in 0 until 3) {
                    p = p.rotated90(Axis.X)
                    yield(p)
                    for (i in 0 until 3) {
                        p = p.rotated90(Axis.Z)
                        yield(p)
                    }
                }
                p = p.rotated90(Axis.X).rotated90(Axis.Z).rotated90(Axis.X)
            }
        }.toList()
    }

    fun manhattanDistTo(other: Point3) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    fun distSquaredTo(other: Point3): Long {
        val dx = (x - other.x).toLong()
        val dy = (y - other.y).toLong()
        val dz = (z - other.z).toLong()
        return dx * dx + dy * dy + dz * dz
    }

    override fun toString(): String {
        return "{$x,$y,$z}"
    }
}
