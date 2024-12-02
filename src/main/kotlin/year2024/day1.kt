package year2024

import java.io.File
import kotlin.math.abs

fun main() {
    val file = File("src/main/resources/2024/2024-day1.txt")
    val lines = file.readLines()

    val pairs = lines.map { it.split(" +".toRegex()).map { it.toInt() } }
    val left = pairs.map { it[0] }.sorted()
    val right = pairs.map { it[1] }.sorted()
    val diffSum = left.zip(right).sumOf { abs(it.second - it.first) }

    println("result1: $diffSum")

    val leftCounts = left.groupingBy { it }.eachCount()
    val rightCounts = right.groupingBy { it }.eachCount()

    val result2 = leftCounts.keys.intersect(rightCounts.keys)
        .sumOf { it * (leftCounts[it] ?: 0) * (rightCounts[it] ?: 0) }

    println("result2: $result2")
}