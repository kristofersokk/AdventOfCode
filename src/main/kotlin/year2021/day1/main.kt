package year2021.day1

import java.io.File

fun main() {
    val file = File("src/main/resources/2021/2021-day1.txt")
    val lines = file.readLines()

    val result1 = lines.zipWithNext().count { (a, b) -> b > a }
    println("result1: $result1")

    val result2 = lines.asSequence().map { it.toInt() }
        .zipWithNext()
        .zipWithNext { a, b -> arrayOf(a.first, a.second, b.second) }
        .map { it.sum() }
        .zipWithNext()
        .count { (a, b) -> b > a }
    println("result2: $result2")
}