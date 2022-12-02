package year2022.day2

import java.io.File

fun main() {
    val file = File("src/main/resources/2022/2022-day2.txt")
    val lines = file.readLines()

    var result1 = 0
    lines.forEach { line ->
        val (a, b) = line.split(" ")
        result1 += when (a) {
            "A" -> when (b) {
                "X" -> 4
                "Y" -> 8
                "Z" -> 3
                else -> 0
            }

            "B" -> when (b) {
                "X" -> 1
                "Y" -> 5
                "Z" -> 9
                else -> 0
            }

            "C" -> when (b) {
                "X" -> 7
                "Y" -> 2
                "Z" -> 6
                else -> 0
            }

            else -> 0
        }
    }
    println("Result1: $result1")

    var result2 = 0
    lines.forEach { line ->
        val (a, b) = line.split(" ")
        result2 += when (a) {
            "A" -> when (b) {
                "X" -> 3
                "Y" -> 4
                "Z" -> 8
                else -> 0
            }

            "B" -> when (b) {
                "X" -> 1
                "Y" -> 5
                "Z" -> 9
                else -> 0
            }

            "C" -> when (b) {
                "X" -> 2
                "Y" -> 6
                "Z" -> 7
                else -> 0
            }

            else -> 0
        }
    }
    println("Result2: $result2")
}