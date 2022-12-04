package aoc_2021.day15

import readInput
import readTestInput
import java.util.*
import kotlin.Comparator

data class Coordinate(val x: Int, val y: Int)
fun Coordinate.left() = Coordinate(x - 1, y)
fun Coordinate.right() = Coordinate(x + 1, y)
fun Coordinate.up() = Coordinate(x, y - 1)
fun Coordinate.down() = Coordinate(x, y + 1)
fun Coordinate.neighbors() = listOf(right(), down(), left(), up())

operator fun List<List<Long>>.get(coor: Coordinate) = if (coor.y in indices && coor.x in get(0).indices) this[coor.y][coor.x] else null
fun List<List<Long>>.contains(coor: Coordinate) = coor.x >= 0 && coor.y >= 0 && coor.x < this[0].size && coor.y < this.size
val List<List<Long>>.lastCoordinate get() = Coordinate(this[0].size - 1, this.size - 1)

data class Room(val coor: Coordinate, var dist: Long, val tax: Long) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Room -> other.coor == coor
            else -> false
        }
    }

    override fun hashCode(): Int {
        return coor.hashCode()
    }
}

fun main() {

    fun calculateMinimumRisk(cave: List<List<Long>>, from: Coordinate, to: Coordinate): Long {
        val nodes =
            cave.flatMapIndexed { i, xs -> List(xs.size) { j -> Coordinate(j, i) } }
                .associateWith { Long.MAX_VALUE }
                .mapValues { (coor, dist) -> Room(coor, dist, cave[coor]!!) }
                .toMutableMap()

        val firstNode = nodes[from]!!
        firstNode.dist = 0

        val queue: PriorityQueue<Room> = PriorityQueue(Comparator.comparingLong(Room::dist))
        queue.add(firstNode)

        while (queue.any()) {
            val currentNode = queue.poll()
            if (currentNode.coor == to) return currentNode.dist
            currentNode.coor.neighbors().mapNotNull { nodes[it] }.forEach { neighbor ->
                val distToNeighbor = currentNode.dist + neighbor.tax
                if (distToNeighbor < neighbor.dist) {
                    neighbor.dist = distToNeighbor
                    queue.add(neighbor)
                }
            }
        }

        throw IllegalStateException("No path found")
    }

    fun increment(n: Long, times: Int): Long = when (times) {
        0 -> n
        else -> increment(if (n == 9L) 1 else n + 1, times - 1)
    }

    fun expandCave(cave: List<List<Long>>, times: Int): List<List<Long>> {
        val reference = List(times) { i -> List(times) { j -> i + j } }
        val width = cave[0].size
        val height = cave.size
        val expandedCave = MutableList(height * 5) { MutableList(width * 5) { 0L } }

        reference.forEachIndexed { y, refRow ->
            refRow.forEachIndexed { x, ref ->
                for (i in 0 until height) {
                    for (j in 0 until width) {
                        expandedCave[i + (height * y)][j + (width * x)] = increment(cave[i][j], ref)
                    }
                }
            }
        }

        return expandedCave
    }

    fun part1(input: List<String>): Long {
        val cave = input.map { it.toCharArray().map { n -> n.digitToInt().toLong() }.toList() }
        return calculateMinimumRisk(cave, Coordinate(0, 0), cave.lastCoordinate)
    }

    fun part2(input: List<String>): Long {
        val cave = expandCave(input.map { it.toCharArray().map { n -> n.digitToInt().toLong() }.toList() }, 5)
        return calculateMinimumRisk(cave, Coordinate(0, 0), cave.lastCoordinate)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("2021", "Day15")
    check(part1(testInput) == 40L)
    check(part2(testInput) == 315L)

    val input = readInput("2021", "Day15")
    println(part1(input))
    println(part2(input))
}