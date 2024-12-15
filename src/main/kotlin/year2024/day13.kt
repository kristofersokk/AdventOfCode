package year2024

import java.io.File

fun main() {
    val file = File("src/main/resources/2024/2024-day13.txt")
    val text = file.readText()

    data class Machine(
        val ax: Long,
        val ay: Long,
        val bx: Long,
        val by: Long,
        val cx: Long,
        val cy: Long
    )

    data class Solution(val aCount: Long, val bCount: Long)

    fun bestSolution(machine: Machine, maxPresses: Long? = null): Solution? {
        val (ax, ay, bx, by, cx, cy) = machine
        val solutions = mutableListOf<Solution>()

        if (cx % ax == 0L && cy % ay == 0L) {
            val aCount1 = cx / ax
            val aCount2 = cy / ay
            if (aCount1 == aCount2) {
                solutions.add(Solution(aCount1, 0))
            }
        }

        if (cx % bx == 0L && cy % by == 0L) {
            val bCount1 = cx / bx
            val bCount2 = cy / by
            if (bCount1 == bCount2) {
                solutions.add(Solution(0, bCount1))
            }
        }

        val numerator = ax * (by * cx - bx * cy)
        val denominator = ax * by - ay * bx
        if (numerator % denominator == 0L) {
            val x = numerator / denominator
            if (x % ax == 0L) {
                val aCount = x / ax
                val bCount = (cx - x) / bx
                solutions.add(Solution(aCount, bCount))
            }
        }

        if (solutions.isEmpty()) {
            return null
        }

        return solutions
            .filter { maxPresses == null || (it.aCount < maxPresses && it.bCount < maxPresses) }
            .minByOrNull { 3 * it.aCount + it.bCount }
    }

    val machines = text.split("\r?\n\r?\n".toRegex())
        .map {
            val (splitALine, splitBLine, splitCLine) = it.split("\r?\n".toRegex())
                .map { line -> line.trim().split(", ") }
            Machine(
                splitALine[0].split("+")[1].toLong(),
                splitALine[1].split("+")[1].toLong(),
                splitBLine[0].split("+")[1].toLong(),
                splitBLine[1].split("+")[1].toLong(),
                splitCLine[0].split("=")[1].toLong(),
                splitCLine[1].split("=")[1].toLong()
            )
        }

//    machines.forEach { machine ->
//        println(machine)
//        val solution = bestSolution(machine, 100)
//        println(solution)
//    }

    val result1 = machines.sumOf { machine ->
        when (val solution = bestSolution(machine, 100)) {
            null -> 0
            else -> 3 * solution.aCount + solution.bCount
        }
    }

    println("Part 1: $result1")

    val machines2 = machines.map {
        it.copy(cx = it.cx + 10_000_000_000_000, cy = it.cy + 10_000_000_000_000)
    }

//    println()
//    machines2.forEach { machine ->
//        println(machine)
//        val solution = bestSolution(machine)
//        println(solution)
//    }

    val result2 = machines2.sumOf { machine ->
        when (val solution = bestSolution(machine)) {
            null -> 0
            else -> 3 * solution.aCount + solution.bCount
        }
    }

    println("Part 2: $result2")

}