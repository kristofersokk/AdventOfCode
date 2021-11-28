package year2020.day2

import java.io.File

fun main() {
    val file = File("src/main/resources/2020-day2.txt")
    val lines = file.readLines()

    val result1 = lines.count { line ->
        val (firstPart, secondPart, password) = line.split(" ")
        val (min, max) = firstPart.split("-").map { it.toInt() }
        val testableChar = secondPart[0]
        val charCount = password.count { it == testableChar }
        charCount in min..max
    }
    println("$result1 / ${lines.size}")

    val result2 = lines.count { line ->
        val (firstPart, secondPart, password) = line.split(" ")
        val (i, j) = firstPart.split("-").map { it.toInt() }
        val testableChar = secondPart[0]
        listOf(i, j).count { password[it - 1] == testableChar } == 1
    }
    println("$result2 / ${lines.size}")
}