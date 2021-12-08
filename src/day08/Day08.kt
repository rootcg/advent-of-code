package day08

import readInput
import readTestInput

fun parseInput(input: List<String>): List<Display> =
    input.map { l -> l.split("|").map { it.split(" ").filter(String::isNotEmpty).map(String::toSet) } }.map { (a, b) -> Display(a, b) }

val numbers: List<Pair<Set<Char>, Int>> =
    listOf("abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg")
        .map(String::toSet)
        .zip(0..9)

fun List<Pair<Set<Char>, Int>>.withCode(code: Set<Char>) = first { (_code, _) -> _code == code }

data class Display(val input: List<Set<Char>>, val output: List<Set<Char>>) {

    private class EquivalenceTable(codedNumbers: List<Set<Char>>) {
        private val table: Map<Char, Char>

        init {
            val one = codedNumbers.first { it.size == 2 }
            val four = codedNumbers.first { it.size == 4 }
            val seven = codedNumbers.first { it.size == 3 }

            val codes: MutableMap<Char, Char> = mutableMapOf()
            codes['a'] = seven.filterNot(one::contains).first()

            val eg = codedNumbers.filter { it.size == 5 }.map { it - four - seven }
            codes['g'] = eg.first { it.size == 1 }.first()
            codes['e'] = eg.first { it.size == 2 }.minus(codes['g']).first()!!

            val bd = codedNumbers.filter { it.size == 5 }.map { it - seven - codes['e'] - codes['g'] }
            codes['d'] = bd.first { it.size == 1 }.first()!!
            codes['b'] = bd.first { it.size == 2 }.minus(codes['d']).first()!!

            codes['c'] = codedNumbers.filter { it.size == 5 }
                .first { it.containsAll(setOf(codes['a'], codes['d'], codes['e'], codes['g'])) }
                .let { it - codes['a'] - codes['d'] - codes['e'] - codes['g'] }
                .first()!!

            codes['f'] = one.minus(codes['c']).first()!!

            table = codes.map { (k, v) -> Pair(v, k) }.toMap()
        }

        fun decode(char: Char): Char = table[char]!!
    }

    private val equivalence: EquivalenceTable = EquivalenceTable(input)

    val decodedOutput: Int =
        output.map { it.map(equivalence::decode).toSet() }
            .map(numbers::withCode)
            .map { it.second }
            .fold("", String::plus)
            .toInt()
}

fun hasUniqueDigitCombination(wires: Set<Char>): Boolean =
    when (wires.size) {
        2, 4, 3, 7 -> true
        else -> false
    }

fun main() {

    fun part1(input: List<String>): Int =
        parseInput(input).flatMap(Display::output).count { hasUniqueDigitCombination(it) }

    fun part2(input: List<String>): Int = parseInput(input).sumOf { it.decodedOutput }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day08")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}