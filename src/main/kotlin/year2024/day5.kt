package year2024

import java.io.File

fun main() {
    val file = File("src/main/resources/2024/2024-day5.txt")
    val text = file.readText()

    val parts = text.split("\r?\n\r?\n".toRegex())
    val rules = parts.first().split("\r?\n".toRegex()).map { it.trim() }.toSet()

    val updates = parts.last().split("\r?\n".toRegex())
        .filter { it.isNotBlank() }
        .map { it.trim().split(",").map { it.toInt() } }

    val result1 = updates.mapNotNull { update ->
        for (i in 0..<update.indices.last) {
            for (j in i + 1..update.indices.last) {
                if ("${update[j]}|${update[i]}" in rules) {
                    return@mapNotNull null
                }
            }
        }
        update[update.size / 2].toLong()
    }.sum()

    println("Result 1: $result1")

    val incorrectlyOrderedUpdates = updates.filter() { update ->
        for (i in 0..<update.indices.last) {
            for (j in i + 1..update.indices.last) {
                if ("${update[j]}|${update[i]}" in rules) {
                    return@filter true
                }
            }
        }
        return@filter false
    }

    val result2 = incorrectlyOrderedUpdates.sumOf { update ->
        val sorted = update.sortedWith { o1: Int, o2: Int ->
            when {
                "${o1}|${o2}" in rules -> -1
                "${o2}|${o1}" in rules -> 1
                else -> 0
            }
        }
        sorted[sorted.size / 2]
    }

    println("Result 2: $result2")

}