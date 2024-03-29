import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(year: String, name: String) = File("src/aoc_$year", "${name.lowercase()}/$name.txt").readLines()

fun readTestInput(year: String, name: String) = File("src/aoc_$year", "${name.lowercase()}/${name}_test.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
