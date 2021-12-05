package year2021.day5

import java.io.File
import kotlin.math.abs

private data class InputLine(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int,
) {
    companion object {
        fun fromList(map: List<Int>): InputLine {
            val (x1, y1, x2, y2) = map
            return InputLine(x1, y1, x2, y2)
        }

    }
}

private fun sortedRange(start: Int, end: Int): IntRange =
    if (start <= end) start..end else end..start

fun main() {
    val file = File("src/main/resources/2021/2021-day5.txt")
    val lines = file.readLines()

    val inputLines = lines.map {
        InputLine.fromList(it.split(" -> ").map { it.split(",") }.flatten().map { it.toInt() })
    }
    val maxX = (inputLines.map { it.x1 } + inputLines.map { it.x2 }).maxOf { it }
    val maxY = (inputLines.map { it.y1 } + inputLines.map { it.y2 }).maxOf { it }
    val map = Array(maxY + 1) {
        IntArray(maxX + 1) { 0 }
    }

    inputLines.forEach { inputLine ->
        when {
            inputLine.x1 == inputLine.x2 -> {
                val x = inputLine.x1
                sortedRange(inputLine.y1, inputLine.y2).forEach { y ->
                    map[y][x] += 1
                }
            }
            inputLine.y1 == inputLine.y2 -> {
                val y = inputLine.y1
                sortedRange(inputLine.x1, inputLine.x2).forEach { x ->
                    map[y][x] += 1
                }
            }
            else -> {
                val (x1, y1, x2, y2) = inputLine
                val dx = if (x1 <= x2) 1 else -1
                val dy = if (y1 <= y2) 1 else -1
                (0..abs(x1 - x2)).forEach { d ->
                    val x = x1 + dx * d
                    val y = y1 + dy * d
                    map[y][x] += 1
                }
            }
        }
    }
    map.forEach {
        println(it.joinToString(separator = "").replace('0', '.'))
    }
    val twoOrMore = map.flatMap { it.asList() }.count { it >= 2 }
    println(twoOrMore)


}
