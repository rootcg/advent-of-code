package day04

import readInput
import readTestInput

fun parseBoards(input: List<String>): List<Board> =
    input.drop(1)
        .filter(String::isNotEmpty)
        .chunked(5)
        .map { it.map { str -> str.split(" ").filterNot(String::isEmpty).map { c -> Square(c.toInt(), false) } } }

data class Square(val value: Int, var marked: Boolean)
typealias Board = List<List<Square>>

fun Board.unmarked() = flatMap { it.filterNot(Square::marked) }.map(Square::value)

fun putNumber(board: Board, n: Int): Int? {
    for (i in 0 until 5) {
        for (j in 0 until 5) {
            val x = board[i][j]
            if (x.value == n) {
                x.marked = true

                // check row
                val row = board[i].map { it.marked }.reduce(Boolean::and)
                if (row) return n * board.unmarked().sum()

                // check column
                val column =
                    board.map { it.filterIndexed { index, _ -> index == j }.first() }
                        .map { it.marked }.reduce(Boolean::and)

                if (column) return n * board.unmarked().sum()
            }
        }
    }

    return null
}

fun main() {
    fun part1(input: List<String>): Int {
        val numbers = input.first().split(",").map { it.toInt() }
        val boards = parseBoards(input)

        for (n in numbers) {
            for (board in boards) {
                for (i in 0..4) {
                    for (j in 0..4) {
                        val x = board[i][j]
                        if (x.value == n) {
                            x.marked = true

                            // check row
                            val row = board[i].map { it.marked }.reduce(Boolean::and)
                            if (row) return n * board.unmarked().sum()

                            // check column
                            val column =
                                board.map { it.filterIndexed { index, _ -> index == j }.first() }
                                    .map { it.marked }.reduce(Boolean::and)

                            if (column) return n * board.unmarked().sum()
                        }
                    }
                }
            }
        }

        throw IllegalStateException()
    }

    fun part2(input: List<String>): Int {
        data class Player(val board: Board, var playing: Boolean)

        val numbers = input.first().split(",").map { it.toInt() }
        val boards = parseBoards(input).toMutableList()
        val boardsTotal = boards.size

        val players = boards.map { Player(it, true) }

        var winsCounter = 0
        for (n in numbers) {
            players.filter { it.playing }.forEach { player ->
                val winningResult = putNumber(player.board, n)
                if (winningResult != null) {
                    player.playing = false
                    winsCounter++

                    if (winsCounter == boardsTotal) return winningResult
                }
            }
        }

        throw IllegalStateException()
    }

    // 12978

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day04")

    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
