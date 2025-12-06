package year2025

import java.io.File
import java.math.BigInteger

fun main() {
    val file = File("src/main/resources/2025/2025-day6.txt")
    val lines = file.readLines()

    val numberLines = lines.dropLast(1).map { it.trim().split("\\s+".toRegex()).map { it.toBigInteger() } }
    val operators = lines.last().trim().split("\\s+".toRegex()).map { it.first() }

    val result1 = numberLines.first().indices
        .map { x ->
            numberLines.map { it[x] }.reduce(
                when (operators[x]) {
                    '+' -> BigInteger::plus
                    else -> BigInteger::times
                }
            )
        }
        .reduce(BigInteger::plus)

    println("result1: $result1")

    val result2 = lines.first().indices.reversed()
        .flatMap { x -> lines.indices.map { y -> lines[y].getOrElse(x) { ' ' } } }
        .joinToString(separator = "")
        .split("(?<=[+|*])\\s+".toRegex())
        .map { problem ->
            problem.dropLast(1)
                .trim()
                .split("\\s+".toRegex())
                .map { it.toBigInteger() }
                .reduce(
                    when (problem.last()) {
                        '+' -> BigInteger::plus
                        else -> BigInteger::times
                    }
                )
        }
        .reduce(BigInteger::plus)

    println("result2: $result2")
}