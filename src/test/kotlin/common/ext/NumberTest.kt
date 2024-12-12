package common.ext

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class NumberTest {
    @Test
    fun numDigits() {
        assertEquals(1, 0L.numDigits())
        assertEquals(1, 1L.numDigits())
        assertEquals(1, 9L.numDigits())
        assertEquals(2, 10L.numDigits())
        assertEquals(2, 99L.numDigits())
        assertEquals(3, 100L.numDigits())
    }
}
