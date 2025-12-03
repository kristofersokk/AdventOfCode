package year2025

import java.io.File

fun main() {
    val file = File("src/main/resources/2025/2025-day2.txt")
    val lines = file.readLines()

    val result1 = lines[0].split(',').sumOf {
        val segments = it.split("-")
        val (xStr, yStr) = segments
        val (x, y) = segments.map { it.toLong() }
        val start = if (xStr.length == 1) {
            1L
        } else if (xStr.length % 2 == 0) {
            xStr.take(xStr.length / 2).toLong()
        } else {
            "9".repeat(xStr.length / 2).toLong()
        }
        val end = if (yStr.length == 1) {
            1L
        } else if (yStr.length % 2 == 0) {
            yStr.take(yStr.length / 2).toLong() + 1
        } else {
            "9".repeat(yStr.length / 2).toLong()
        }
        (start..end).asSequence()
            .map {
                it.toString().repeat(2).toLong()
            }
            .filter { it in x..y }
            .sum()
    }

    println("result1: $result1")

    val result2 = lines[0].split(',')
        .flatMap {
            val segments = it.split("-")
            val (xStr, yStr) = segments
            val (x, y) = segments.map { it.toLong() }
            val end = if (yStr.length == 1) {
                1L
            } else if (yStr.length % 2 == 0) {
                yStr.take(yStr.length / 2).toLong() + 1
            } else {
                "9".repeat(yStr.length / 2).toLong()
            }
            (1..end).asSequence()
                .flatMap {
                    val str = it.toString()
                    val maxRepeat = (yStr.length + 1) / str.length
                    (2..maxRepeat).map { str.repeat(it).toLong() }
                }
                .filter { it in x..y }
                .toList()
        }
        .toSet()
        .toList()
        .sum()

    println("result2: $result2")

}