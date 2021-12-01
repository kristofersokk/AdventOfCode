package year2020.day5

import java.io.File

fun main() {
    val file = File("src/main/resources/2020/2020-day5.txt")
    val lines = file.readLines()
    val boardingPasses = lines.map { line ->
        val r = line.substring(0 until 7)
            .replace('F', '0')
            .replace('B', '1')
            .toInt(2)
        val c = line.substring(7)
            .replace('L', '0')
            .replace('R', '1')
            .toInt(2)
        val id = 8 * r + c
        id
    }.toSet()
    println(boardingPasses.maxOf { it })

    for (i in 0 until 8 * 128) {
        if (i !in boardingPasses) {
            println(i)
        }
    }

}