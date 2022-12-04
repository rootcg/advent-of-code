package aoc_2021.day10

import readInput
import readTestInput

data class Symbol(val open: Char, val close: Char, val lintPoints: Int, val autocompletePoints: Int)

val symbols: List<Symbol> = listOf(
    Symbol('(', ')', lintPoints = 3, autocompletePoints = 1),
    Symbol('[', ']', lintPoints = 57, autocompletePoints = 2),
    Symbol('{', '}', lintPoints = 1197, autocompletePoints = 3),
    Symbol('<', '>', lintPoints = 25137, autocompletePoints = 4),
)

fun Char.isOpeningSymbol() = symbols.any { it.open == this }
fun Char.lintPoints() = symbols.first { it.close == this }.lintPoints
fun Char.autocompletePoints() = symbols.first { it.close == this }.autocompletePoints
fun Char.isClosedBy(c: Char) = c == symbols.first { it.open == this }.close
fun Char.closeSymbol() = symbols.first { it.open == this }.close

fun <T> List<T>.middle() = get(size / 2)

fun CharArray.firstIncorrectSymbol(): Char? =
    fold(listOf<Char>()) { acc, c ->
        if (c.isOpeningSymbol()) acc + c
        else if (acc.last().isClosedBy(c)) acc.dropLast(1)
        else return c
    }.let { return null }

fun CharArray.hasSyntaxErrors(): Boolean = firstIncorrectSymbol() != null

fun CharArray.incompleteExpressions(): List<Char> =
    fold(listOf()) { acc, c ->
        if (c.isOpeningSymbol()) acc + c
        else if (acc.last().isClosedBy(c)) acc.dropLast(1)
        else throw IllegalStateException("Syntax error")
    }

fun main() {

    fun part1(input: List<String>): Int =
        input.asSequence().map(String::toCharArray).map(CharArray::firstIncorrectSymbol).sumOf { it?.lintPoints() ?: 0 }

    fun part2(input: List<String>): Long =
        input.asSequence()
            .map(String::toCharArray)
            .filterNot(CharArray::hasSyntaxErrors)
            .map(CharArray::incompleteExpressions)
            .map { it.reversed().map(Char::closeSymbol).map(Char::autocompletePoints).fold(0L) { acc, p -> acc * 5 + p } }
            .sortedDescending()
            .toList()
            .middle()

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("2021", "Day10")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("2021", "Day10")
    println(part1(input))
    println(part2(input))
}