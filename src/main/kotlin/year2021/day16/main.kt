package year2021.day16

import java.io.File

private data class Packet(
    val version: Int,
    val type: Int,
    var literalValue: Long? = null,
    val subPackets: MutableList<Packet> = mutableListOf()
) {
    val totalValue: Long
        get() = when (type) {
            0 -> subPackets.sumOf { it.totalValue }
            1 -> subPackets.fold(1) { acc, packet -> acc * packet.totalValue }
            2 -> subPackets.minOf { it.totalValue }
            3 -> subPackets.maxOf { it.totalValue }
            4 -> literalValue ?: 0L
            5 -> if (subPackets[0].totalValue > subPackets[1].totalValue) 1 else 0
            6 -> if (subPackets[0].totalValue < subPackets[1].totalValue) 1 else 0
            7 -> if (subPackets[0].totalValue == subPackets[1].totalValue) 1 else 0
            else -> 0L
        }

    val totalVersion: Int
        get() = version + subPackets.sumOf { it.totalVersion }

    fun print(level: Int = 0) {
        println("   ".repeat(level) + "Packet(vs: ${version}, type: ${type}, literalValue: ${literalValue})")
        subPackets.forEach { it.print(level + 1) }
    }
}

private fun parsePacket(bits: String): Pair<Packet, String> {
    val version = bits.substring(0, 3).toInt(2)
    val packetType = bits.substring(3, 6).toInt(2)
    var readBits = 6
    var literalValue: Long? = null
    val subPackets = mutableListOf<Packet>()
    val afterHeader = bits.substring(readBits)
    when (packetType) {
        4 -> {
            var bitCount = 0
            val sb = StringBuilder()
            while (true) {
                val fiveBits = afterHeader.substring(bitCount, bitCount + 5)
                sb.append(fiveBits.substring(1))
                bitCount += 5
                val leadBit = fiveBits[0]
                if (leadBit == '0') {
                    break
                }
            }
            literalValue = sb.toString().toLong(2)
            readBits += bitCount
        }
        else -> {
            val lengthBit = afterHeader[0].digitToInt()
            readBits += 1
            var remainingBits = afterHeader.substring(1)
            when (lengthBit) {
                0 -> {
                    val bitsLength = remainingBits.substring(0, 15).toInt(2)
                    readBits += 15
                    remainingBits = remainingBits.substring(15)
                    var usedBits = 0
                    while (true) {
                        val (newPacket, newRemainingBits) = parsePacket(remainingBits)
                        subPackets.add(newPacket)
                        val diff = remainingBits.length - newRemainingBits.length
                        remainingBits = newRemainingBits
                        readBits += diff
                        usedBits += diff
                        if (usedBits >= bitsLength) {
                            break
                        }
                    }
                }
                1 -> {
                    val subPacketCount = remainingBits.substring(0, 11).toInt(2)
                    readBits += 11
                    remainingBits = remainingBits.substring(11)
                    repeat(subPacketCount) {
                        val (newPacket, newRemainingBits) = parsePacket(remainingBits)
                        subPackets.add(newPacket)
                        val diff = remainingBits.length - newRemainingBits.length
                        remainingBits = newRemainingBits
                        readBits += diff
                    }
                }
            }
        }
    }
    val packet = Packet(version, packetType, literalValue)
    packet.subPackets.addAll(subPackets)
    return packet to bits.substring(readBits)
}

fun main() {
    val file = File("src/main/resources/2021/2021-day16.txt")
    val bits = file.readLines()[0].chunked(2).joinToString("") {
        it.toInt(16).toString(2).padStart(8, '0')
    }
    println(bits)
    val (packet) = parsePacket(bits)
    packet.print()

    // Part 1
    println("Part 1: ${packet.totalVersion}")
    // Correct: 893

    // Part 2
    println("Part 2: ${packet.totalValue}")
    // Correct: 4358595186090
}
