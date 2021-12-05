package day05

import readInput
import readTestInput
import kotlin.math.max
import kotlin.math.min

fun List<String>.toPoint():Pair<Int, Int> = Pair(get(0).toInt(), get(1).toInt())

data class Range(val start: Pair<Int, Int>, val end: Pair<Int, Int>) {
    val all by lazy {
        if (isDiagonal()) xs().zip(ys())
        else xs().flatMap { x -> ys().map { y -> Pair(x, y) } }
    }

    fun isHorizontal() = start.first == end.first
    fun isVertical() = start.second == end.second
    private fun isDiagonal() = !(isHorizontal() or isVertical())

    private fun xs() = if (start.first < end.first) start.first.rangeTo(end.first) else start.first downTo end.first
    private fun ys() = if (start.second < end.second) start.second.rangeTo(end.second) else start.second downTo end.second
}

fun parseRanges(input: List<String>): List<Range> =
    input.map { it.split(" -> ").map { point -> point.split(",").toPoint() } }.map { Range(it[0], it[1]) }

fun main() {

    fun part1(input: List<String>): Int =
        parseRanges(input)
            .filter { it.isHorizontal()  || it.isVertical() }
            .flatMap { it.all }
            .groupingBy { it }
            .eachCount()
            .filterValues { it > 1 }
            .count()

    fun part2(input: List<String>): Int =
        parseRanges(input)
            .flatMap { it.all }
            .groupingBy { it }
            .eachCount()
            .filterValues { it > 1 }
            .count()

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day05")

    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
