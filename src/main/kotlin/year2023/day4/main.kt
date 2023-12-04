package year2023.day4

import java.io.File

fun main() {
    val file = File("src/main/resources/2023/2023-day4.txt")
    val lines = file.readLines()

    val result1 = lines
        .sumOf { line ->
            val correctNumbers = getLineMatchesCount(line)
            if (correctNumbers == 0)
                return@sumOf 0L

            2.pow(correctNumbers - 1)
        }
    println("Result1: $result1")

    val scratchCardCounts = lines.indices.associateWith { 1 }.toMutableMap()
    lines.forEachIndexed { lineIndex, line ->
        val correctNumbers = getLineMatchesCount(line)
        if (correctNumbers > 0) {
            (1..correctNumbers).map { lineIndex + it }
                .filter { it < lines.size }
                .forEach { scratchCardCounts[it] = scratchCardCounts[it]!! + scratchCardCounts[lineIndex]!! }
        }
    }
    val result2 = scratchCardCounts.values.sum()
    println("Result2: $result2")
}

private fun getLineMatchesCount(line: String): Int {
    val parts = line.split(" ?[:|] +".toRegex())
    val winningNumbers = parts[1].split(" +".toRegex()).map { it.toInt() }
    val numbers = parts[2].split(" +".toRegex()).map { it.toInt() }
    return numbers.count { it in winningNumbers }
}

private fun Int.pow(i: Int): Long {
    var result = 1L
    repeat(i) {
        result *= this
    }
    return result
}
