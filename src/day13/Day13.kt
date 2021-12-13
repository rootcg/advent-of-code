package day13

import readInput
import readTestInput

enum class FoldDirection {
    UP, LEFT;

    companion object {
        fun of(direction: String): FoldDirection {
            return when (direction) {
                "y" -> UP
                "x" -> LEFT
                else -> throw IllegalStateException("unknown fold direction")
            }
        }
    }
}

interface Foldable<T> {
    fun foldUp(position: Int): T
    fun foldLeft(position: Int): T
}

class FoldInstruction(private val direction: FoldDirection, private val position: Int) {
    fun <T> apply(foldable: Foldable<T>): T = when (direction) {
        FoldDirection.UP -> foldable.foldUp(position)
        FoldDirection.LEFT -> foldable.foldLeft(position)
    }
}

data class Hole(val x: Int, val y: Int)

fun parseElfPaper(input: List<String>): ElfPaper =
    input.takeWhile(String::isNotEmpty)
        .map { it.split(",").map(String::toInt) }
        .map { (x, y) -> Hole(x, y) }
        .toSet()
        .let(::ElfPaper)

fun parseFoldInstructions(input: List<String>): List<FoldInstruction> =
    input.takeLastWhile(String::isNotEmpty)
        .map { it.split(" ").last().split("=") }
        .map { (direction, position) -> FoldInstruction(FoldDirection.of(direction), position.toInt()) }

class ElfPaper(private val holes: Set<Hole>) : Foldable<ElfPaper> {
    val holesCount = holes.size

    override fun foldUp(position: Int): ElfPaper =
        ElfPaper(
            holes.filter { (_, y) -> y > position }
                .groupBy { (_, y) -> y }
                .toSortedMap()
                .values
                .flatMap { holes -> holes.map { hole -> hole.copy(y = hole.y - (hole.y - position) * 2) } }
                .toSet()
                .plus(holes.filter { (_, y) -> y < position })
        )

    override fun foldLeft(position: Int): ElfPaper = rotate().foldUp(position).rotate()

    fun print() {
        val rows = holes.maxOf { (_, y) -> y }
        val cols = holes.maxOf { (x, _) -> x }
        for (i in 0..rows) {
            for (j in 0..cols) {
                if (holes.any { (x, y) -> y == i && x == j }) print('#')
                else print('.')
            }
            println()
        }
    }

    private fun rotate(): ElfPaper = ElfPaper(holes.map { (x, y) -> Hole(y, x) }.toSet())
}

fun main() {

    fun part1(input: List<String>): Int {
        val paper = parseElfPaper(input)
        val foldInstructions = parseFoldInstructions(input)
        return foldInstructions.first().apply(paper).holesCount
    }

    fun part2(input: List<String>) {
        val paper = parseElfPaper(input)
        val foldInstructions = parseFoldInstructions(input)
        foldInstructions.fold(paper) { acc, foldInstruction -> foldInstruction.apply(acc) }.print()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day13")
    check(part1(testInput) == 17)
//    check(part2(testInput) == 1)

    val input = readInput("Day13")
    println(part1(input))
    part2(input)
}