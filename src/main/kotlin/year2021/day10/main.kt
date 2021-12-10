package year2021.day10

import java.io.File
import java.math.BigInteger

private enum class Brace (val start: Char, val end: Char) {
    NORMAL('(', ')'),
    SQUARE('[', ']'),
    CURLY('{', '}'),
    DIAMOND('<', '>');

    companion object {
        val startChars
            get() = values().map { it.start }.toCharArray()
        val endChars
            get() = values().map { it.end }.toCharArray()
    }
}

fun main() {
    val file = File("src/main/resources/2021/2021-day10.txt")
    val input = file.readLines()

    // Part 1: Corrupted lines
    val firstErrors = mutableListOf<Brace>()
    input.forEach { inputLine ->
        val stack = mutableListOf<Brace>()
        for ((index, char) in inputLine.withIndex()) {
            if (char in Brace.startChars) {
                val braceType = Brace.values().first { it.start == char }
                stack.add(braceType)
            } else {
                val braceType = Brace.values().first { it.end == char }
                if (stack.lastOrNull() == braceType) {
                    stack.removeLast()
                } else {
                    // syntax error
                    firstErrors.add(braceType)
                    break
                }
            }
        }
    }
    val part1Score = firstErrors.sumOf { braceType -> when (braceType) {
            Brace.NORMAL -> 3L
            Brace.SQUARE -> 57L
            Brace.CURLY -> 1197L
            Brace.DIAMOND -> 25137L
        }
    }
    println(part1Score)
    // Correct: 278475

    // Part 2: Incomplete lines
    val incompleteLineScores = mutableListOf<BigInteger>()
    inputLineLoop@ for (inputLine in input) {
        val stack = mutableListOf<Brace>()
        for (char in inputLine) {
            if (char in Brace.startChars) {
                val braceType = Brace.values().first { it.start == char }
                stack.add(braceType)
            } else {
                val braceType = Brace.values().first { it.end == char }
                if (stack.lastOrNull() == braceType) {
                    stack.removeLast()
                } else {
                    // syntax error
                    continue@inputLineLoop
                }
            }
        }
        if (stack.isNotEmpty()) {
            // Incomplete line
            val necessaryBraces = stack.reversed()
//            println(inputLine)
//            println(necessaryBraces.joinToString(separator = "") { it.end.toString() })
            var score = BigInteger.ZERO
            necessaryBraces.forEach { brace ->
                score = score.multiply(BigInteger.valueOf(5)).add(BigInteger.valueOf(when (brace) {
                    Brace.NORMAL -> 1
                    Brace.SQUARE -> 2
                    Brace.CURLY -> 3
                    Brace.DIAMOND -> 4
                }))
            }
            incompleteLineScores.add(score)
        }
    }
    val sorted = incompleteLineScores.sorted()
    val part2Score = sorted[sorted.size / 2]
    println(part2Score)
    // Correct: 3015539998

}
