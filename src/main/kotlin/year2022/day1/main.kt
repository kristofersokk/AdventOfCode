package year2022.day1

import java.io.File

fun main() {
    val file = File("src/main/resources/2022/2022-day1.txt")
    val lines = file.readLines()

    val sums = mutableListOf(0)
    lines.forEach { line ->
        if (line.isBlank()) {
            sums.add(0)
        } else {
            sums[sums.lastIndex] += line.toInt()
        }
    }

    println("result1: ${sums.maxOrNull()}")

    val sortedElves = sums.sorted().reversed()
    sortedElves.take(3).sum().let { println("result2: $it") }
}