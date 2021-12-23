package year2021.day22

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    val file = File("src/main/resources/2021/2021-day22.txt")
    val lines = file.readLines()
    val reactor = Array(101) { Array(101) { BooleanArray(101) { false } } }

    lines.forEach { line ->
        val (command, commandRest) = line.split(" ")
        val ranges = commandRest.split(",").map {
            it.split("=")[1].split("..").map { it.toInt() }.let {
                it[0]..it[1]
            }
        }
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
    val onCubes = reactor.sumOf { it.sumOf { it.count { it } } }
    println(onCubes)

    // Part 1
    // Correct: 582644

    // Part 2
    // Correct:
}
