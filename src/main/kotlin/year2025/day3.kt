package year2025

import java.io.File

fun main() {
    val file = File("src/main/resources/2025/2025-day3.txt")
    val lines = file.readLines()

    val result1 = lines.sumOf { line ->
        val nums = line.map { it.digitToInt() }
        val biggestNumber = nums.reversed().asSequence().drop(1).max()
        val biggestIndexIndex = nums.indexOfFirst { it == biggestNumber }
        val secondNumber = nums.asSequence().drop(biggestIndexIndex + 1).max()
        10 * biggestNumber + secondNumber
    }

    println("result1: $result1")

    val result2 = lines.sumOf { line ->
        val nums = line.map { it.digitToInt() }

        fun populate(existingNums: IntArray, searchStartIndex: Int): IntArray? {
            if (existingNums.size == 12) return existingNums
            if (searchStartIndex >= nums.size) return null
            (9 downTo 1).asSequence().forEach { searchNum ->
                val searchNumIndex = nums.asSequence().drop(searchStartIndex).indexOf(searchNum)
                if (searchNumIndex >= 0) {
                    val result = populate(existingNums + searchNum, searchStartIndex + searchNumIndex + 1)
                    if (result != null) return result
                }
            }
            return null
        }

        val result = populate(intArrayOf(), 0) ?: intArrayOf(0)
        result.joinToString(separator = "").toLong()
    }

    println("result2: $result2")


}