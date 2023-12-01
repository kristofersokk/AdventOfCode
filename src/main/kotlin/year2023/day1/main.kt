package year2023.day1

import java.io.File

fun main() {
    val file = File("src/main/resources/2023/2023-day1.txt")
    val lines = file.readLines()

    val result1 = lines.sumOf { line ->
        val first = line.first { it.isDigit() }.toString()
        val last = line.reversed().first { it.isDigit() }.toString()
        (first + last).toInt()
    }

    println("result1: $result1")

    val numberNamesToNumbers = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9",
    )

    fun String.fromWordToNumber() = numberNamesToNumbers[this] ?: this

    val matches = numberNamesToNumbers.keys + numberNamesToNumbers.values

    val result2 = lines.sumOf { line ->
        val first = line.findAnyOf(matches)!!.second.fromWordToNumber()
        val last = line.findLastAnyOf(matches)!!.second.fromWordToNumber()
        val solution = (first + last).toInt()
        solution
    }

    println("result2: $result2")
}