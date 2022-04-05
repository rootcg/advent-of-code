package day17

import readInput
import readTestInput
import kotlin.math.absoluteValue

data class Velocity(val x: Int, val y: Int) {
    fun drag() = if (x > 0) Velocity(x - 1, y) else if (x < 0) Velocity(x + 1, y) else this
    fun gravity() = Velocity(x, y - 1)
}

data class Point(val x: Int, val y: Int) {
    fun move(velocity: Velocity) = Point(x + velocity.x, y + velocity.y)
}

data class Area(val xs: IntRange, val ys: IntRange) {

    val height: Int = (ys.first.absoluteValue - ys.last.absoluteValue).absoluteValue

    companion object {
        private val regex = Regex("""[xy]=(-?\d*)..(-?\d*)""")

        fun of(txt: String): Area {
            val matches = regex.findAll(txt).toList()
            val xs = matches[0].groupValues.drop(1).map { it.toInt() }
            val ys = matches[1].groupValues.drop(1).map { it.toInt() }
            return Area(IntRange(xs[0], xs[1]), IntRange(ys[0], ys[1]))
        }
    }

    fun contains(p: Point) = p.x in xs && p.y in ys
}

fun nthTriangle(n: Int): Int = 0.rangeTo(n).sum()

fun reachTarget(velocity: Velocity, target: Area, position: Point): Boolean =
    probeTrajectory(velocity, position)
        .takeWhile { (p, _) -> p.x <= target.xs.last && p.y >= target.ys.first }
        .any { (p, _) -> target.contains(p) }

fun probeTrajectory(velocity: Velocity, position: Point) =
    generateSequence(Pair(position, velocity)) { (p, v) -> Pair(p.move(v), v.drag().gravity()) }

fun xVelocitiesCandidates(startTarget: Int, endTarget: Int) =
    IntRange(generateSequence(1, Int::inc).first { nthTriangle(it) >= startTarget }, endTarget)

fun yVelocitiesCandidates(target: Area, xVelocity: Int, position: Point) =
    target.ys.first.rangeTo(target.height * 20).filter { reachTarget(Velocity(xVelocity, it), target, position) }

fun main() {

    fun part1(input: List<String>): Int {
        val target = Area.of(input[0])
        val xVelocityCandidates = xVelocitiesCandidates(target.xs.first, target.xs.last)
        val validVelocities = xVelocityCandidates.map { x -> yVelocitiesCandidates(target, x, Point(0, 0)).map { y -> Velocity(x, y) } }.filter { it.isNotEmpty() }.flatten()

        return validVelocities.map { it.y }.maxOf { it }.let { nthTriangle(it) }
    }

    fun part2(input: List<String>): Int {
        val target = Area.of(input[0])
        val xVelocityCandidates = xVelocitiesCandidates(target.xs.first, target.xs.last)
        val validVelocities = xVelocityCandidates.map { x -> yVelocitiesCandidates(target, x, Point(0, 0)).map { y -> Velocity(x, y) } }.filter { it.isNotEmpty() }.flatten()
        return validVelocities.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day17")
    println("PART ONE TEST: ")
    check(part1(testInput) == 45)
    println("PART TWO TEST: ")
    check(part2(testInput) == 112)

    val input = readInput("Day17")
    println("PART ONE: ")
    println(part1(input))
    println("PART TWO: ")
    println(part2(input))
}