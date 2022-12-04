package aoc_2021.day03

import readInput
import readTestInput

fun Boolean.toInt() = if (this) 1 else 0

data class BitOccurrences(val zero: Int, val one: Int) {
    val mostCommon: Int?
        get() = if (one == zero) null else if (one > zero) 1 else 0
    val leastCommon: Int?
        get() = if (one == zero) null else if (one < zero) 1 else 0

    companion object {
        fun of(chars: List<Char>): BitOccurrences =
            chars.map { BitOccurrences((it == '0').toInt(), (it == '1').toInt()) }.reduce(BitOccurrences::sum)
    }

    fun sum(other: BitOccurrences) = BitOccurrences(zero + other.zero, one + other.one)
}

fun List<BitOccurrences>.sum(other: List<BitOccurrences>): List<BitOccurrences> = zip(other, BitOccurrences::sum)

fun binaryToDecimal(binary: Long): Long = if (binary == 0L) 0 else 2 * binaryToDecimal(binary / 10) + binary % 10

// part 2

fun rating(input: List<String>, i: Int = 0, bitExtractor: (BitOccurrences) -> Int): String {
    val bit = bitExtractor(BitOccurrences.of(input.map { it[i] }))
    val candidates = input.filter { it[i].digitToInt() == bit }
    return if (candidates.size == 1) candidates[0] else rating(candidates, i + 1, bitExtractor)
}

fun oxygenRating(input: List<String>) = rating(input) { it.mostCommon ?: 1}.toLong()
fun co2Rating(input: List<String>) = rating(input) { it.leastCommon ?: 0}.toLong()

fun main() {
    fun part1(input: List<String>): Long =
        input.map { it.map { x -> BitOccurrences((x == '0').toInt(), (x == '1').toInt()) } }
            .reduce(List<BitOccurrences>::sum)
            .fold(Pair("", "")) { (first, second), bo -> Pair(first + bo.mostCommon, second + bo.leastCommon) }
            .let { binaryToDecimal(it.first.toLong()) * binaryToDecimal(it.second.toLong()) }


    fun part2(input: List<String>) = binaryToDecimal(oxygenRating(input)) * binaryToDecimal(co2Rating(input))

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("2021", "Day03")

    check(part1(testInput) == 198L)
    check(part2(testInput) == 230L)

    val input = readInput("2021", "Day03")
    println(part1(input))
    println(part2(input))
}
