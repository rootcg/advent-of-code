package day01

import readInput
import readTestInput

fun main() {
    fun part1(input: List<String>): Int =
        input.map { it.toInt() }.zipWithNext { a, b -> a < b }.count { it }

    fun part2(input: List<String>): Int =
        input.map { it.toInt() }.windowed(3, 1).map { it.sum() }.zipWithNext { a, b -> a < b }.count { it }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day01")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
