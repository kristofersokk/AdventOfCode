package year2024

import java.io.File

fun main() {
    val file = File("src/main/resources/2024/2024-day4.txt")
    val lines = file.readLines()

    val testStr = "XMAS"

    var count1 = 0
    for (x in lines.first().indices) {
        for (y in lines.indices) {
            for (dx in -1..1) {
                for (dy in -1..1) {
                    if (dx == 0 && dy == 0) continue
                    for (index in testStr.indices) {
                        val x1 = x + dx * index
                        val y1 = y + dy * index
                        if (x1 !in lines.first().indices || y1 !in lines.indices) break
                        if (lines[y1][x1] != testStr[index]) break
                        if (index == testStr.length - 1) count1++
                    }
                }
            }
        }
    }

    val result1 = count1
    println("Result 1: $result1")

    var count2 = 0
    for (x in lines.first().indices) {
        for (y in lines.indices) {
            if (x + 2 !in lines.first().indices || y + 2 !in lines.indices) continue
            if (lines[y + 1][x + 1] != 'A') continue
            if (listOf(lines[y][x], lines[y + 2][x + 2]).sorted() == listOf('M', 'S') &&
                listOf(lines[y][x + 2], lines[y + 2][x]).sorted() == listOf('M', 'S')
            ) {
                count2++
            }
        }
    }

    val result2 = count2
    println("Result 2: $result2")

}