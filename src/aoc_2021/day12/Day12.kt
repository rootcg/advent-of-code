package aoc_2021.day12

import readInput
import readTestInput

data class Cave(val name: String) {
    val isStart = name.equals("start", ignoreCase = true)
    val isEnd = name.equals("end", ignoreCase = true)
    val isSmall = name == name.lowercase()
}

typealias Path = List<Cave>

fun parseInput(input: List<String>): Map<Cave, Path> =
    input.map { it.split("-").map(::Cave) }
        .fold(mutableMapOf()) { acc, (p, c) ->
            acc.apply { merge(p, listOf(c), Path::plus) }.apply { merge(c, listOf(p), Path::plus) }
        }

fun calculatePaths(
    start: Cave,
    caveConnections: Map<Cave, Path>,
    previous: Path = listOf(),
    connectionsFilter: (Cave, Path) -> Boolean
): List<Path> =
    if (start.isEnd) listOf(listOf(start))
    else caveConnections[start]
        ?.filterNot { connectionsFilter(it, previous) }
        ?.flatMap { calculatePaths(start = it, caveConnections, previous + start, connectionsFilter) }
        ?.map { listOf(start) + it }!!

fun main() {

    fun part1(input: List<String>): Int {
        val caveConnections = parseInput(input)
        val start = caveConnections.keys.first(Cave::isStart)
        fun connectionsFilter(cave: Cave, previous: Path) = cave.isSmall && previous.contains(cave)
        return calculatePaths(start, caveConnections, connectionsFilter = ::connectionsFilter).size
    }

    fun part2(input: List<String>): Int {
        fun normalCaveFilter(cave: Cave, previous: Path) = cave.isSmall && previous.contains(cave)
        fun specialCaveFilter(cave: Cave, previous: Path) = previous.count { it == cave } == 2

        val caveConnections = parseInput(input)
        val start = caveConnections.keys.first(Cave::isStart)
        val specialCaves = caveConnections.keys.filter(Cave::isSmall).filterNot(Cave::isStart)

        return specialCaves.flatMap { specialCave ->
            calculatePaths(start, caveConnections) { cave, previous ->
                if (cave == specialCave) specialCaveFilter(cave, previous)
                else normalCaveFilter(cave, previous)
            }
        }.distinct().size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day12")
    check(part1(testInput) == 226)
    check(part2(testInput) == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}