package day04

import readInput
import readTestInput

fun parseNumbers(input: List<String>) = input.first().split(",").map { it.toInt() }
fun parseBoards(input: List<String>): List<Board> =
    input.drop(1)
        .filter(String::isNotEmpty)
        .chunked(5)
        .map { it.map { str -> str.split(" ").filterNot(String::isEmpty).map { c -> Square(c.toInt(), false) } } }

data class Square(val value: Int, var marked: Boolean)
typealias Board = List<List<Square>>

fun Board.unmarked() = flatMap { it.filterNot(Square::marked) }.map(Square::value)
fun Board.mark(n: Int) = onEach { row -> row.onEach { sq -> if (sq.value == n) sq.marked = true } }
fun Board.rows() = this
fun Board.columns() = (0 until 5).map { j -> rows().map { row -> row[j] } }
fun Board.completeRows() = rows().filter { it.map(Square::marked).reduce(Boolean::and) }
fun Board.completeColumns() = columns().filter { it.map(Square::marked).reduce(Boolean::and) }
fun Board.won() = (completeRows() + completeColumns()).isNotEmpty()

data class Game(val boards: List<Board>, val numbers: List<Int>)

val Game.firstWinner: Int
    get() = numbers.asSequence()
        .map { n -> n * (boards.asSequence().filterNot(Board::won).map { b -> b.mark(n) }.firstOrNull(Board::won)?.unmarked()?.sum() ?: 0) }
        .filter { it > 0 }
        .first()

val Game.lastWinner: Int
    get() = numbers.asSequence()
        .map { n -> n * (boards.asSequence().filterNot(Board::won).map { b -> b.mark(n) }.lastOrNull(Board::won)?.unmarked()?.sum() ?: 0) }
        .filter { it > 0 }
        .last()

fun main() {

    fun part1(input: List<String>): Int = Game(parseBoards(input), parseNumbers(input)).firstWinner

    fun part2(input: List<String>): Int = Game(parseBoards(input), parseNumbers(input)).lastWinner

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day04")

    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
