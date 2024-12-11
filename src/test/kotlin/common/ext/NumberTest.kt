package common.ext

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class NumberTest {
    @Test
    fun numDigits() {
        assertEquals(1, 0.numDigits())
        assertEquals(1, 1.numDigits())
        assertEquals(1, 9.numDigits())
        assertEquals(2, 10.numDigits())
        assertEquals(2, 99.numDigits())
        assertEquals(3, 100.numDigits())
    }
}
