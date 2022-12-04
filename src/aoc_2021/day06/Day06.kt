package aoc_2021.day06

import readInput
import readTestInput

fun parseInput(input: List<String>): List<Int> = input.flatMap { it.split(",") }.map { it.toInt() }

object FamilyCounter {
    private val cache: MutableMap<Int, Long> = mutableMapOf()
    operator fun invoke(days: Int): Long = cache.computeIfAbsent(days) { d -> 1 + ((d - 9) downTo 0 step 7).sumOf { this(it) }}
}

fun main() {

    fun part1(input: List<String>): Long = parseInput(input).map { 80 + (8 - it) }.sumOf { FamilyCounter(it) }
    fun part2(input: List<String>): Long = parseInput(input).map { 256 + (8 - it) }.sumOf { FamilyCounter(it) }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day06")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}