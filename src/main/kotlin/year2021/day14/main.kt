package year2021.day14

import java.io.File

fun main() {
    val file = File("src/main/resources/2021/2021-day14.txt")
    val input = file.readLines().map { it.trim() }
    val polymerizations = input.drop(2).associate {
        val (aa, b) = it.split(" -> ")
        aa[0] to aa[1] to b[0]
    }

    // Part 1
    val start = input[0]
    val firstChar = start.first()
    val lastChar = start.last()
    var charPairCounts = start.zipWithNext().groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    repeat(40) {
        val newCharPairCounts = mutableMapOf<Pair<Char, Char>, Long>()
        charPairCounts.forEach { (a, b), count ->
            val middle = polymerizations[a to b]!!
            newCharPairCounts.merge(a to middle, count, Long::plus)
            newCharPairCounts.merge(middle to b, count, Long::plus)
        }
        charPairCounts = newCharPairCounts
    }
    val charCounts = mutableMapOf<Char, Long>()
    charPairCounts.forEach { (a, b), count ->
        charCounts.merge(a, count, Long::plus)
        charCounts.merge(b, count, Long::plus)
    }
    charCounts.merge(firstChar, 1, Long::plus)
    charCounts.merge(lastChar, 1, Long::plus)
    charCounts.keys.forEach {
        charCounts[it] = charCounts[it]!! / 2
    }

    println(charCounts)
    val result = charCounts.maxOf { it.value } -
        charCounts.minOf { it.value }
    println(result)
    println()

    // Part 1
    // Correct: 2797

    // Part 2
    // Correct: 2926813379532
}
