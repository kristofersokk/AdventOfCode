package year2022.day6

import java.io.File

fun main() {
    val file = File("src/main/resources/2022/2022-day6.txt")
    val lines = file.readLines()

    val line = lines.first()
    val result1 = line.asSequence()
        .windowed(4)
        .withIndex()
        .filter { it.value.toSet().size == 4 }
        .first().index + 4

    println("Result1: $result1")

    val result2 = line.asSequence()
        .windowed(14)
        .withIndex()
        .filter { it.value.toSet().size == 14 }
        .first().index + 14

    println("Result2: $result2")

    println("Development time: 5m49s")
}