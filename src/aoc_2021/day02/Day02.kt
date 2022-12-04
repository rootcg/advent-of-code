package aoc_2021.day02

import readInput
import readTestInput

class OldSubmarine {

    fun interface Move {
        operator fun invoke(point: Pair<Int, Int>): Pair<Int, Int>
    }

    enum class MoveTemplate {
        FORWARD {
            override fun invoke(distance: Int): Move = Move { (a, b) -> Pair(a + distance, b) }
        },
        DOWN {
            override fun invoke(distance: Int): Move = Move { (a, b) -> Pair(a, b + distance) }
        },
        UP {
            override fun invoke(distance: Int): Move = Move { (a, b) -> Pair(a, b - distance) }
        };

        abstract operator fun invoke(distance: Int): Move
    }

    private fun String.toMove(): Move {
        val (direction, distance) = this.split(" ")
        val moveTemplate = MoveTemplate.valueOf(direction.uppercase())
        return moveTemplate(distance.toInt())
    }

    fun move(directions: List<String>): Int =
        directions.map { it.toMove() }.fold(Pair(0, 0)) { acc, op -> op(acc) }.let { (a, b) -> a * b }
}

class ModernSubmarine {

    fun interface Move {
        operator fun invoke(point: Triple<Int, Int, Int>): Triple<Int, Int, Int>
    }

    enum class MoveTemplate {
        FORWARD {
            override fun invoke(distance: Int): Move =
                Move { (a, b, c) -> Triple(a + distance, b + (c * distance), c) }
        },
        DOWN {
            override fun invoke(distance: Int): Move = Move { (a, b, c) -> Triple(a, b, c + distance) }
        },
        UP {
            override fun invoke(distance: Int): Move = Move { (a, b, c) -> Triple(a, b, c - distance) }
        };

        abstract operator fun invoke(distance: Int): Move
    }

    private fun String.toMove(): Move {
        val (direction, distance) = this.split(" ")
        val moveTemplate = MoveTemplate.valueOf(direction.uppercase())
        return moveTemplate(distance.toInt())
    }

    fun move(directions: List<String>): Int =
        directions.map { it.toMove() }.fold(Triple(0, 0, 0)) { acc, op -> op(acc) }.let { (a, b) -> a * b }
}

fun main() {
    fun part1(input: List<String>): Int = OldSubmarine().move(input)

    fun part2(input: List<String>): Int = ModernSubmarine().move(input)

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day02")

    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
