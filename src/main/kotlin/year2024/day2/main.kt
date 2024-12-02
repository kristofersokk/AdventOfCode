package year2024.day2

import java.io.File

fun main() {
    val file = File("src/main/resources/2024/2024-day2.txt")
    val lines = file.readLines()

    val reports = lines.map { it.split(" ").map { it.toInt() } }
    val result1 = reports.count { report ->
        val isPositive = report[1] - report[0] > 0
        val acceptableRange = when (isPositive) {
            true -> 1..3
            false -> -3..-1
        }
        (1 until report.size).all { i ->
            report[i] - report[i - 1] in acceptableRange
        }
    }

    println("Result 1: $result1")

    val result2 = reports.count { report ->
        arrayOf(1..3, -3..-1).any { acceptableRange ->
            val faultIndexes = mutableListOf<Int>()
            (1 until report.size).forEach { i ->
                if (report[i] - report[i - 1] !in acceptableRange) {
                    faultIndexes.add(i)
                    if (faultIndexes.size > 2) {
                        return@any false
                    }
                }
            }
            if (faultIndexes.size == 2) {
                return@any faultIndexes[1] - faultIndexes[0] == 1 &&
                    report[faultIndexes[1]] - report[faultIndexes[0] - 1] in acceptableRange
            }
            if (faultIndexes.size == 1) {
                val index = faultIndexes.first()
                return@any index == 1 ||
                    index == report.size - 1 ||
                    index > 1 && report[index] - report[index - 2] in acceptableRange ||
                    index < report.size - 1 && report[index + 1] - report[index - 1] in acceptableRange
            }
            true
        }
    }

    println("Result 2: ${result2}")
}