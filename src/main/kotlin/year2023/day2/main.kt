package year2023.day2

import java.io.File

fun main() {
    val file = File("src/main/resources/2023/2023-day2.txt")
    val lines = file.readLines()

    val part1Limits = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14,
    )

    val result1 = lines.sumOf { line ->
        val parts = line.split(": ", "; ")
        val gameId = parts[0].split(" ")[1].toInt()
        parts.drop(1).forEach { part ->
            part.split(", ").forEach { colorPart ->
                val (countStr, color) = colorPart.split(" ")
                if (countStr.toInt() > part1Limits[color]!!) {
                    return@sumOf 0
                }
            }
        }
        gameId
    }

    println("result1: $result1")

    val result2 = lines.sumOf { line ->
        val parts = line.split(": ", "; ")
        val colorCounts = parts.drop(1).map { part ->
            part.split(", ").associate { colorPart ->
                val (countStr, color) = colorPart.split(" ")
                color to countStr.toInt()
            }
        }
        part1Limits.keys.map { color ->
            colorCounts.maxOf { it[color] ?: 0 }
        }.multiply()
    }

    println("result2: $result2")
}

private fun Iterable<Int>.multiply(): Long {
    var result = 1L
    for (i in this) {
        result *= i
    }
    return result
}
