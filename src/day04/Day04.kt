package day04

import readInput
import readTestInput

fun parseNumbers(input: List<String>) = input.first().split(",").map { it.toInt() }
fun parseBoards(input: List<String>): List<Board> = input.drop(1).filter(String::isNotEmpty).chunked(5).map { Board(it.map(::parseSquares)) }
fun parseSquares(input: String) = input.split(" ").filterNot(String::isEmpty).map { c -> Square(c.toInt(), false) }

data class Square(val value: Int, val marked: Boolean)

data class Board(private val squares: List<List<Square>>) {
    private val rows = squares
    private val columns = (0 until 5).map { j -> rows.map { row -> row[j] } }

    fun unmarked() = rows.flatMap { it.filterNot(Square::marked) }.map(Square::value)
    fun mark(n: Int) = Board(rows.map { row -> row.map { sq -> if (sq.value == n) sq.copy(marked = true) else sq } })
    fun won() = (completeRows() + completeColumns()).isNotEmpty()

    private fun completeRows() = rows.filter { it.map(Square::marked).reduce(Boolean::and) }
    private fun completeColumns() = columns.filter { it.map(Square::marked).reduce(Boolean::and) }
}

data class Game(val boards: List<Board>, val numbers: List<Int>) {
    val firstWinner: Int by lazy {
        playSequence().map { (bs, n) -> n * (bs.firstOrNull(Board::won)?.unmarked()?.sum() ?: 0) }.first { it > 0 }
    }

    val lastWinner: Int by lazy {
        playSequence().map { (bs, n) -> n * (bs.lastOrNull(Board::won)?.unmarked()?.sum() ?: 0) }.last { it > 0 }
    }

    private fun playSequence(): Sequence<Pair<List<Board>, Int>> {
        return generateSequence(Pair(boards.markAll(numbers[0]), 0)) { (lastBoards, i) ->
           numbers.getOrNull(i + 1)?.let { Pair(lastBoards.filterNot(Board::won).markAll(it), i + 1) }
        }.map { (bs, i) -> Pair(bs, numbers[i]) }
    }

    private fun List<Board>.markAll(n: Int) = map { it.mark(n) }
}

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
