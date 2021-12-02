package year2021.day2

import java.io.File

fun main() {
    val file = File("src/main/resources/2021/2021-day2.txt")
    val lines = file.readLines()

    var distance1 = 0
    var depth1 = 0
    lines.forEach { line ->
        val (direction, amountStr) = line.split(" ")
        val amount = amountStr.toInt()
        when (direction) {
            "up" -> depth1 -= amount
            "down" -> depth1 += amount
            "forward" -> distance1 += amount
        }
    }
    println(distance1)
    println(depth1)
    println(distance1 * depth1)

    var depth2 = 0
    var aim = 0
    var distance2 = 0
    lines.forEach { line ->
        val (direction, amountStr) = line.split(" ")
        val amount = amountStr.toInt()
        when (direction) {
            "up" -> aim -= amount
            "down" -> aim += amount
            "forward" -> {
                distance2 += amount
                depth2 += aim * amount
            }
        }
    }
    println(distance2)
    println(depth2)
    println(distance2 * depth2)
}
