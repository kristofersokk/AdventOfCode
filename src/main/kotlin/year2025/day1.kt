package year2025

import java.io.File
import kotlin.math.abs

fun main() {
    val file = File("src/main/resources/2025/2025-day1.txt")
    val lines = file.readLines()

    val result1 = lines.asSequence()
        .map { line ->
            val sign = when (line.first()) {
                'L' -> -1
                else -> 1
            }
            sign * line.drop(1).toInt()
        }
        .runningFold(50) { acc, i -> (acc + i + 100) % 100 }
        .filter { it == 0 }
        .count()

    println("result1: $result1")

    val result2 = lines.asSequence()
        .map { line ->
            val sign = when (line.first()) {
                'L' -> -1
                else -> 1
            }
            sign * line.drop(1).toInt()
        }
        .runningFold(50 to (0 to 0)) { (loc), offset ->
            val newLoc = loc + offset
            val count = abs(newLoc / 100) + (if (loc > 0 && newLoc <= 0) 1 else 0)
            val normalizedLoc = (newLoc % 100 + 100) % 100
            normalizedLoc to (offset to count)
        }
        .map { it.second.second }
        .sum()

    println("result2: $result2")
}