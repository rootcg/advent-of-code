package day07

import readInput
import readTestInput
import kotlin.math.absoluteValue

fun main() {

    fun part1(input: List<String>): Int {
        val numbers: List<Int> = input.flatMap { it.split(",") }.map { it.toInt() }
        val max: Int = numbers.maxOf { it }
        return (max downTo 0).map { m -> numbers.sumOf { n -> (m - n).absoluteValue } }.minOf { it }
    }

    fun part2(input: List<String>): Int {
        val numbers: List<Int> = input.flatMap { it.split(",") }.map { it.toInt() }
        val max: Int = numbers.maxOf { it }
        fun calculateFuelCost(moves: Int) = moves * (moves + 1) / 2
        return (max downTo 0).map { m -> numbers.sumOf { n -> calculateFuelCost((m - n).absoluteValue) } }.minOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day07")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}