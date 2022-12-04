package year2022.day3

import java.io.File

fun main() {
    val file = File("src/main/resources/2022/2022-day3.txt")
    val lines = file.readLines()

    var result1 = 0
    lines.forEach { line ->
        val firstHalf = line.substring(0, line.length / 2)
        val secondHalf = line.substring(line.length / 2)
        val commonLetter = firstHalf.toSet().intersect(secondHalf.toSet()).first()
        val prio = when (commonLetter) {
            in 'a'..'z' -> 1 + commonLetter.code - 'a'.code
            else -> 27 + commonLetter.code - 'A'.code
        }
        result1 += prio
    }

    println("Part 1: $result1")

    var result2 = 0
    lines.chunked(3).forEach { chunkedLines ->
        val (a, b, c) = chunkedLines
        val commonLetter = a.toSet().intersect(b.toSet()).intersect(c.toSet()).first()
        val prio = when (commonLetter) {
            in 'a'..'z' -> 1 + commonLetter.code - 'a'.code
            else -> 27 + commonLetter.code - 'A'.code
        }
        result2 += prio
    }

    println("Part 2: $result2")

}