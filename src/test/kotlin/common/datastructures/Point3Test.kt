package common.datastructures

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Point3Test {

    @Test
    fun testPlus() {
        assertEquals(
            Point3(5, 7, 9),
            Point3(1, 2, 3) + Point3(4, 5, 6))
    }

    @Test
    fun testMinus() {
        assertEquals(
            Point3(-1, -3, -5),
            Point3(3, 2, 1) - Point3(4, 5, 6))
    }

    @Test
    fun testGetAxis() {
        val p = Point3(1, 2, 3)
        assertEquals(1, p[Axis.X])
        assertEquals(2, p[Axis.Y])
        assertEquals(3, p[Axis.Z])
    }

    @Test
    fun testMap() {
        val p = Point3(1, 2, 3)
        assertEquals(Point3(0, 2, 6), p.map { axis, i -> axis.ordinal * i })
    }

    @Test
    fun testRotated90() {
        val p = Point3(1, 2, 3)
        assertEquals(Point3(1, 2, 3), p.rotated90(Axis.X, 0))
        assertEquals(Point3(1, 3, -2), p.rotated90(Axis.X, 1))
        assertEquals(Point3(1, -2, -3), p.rotated90(Axis.X, 2))
        assertEquals(Point3(1, -3, 2), p.rotated90(Axis.X, 3))
        assertEquals(Point3(1, 2, 3), p.rotated90(Axis.X, 4))

        assertEquals(Point3(1, 2, 3), p.rotated90(Axis.Y, 0))
        assertEquals(Point3(-3, 2, 1), p.rotated90(Axis.Y, 1))
        assertEquals(Point3(-1, 2, -3), p.rotated90(Axis.Y, 2))
        assertEquals(Point3(3, 2, -1), p.rotated90(Axis.Y, 3))
        assertEquals(Point3(1, 2, 3), p.rotated90(Axis.Y, 4))

        assertEquals(Point3(1, 2, 3), p.rotated90(Axis.Z, 0))
        assertEquals(Point3(2, -1, 3), p.rotated90(Axis.Z, 1))
        assertEquals(Point3(-1, -2, 3), p.rotated90(Axis.Z, 2))
        assertEquals(Point3(-2, 1, 3), p.rotated90(Axis.Z, 3))
        assertEquals(Point3(1, 2, 3), p.rotated90(Axis.Z, 4))
    }

    @Test
    fun testFlip() {
        val p = Point3(1, 2, 3)
        assertEquals(Point3(-1, 2, 3), p.flip(Axis.X))
        assertEquals(Point3(1, -2, 3), p.flip(Axis.Y))
        assertEquals(Point3(1, 2, -3), p.flip(Axis.Z))
    }
}
