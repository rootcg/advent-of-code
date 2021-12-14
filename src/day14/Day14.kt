package day14

import readInput
import readTestInput

fun main() {

    data class Polymer(val elements: String) {
        fun mostCommonElementCounter(): Int = elements.groupingBy { it }.eachCount().maxOf { it.value }
        fun leastCommonElementCounter(): Int = elements.groupingBy { it }.eachCount().minOf { it.value }

        fun insert(rules: Map<String, Char>, times: Int = 1): Polymer =
            (0 until times).fold(this.elements) { acc, _ ->
                acc.zipWithNext()
                    .map { (a, b) -> rules["$a$b"]?.let { r -> "$a$r" } ?: "$a" }
                    .reduce(String::plus)
                    .let { it + elements.last() }
            }.let(::Polymer)
    }

    fun part1(input: List<String>): Int {
        val polymer = Polymer(input.first())
        val rules = input.drop(2).map { it.split(" -> ") }.associate { (pair, element) -> Pair(pair, element[0]) }
        return polymer.insert(rules, times = 10).let { it.mostCommonElementCounter() - it.leastCommonElementCounter() }
    }

    fun part2(input: List<String>): Long {
        val polymer = input.first()
        val rules = input.drop(2).map { it.split(" -> ") }
            .associate { (pair, element) -> Pair(pair, Pair(element[0], listOf(pair[0] + element, element + pair[1]))) }

        var pairCounters: MutableMap<String, Long> =
            polymer.zipWithNext()
                .map { (a, b) -> "$a$b" }
                .groupingBy { it }
                .foldTo(mutableMapOf(), 0) { acc, _ -> acc + 1 }

        val elementCounters: MutableMap<Char, Long> =
            polymer.toCharArray().groupBy { it }.mapValuesTo(mutableMapOf()) { (_, v) -> v.size.toLong() }

        repeat(40) {
            val _elementCounters: MutableMap<Char, Long> = mutableMapOf()
            val _pairCounters: MutableMap<String, Long> = mutableMapOf()

            pairCounters.forEach { (pair, counter) ->
                rules[pair]?.also { (e, generatedPairs) ->
                    _elementCounters.merge(e, counter, Long::plus)
                    generatedPairs.forEach { generatedPair -> _pairCounters.merge(generatedPair, counter, Long::plus) }
                }
            }

            _elementCounters.forEach { (element, counter) -> elementCounters.merge(element, counter, Long::plus) }
            pairCounters = _pairCounters
        }

        return elementCounters.maxOf { it.value } - elementCounters.minOf { it.value }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day14")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}