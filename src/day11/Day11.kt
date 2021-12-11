package day11

import readInput
import readTestInput

data class Point(val i: Int, val j: Int) {
    private fun up() = Point(i - 1, j)
    private fun upRight() = Point(i - 1, j + 1)
    private fun right() = Point(i, j + 1)
    private fun downRight() = Point(i + 1, j + 1)
    private fun down() = Point(i + 1, j)
    private fun downLeft() = Point(i + 1, j - 1)
    private fun left() = Point(i, j - 1)
    private fun upLeft() = Point(i - 1, j - 1)
    fun allAround(): List<Point> = listOf(up(), upRight(), right(), downRight(), down(), downLeft(), left(), upLeft())
}

data class Octopus(var position: Point, var energy: Int) {
    private var flashed = false
    fun flash(): List<Point> = if (flashed || energy <= 9) emptyList() else apply { flashed = true }.position.allAround()
    fun incrementEnergy(): Octopus = apply { energy += 1 }
    fun resetEnergy(): Octopus = apply { energy = 0 }.apply { flashed = false }
    fun hasFlashed() = flashed
}

fun parseInput(input: List<String>): Map<Point, Octopus> =
        input.map { it.map(Char::digitToInt) }
                .flatMapIndexed { i: Int, row: List<Int> -> row.mapIndexed { j: Int, n: Int -> Octopus(Point(i, j), n) } }
                .associateBy { it.position }

fun incrementEnergy(octopuses: Map<Point, Octopus>) {
    octopuses.forEach { (_, v) -> v.incrementEnergy() }
}

fun flash(octopuses: Map<Point, Octopus>) {
    fun flash(os: Collection<Octopus>) {
        if (os.isNotEmpty()) os.flatMap { it.flash().mapNotNull(octopuses::get) }.map(Octopus::incrementEnergy).let(::flash)
    }
    flash(octopuses.values)
}

fun resetEnergy(octopuses: Map<Point, Octopus>) {
    octopuses.filterValues(Octopus::hasFlashed).forEach { (_, v) -> v.resetEnergy() }
}

fun main() {

    fun part1(input: List<String>): Int {
        val octopuses: Map<Point, Octopus> = parseInput(input)
        var counter = 0

        for (i in 1..100) {
            incrementEnergy(octopuses)
            flash(octopuses)
            counter += octopuses.values.count(Octopus::hasFlashed)
            resetEnergy(octopuses)
        }

        return counter
    }

    fun part2(input: List<String>): Int {
        val octopuses: Map<Point, Octopus> = parseInput(input)

        var steps = 0
        var counter: Int
        do {
            incrementEnergy(octopuses)
            flash(octopuses)
            counter = octopuses.values.count(Octopus::hasFlashed)
            resetEnergy(octopuses)
            steps++
        } while(counter != octopuses.size)

        return steps
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day11")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}