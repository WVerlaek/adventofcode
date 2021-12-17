package y21

import common.puzzle.Input
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class Day16Test {
    @Test
    fun testParseLiteralPacket() {
        val (pack, len) = Packet.parsePacket("110100101111111000101000")
        assertEquals(21, len)
        assertTrue(pack is Packet.Literal)
        assertEquals(2021, pack.number)
    }

    @Test
    fun testParseOperatorPacket() {
        val (pack, len) = Packet.parsePacket("00111000000000000110111101000101001010010001001000000000")
        assertEquals(49, len)
        assertTrue(pack is Packet.Operator)
        assertFalse(pack.lengthTypeID)
        assertEquals(1, pack.version)
        assertEquals(6, pack.type)
        val subpackets = pack.subPackets
        assertEquals(2, subpackets.size)
    }

    @Test
    fun solveLevel1() {
        assertEquals(14, Day16(Input("EE00D40C823060")).solveLevel1())
        assertEquals(16, Day16(Input("8A004A801A8002F478")).solveLevel1())
        assertEquals(12, Day16(Input("620080001611562C8802118E34")).solveLevel1())
        assertEquals(23, Day16(Input("C0015000016115A2E0802F182340")).solveLevel1())
        assertEquals(31, Day16(Input("A0016C880162017C3686B18A3D4780")).solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(1L, Day16(Input("9C0141080250320F1802104A08")).solveLevel2())
    }
}
