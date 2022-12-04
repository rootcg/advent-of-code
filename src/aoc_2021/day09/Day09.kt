package aoc_2021.day09

import readInput
import readTestInput

fun parseInput(input: List<String>): List<List<Int>> =
    input.map { it.split("").filter(String::isNotEmpty).map(String::toInt) }

fun isLowPoint(i: Int, j: Int, grid: List<List<Int>>): Boolean {
    val n = grid[i][j]
    return (i == 0 || grid[i - 1][j] > n)
            && (j == 0 || grid[i][j - 1] > n)
            && (i == grid.size - 1 || grid[i + 1][j] > n)
            && (j == grid[0].size - 1 || grid[i][j + 1] > n)
}

fun calculateBasin(i: Int, j: Int, grid: MarkableGrid): Int =
    if (i < 0 || i >= grid.size || j < 0 || j >= grid[0].size || grid[i][j] == 9 || grid.isMarked(i, j)) 0
    else {
        grid.mark(i, j)
        1 + calculateBasin(i - 1, j, grid) + calculateBasin(i, j + 1, grid) + calculateBasin(i + 1, j, grid) + calculateBasin(i, j - 1, grid)
    }

class MarkableGrid(val grid: List<List<Int>>) {
    val size: Int = grid.size
    private val marked: List<MutableList<Boolean>> = grid.map { it.map { false }.toMutableList() }

    fun mark(i: Int, j: Int) {
        marked[i][j] = true
    }

    fun isMarked(i: Int, j: Int) = marked[i][j]

    operator fun get(i: Int): List<Int> = grid[i]
}

fun main() {

    fun part1(input: List<String>): Int {
        val grid: List<List<Int>> = parseInput(input)
        return grid.flatMapIndexed { i, row -> row.filterIndexed { j, _ -> isLowPoint(i, j, grid) } }.sumOf { it + 1 }
    }

    fun part2(input: List<String>): Int {
        val grid: List<List<Int>> = parseInput(input)
        return grid.flatMapIndexed { i, row -> row.mapIndexed { j, _ -> Triple(isLowPoint(i, j, grid), i, j) } }
            .asSequence()
            .filter { it.first }
            .map { (_, i, j) -> calculateBasin(i, j, grid = MarkableGrid(grid)) }
            .sortedDescending()
            .take(3)
            .reduce { a, b -> a * b }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day09")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}