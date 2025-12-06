package year2025

import java.io.File
import kotlin.math.max

fun main() {
    val file = File("src/main/resources/2025/2025-day5.txt")
    val lines = file.readLines()

    val ranges = lines.takeWhile { it.isNotEmpty() }.map { it.split('-').map { it.toLong() }.let { it[0]..it[1] } }
    val productIds = lines.drop(ranges.size + 1).map { it.toLong() }

    val result1 = productIds.count { productId -> ranges.any { productId in it } }

    println("result1: $result1")

    val joinedRanges = mutableListOf<LongRange>()
    ranges.sortedBy { it.first }.forEach { range ->
        if (joinedRanges.isEmpty() || range.first > joinedRanges.last().last + 1) {
            joinedRanges.add(range)
        } else {
            joinedRanges[joinedRanges.lastIndex] = joinedRanges.last().first..max(joinedRanges.last().last, range.last)
        }
    }
    val result2 = joinedRanges.sumOf { it.last - it.first + 1 }

    println("result2: $result2")
}