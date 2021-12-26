package year2021.day22

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    val file = File("src/main/resources/2021/2021-day22.txt")
    val lines = file.readLines()

    val parsedLines = lines.map { line ->
        val (command, commandRest) = line.split(" ")
        val ranges = commandRest.split(",").map {
            it.split("=")[1].split("..").map { it.toInt() }.let {
                it[0]..it[1]
            }
        }
        command to ranges
    }

    // Part 1 solution
    val reactor = Array(101) { Array(101) { BooleanArray(101) { false } } }
    parsedLines.forEach { (command, ranges) ->
        if (
            ranges[0].first <= 50 &&
            ranges[0].last >= -50 &&
            ranges[1].first <= 50 &&
            ranges[1].last >= -50 &&
            ranges[2].first <= 50 &&
            ranges[2].last >= -50
        ) {
            val xRange = max(-50, ranges[0].first)..min(50, ranges[0].last)
            val yRange = max(-50, ranges[1].first)..min(50, ranges[1].last)
            val zRange = max(-50, ranges[2].first)..min(50, ranges[2].last)
            val newBoolean = command == "on"
            for (x in xRange) {
                for (y in yRange) {
                    reactor[x + 50][y + 50].fill(newBoolean, zRange.first + 50, zRange.last + 51)
                }
            }
        }
    }
    val onCubes1 = reactor.sumOf { it.sumOf { it.count { it } } }
    println(onCubes1)

    // part 2 solution
    val laylines = (0 until 3).map { rangeIndex ->
        parsedLines.flatMap { (_, ranges) ->
            val range = ranges[rangeIndex]
            listOf(range.first, range.last + 1)
        }.toSet().toList().sorted()
    }
    laylines.forEach(::println)
    val zippedLayLines = laylines.map { it.zipWithNext() }
    var onCubes2 = 0L
    zippedLayLines[0].forEachIndexed { index, xRange ->
        println("$index/${zippedLayLines[0].size}")
        zippedLayLines[1].forEach { yRange ->
            zippedLayLines[2].forEach { zRange ->
                val x = xRange.first
                val y = yRange.first
                val z = zRange.first
                val isRegionOn = parsedLines.lastOrNull { (_, ranges) ->
                    x in ranges[0] && y in ranges[1] && z in ranges[2]
                }?.first == "on"
                if (isRegionOn) {
                    onCubes2 += (xRange.second - xRange.first).toLong() *
                        (yRange.second - yRange.first).toLong() *
                        (zRange.second - zRange.first).toLong()
                }
            }
        }
    }
    println(onCubes2)

    // Part 1
    // Correct: 582644

    // Part 2
    // Correct: 1263804707062415
}
