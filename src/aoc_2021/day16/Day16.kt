package aoc_2021.day16

import aoc_2021.day16.LengthType.BITS
import aoc_2021.day16.LengthType.PACKETS
import aoc_2021.day16.PacketType.*
import readInput
import readTestInput

enum class PacketType {
    LITERAL, SUM, PRODUCT, MIN, MAX, GT, LT, EQ;

    companion object {
        fun of(id: Int) =
            when (id) {
                0 -> SUM
                1 -> PRODUCT
                2 -> MIN
                3 -> MAX
                4 -> LITERAL
                5 -> GT
                6 -> LT
                7 -> EQ
                else -> throw IllegalStateException("Unknown Packet Type")
            }
    }
}

enum class LengthType(val size: Int) {
    BITS(15), PACKETS(11);

    companion object {
        fun of(id: Int) =
            when (id) {
                0 -> BITS
                1 -> PACKETS
                else -> throw IllegalStateException("Unknown Length Type")
            }
    }
}

sealed class Packet(val version: Int, val size: Int) {
    abstract fun accumulatedVersion(): Int
    abstract fun value(): Long
}

class LiteralPacket(version: Int, size: Int, val content: Long) : Packet(version, size) {
    override fun accumulatedVersion(): Int = version
    override fun value(): Long = content
}

class OperatorPacket(version: Int, size: Int, private val type: PacketType, val subpackets: List<Packet>) : Packet(version, size) {

    override fun accumulatedVersion(): Int = version + subpackets.sumOf { it.accumulatedVersion() }

    override fun value(): Long =
        when (type) {
            SUM -> subpackets.sumOf { it.value() }
            PRODUCT -> subpackets.map { it.value() }.fold(1) { acc, v -> acc * v }
            MIN -> subpackets.minOf { it.value() }
            MAX -> subpackets.maxOf { it.value() }
            GT -> if (subpackets[0].value() > subpackets[1].value()) 1 else 0
            LT -> if (subpackets[0].value() < subpackets[1].value()) 1 else 0
            EQ -> if (subpackets[0].value() == subpackets[1].value()) 1 else 0
            else -> throw IllegalStateException("Unknown Operation Type")
        }

}

fun <E, T : MutableList<E>> T.poll(n: Int): MutableList<E> = take(n).also { repeat(n) { removeFirst() } }.toMutableList()
fun MutableList<Char>.decimal() = joinToString("").toInt(radix = 2)
fun MutableList<Char>.binary() = joinToString("")
fun MutableList<Char>.hasContent() = any { it.digitToInt() == 1 }

object Packets {

    private const val VERSION_SIZE = 3
    private const val TYPE_SIZE = 3
    private const val LENGTH_TYPE_SIZE = 1

    fun fromHex(packet: String): List<Packet> = fromBinary(hexToBinary(packet))

    private fun fromBinary(packet: String, packetLimit: Int? = null): List<Packet> {
        val buffer = packet.toMutableList()
        val packets = mutableListOf<Packet>()

        while (buffer.hasContent() && (packetLimit == null || packets.size < packetLimit)) {
            val version = buffer.poll(VERSION_SIZE).decimal()
            val type = buffer.poll(TYPE_SIZE).decimal().let { PacketType.of(it) }

            packets.add(
                when (type) {
                    LITERAL -> {
                        var literalValue = ""
                        var prefix = 1
                        var packetSize = VERSION_SIZE + TYPE_SIZE
                        while (prefix == 1) {
                            val bitGroup = buffer.poll(5)
                            prefix = bitGroup.poll(1).decimal()
                            literalValue += bitGroup.binary()
                            packetSize += 5
                        }
                        LiteralPacket(version, packetSize, literalValue.toLong(radix = 2))
                    }
                    else -> {
                        val lengthType = buffer.poll(LENGTH_TYPE_SIZE).first().digitToInt().let { LengthType.of(it) }
                        val length = buffer.poll(lengthType.size).decimal()
                        val subpackets = when (lengthType) {
                            BITS -> fromBinary(buffer.poll(length).binary())
                            PACKETS -> fromBinary(buffer.binary(), length).also { ps -> buffer.poll(ps.sumOf { p -> p.size }) }
                        }
                        val packetSize = VERSION_SIZE + TYPE_SIZE + LENGTH_TYPE_SIZE + lengthType.size + subpackets.sumOf { it.size }
                        OperatorPacket(version, packetSize, type, subpackets)
                    }
                }
            )
        }

        return packets
    }

    private fun hexToBinary(hex: String) =
        hex.toCharArray().joinToString("") { it.digitToInt(radix = 16).toString(radix = 2).padStart(4, '0') }
}

fun main() {

    fun part1(input: List<String>): Unit = input.forEach { println(Packets.fromHex(it).first().accumulatedVersion()) }

    fun part2(input: List<String>): Unit = input.forEach { println(Packets.fromHex(it).first().value()) }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("2021", "Day16")
    println("TEST PART ONE: ")
    part1(testInput)
    println("TEST PART TWO: ")
    part2(testInput)

    println()

    val input = readInput("2021", "Day16")
    println("PART ONE: ")
    part1(input)
    println("PART TWO: ")
    part2(input)
}