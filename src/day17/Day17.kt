package day17

import readInput
import readTestInput

data class Point(val x: Int, val y: Int)

data class Area(val xs: IntRange, val ys: IntRange) {

    companion object {
        private val regex = Regex("""[xy]=(-?\d*)..(-?\d*)""")

        fun of(txt: String): Area {
            val matches = regex.findAll(txt).toList()
            val xs = matches[0].groupValues.drop(1).map { it.toInt() }
            val ys = matches[1].groupValues.drop(1).map { it.toInt() }
            return Area(IntRange(xs[0], xs[1]), IntRange(ys[0], ys[1]))
        }
    }

    fun isIn(p: Point) = p.x in xs && p.y in ys
}

fun main() {

    fun part1(input: List<String>): Int {
        println(Area.of(input[0]))

        return 0
    }

    fun part2(input: List<String>): Int = 0

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day17")
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}