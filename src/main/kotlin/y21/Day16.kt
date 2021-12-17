package y21

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 16, 2) { Day16(it) }

sealed class Packet(val version: Int, val type: Int) {
    class Literal(version: Int, type: Int, val number: Long) : Packet(version, type)
    class Operator(version: Int, type: Int, val lengthTypeID: Boolean, val subPackets: List<Packet>) : Packet(version, type)

    companion object {
        fun parsePacket(bits: String): Pair<Packet, Int> {
            val version = bits.substring(0, 3).toInt(2)
            return when (val type = bits.substring(3, 6).toInt(2)) {
                4 -> {
                    // Literal.
                    var from = 6
                    var numberBits = ""
                    while (true) {
                        numberBits += bits.substring(from + 1, from + 5)
                        if (bits[from] == '0') {
                            break
                        }
                        from += 5
                    }
                    val number = numberBits.toLong(2)
                    val literal = Literal(version, type, number)
                    literal to from + 5
                }
                else -> {
                    // Operator.
                    val lengthTypeId = bits[6] == '1'
                    val lengthNumBits = if (lengthTypeId) 11 else 15
                    val lengthBits = bits.substring(7, 7+lengthNumBits)
                    val numSubPackets = if (lengthTypeId) lengthBits.toInt(2) else Int.MAX_VALUE
                    val numSubPacketBits = if (lengthTypeId) Int.MAX_VALUE else lengthBits.toInt(2)

                    val (subPackets, subLen) = parseSubPackets(bits.substring(7 + lengthNumBits), numSubPackets, numSubPacketBits)
                    val operator = Operator(version, type, lengthTypeId, subPackets)
                    operator to 7 + lengthNumBits + subLen
                }
            }
        }

        private fun parseSubPackets(bits: String, maxPackets: Int, maxPacketBits: Int): Pair<List<Packet>, Int> {
            var totalLen = 0
            val packets = mutableListOf<Packet>()
            for (i in 0 until maxPackets) {
                if (totalLen >= maxPacketBits) {
                    break
                }

                val substr = bits.substring(totalLen)
                val (packet, len) = parsePacket(substr)
                totalLen += len
                packets += packet
            }

            return packets to totalLen
        }
    }
}

fun Char.hexToBits() = when (this) {
    '0' -> "0000"
    '1' -> "0001"
    '2' -> "0010"
    '3' -> "0011"
    '4' -> "0100"
    '5' -> "0101"
    '6' -> "0110"
    '7' -> "0111"
    '8' -> "1000"
    '9' -> "1001"
    'A' -> "1010"
    'B' -> "1011"
    'C' -> "1100"
    'D' -> "1101"
    'E' -> "1110"
    'F' -> "1111"
    else -> throw NumberFormatException("Invalid hex char: $this")
}

class Day16(val input: Input) : Puzzle {
    fun String.hexToBits(): String {
        return map { c -> c.hexToBits() }
            .joinToString("")
    }
    override fun solveLevel1(): Any {
        val (packet, _) = Packet.parsePacket(input.string.hexToBits())
        return packet.sumOfVersions()
    }

    override fun solveLevel2(): Any {
        val (packet, _) = Packet.parsePacket(input.string.hexToBits())
        return packet.evaluate()
    }

    private fun Packet.sumOfVersions(): Int = when (this) {
        is Packet.Literal -> version
        is Packet.Operator -> version + subPackets.sumOf { it.sumOfVersions() }
    }

    fun Packet.evaluate(): Long {
        return when (this) {
            is Packet.Literal -> number
            is Packet.Operator -> {
                val subEvaluations = subPackets.map { it.evaluate() }
                when (type) {
                    0 -> subEvaluations.sumOf { it }
                    1 -> subEvaluations.fold(1) { acc, l -> acc * l }
                    2 -> subEvaluations.minOf { it }
                    3 -> subEvaluations.maxOf { it }
                    5 -> if (subEvaluations[0] > subEvaluations[1]) 1 else 0
                    6 -> if (subEvaluations[0] < subEvaluations[1]) 1 else 0
                    7 -> if (subEvaluations[0] == subEvaluations[1]) 1 else 0
                    else -> throw IllegalStateException("Unknown type $type")
                }
            }
        }
    }
}
