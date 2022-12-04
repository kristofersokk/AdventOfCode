package year2022.day3

import java.io.File

fun main() {
    val file = File("src/main/resources/2022/2022-day4.txt")
    val lines = file.readLines()

    var result1 = 0
    lines.forEach { line ->
        val (a, b) = line.split(",")
        val (a1, a2) = a.split("-").map { it.toInt() }
        val (b1, b2) = b.split("-").map { it.toInt() }
        if (b1 >= a1 && b2 <= a2 || a1 >= b1 && a2 <= b2)
            result1++
    }

    println("Result 1: $result1")

    var result2 = 0
    lines.forEach { line ->
        val (a, b) = line.split(",")
        val (a1, a2) = a.split("-").map { it.toInt() }
        val (b1, b2) = b.split("-").map { it.toInt() }
        if (!(b2 < a1 || b1 > a2))
            result2++
    }

    println("Result 1: $result2")

}