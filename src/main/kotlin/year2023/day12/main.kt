package year2023.day12

import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    val file = File("src/main/resources/2023/2023-day12.txt")
    val lines = file.readLines()

    val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

    val part1Time = measureTime {
        val result1 = lines.withIndex().sumOf { (index, line) ->
            val parts = line.split(" ")
            val row = parts[0]
            val groups = parts[1].split(",").map { it.toInt() }
            val arrangements = findArrangements(row, 0, groups, 0, cache)
//        println("${index + 1}: $row: $groups, $arrangements")
            arrangements
        }

        println("Part 1: $result1")
    }
    println("Part 1 time: $part1Time")

    println()

    val part2Time = measureTime {
        val result2 = lines.withIndex().sumOf { (index, line) ->
            val parts = line.split(" ")
            val row = (1..5).joinToString(separator = "?") { parts[0] }
            val groups = (1..5).joinToString(separator = ",") { parts[1] }
                .split(",").map { it.toInt() }
            val arrangements = findArrangements(row, 0, groups, 0, cache)
//        println("${index + 1}: $row: $groups, $arrangements")
            arrangements
        }

        println("Part 2: $result2")
    }

    println("Part 2 time: $part2Time")

}

private fun findArrangements(
    row: String,
    rowOffset: Int,
    groups: List<Int>,
    groupOffset: Int,
    cache: MutableMap<Pair<String, List<Int>>, Long>
): Long {
    if (groupOffset >= groups.size) {
        return 1
    }

    val currentGroupSize = groups[groupOffset]

    if (rowOffset + currentGroupSize > row.length) {
        return 0
    }

    val cacheKey = Pair(row.substring(rowOffset), groups.drop(groupOffset))
    if (cacheKey in cache) {
        return cache[cacheKey]!!
    }

    val indexOfNextDamaged = row.indexOf('#', rowOffset)
    val indexOfLastDamaged = row.substring(rowOffset).lastIndexOf('#').let {
        if (it == -1) -1 else it + rowOffset
    }

    val minStartIndex = max(
        rowOffset,
        if (groupOffset == groups.size - 1) indexOfLastDamaged - currentGroupSize + 1 else 0
    )
    val maxStartIndex = min(
        row.length - groups.drop(groupOffset).sum() - (groups.size - groupOffset - 1),
        if (indexOfNextDamaged != -1) indexOfNextDamaged else Int.MAX_VALUE
    )

    val arrangements = (minStartIndex..maxStartIndex).sumOf { startIndex ->
        val endIndex = startIndex + currentGroupSize - 1
        val currentRowSegment = row.substring(startIndex, endIndex + 1)
        val currentRowSegmentFits = currentRowSegment.all { it in arrayOf('#', '?') }
        val theresNecessarySpaceAfter =
            groupOffset == groups.size - 1 || (endIndex < row.length - 1 && row[endIndex + 1] in arrayOf('.', '?'))

        if (currentRowSegmentFits && theresNecessarySpaceAfter) {
            findArrangements(row, endIndex + 2, groups, groupOffset + 1, cache)
        } else {
            0
        }
    }

    cache[cacheKey] = arrangements

    return arrangements
}
