package year2023.day8

import java.io.File

fun main() {
    val file = File("src/main/resources/2023/2023-day8.txt")
    val lines = file.readLines()

    val directions = lines[0].trim()

    val forks = lines.drop(2).associate { line ->
        val parts = line.split(" = (", ", ", ")")
        parts[0] to (parts[1] to parts[2])
    }

    val result1 = "AAA".solve(directions, forks) { it == "ZZZ" }

    println("Part 1: $result1")

    val result2 = forks.keys
        .filter { it.endsWith('A') }
        .map {
            it.solve(directions, forks) {
                it.endsWith('Z')
            }
        }.reduce(::lcm)

    println("Part 2: $result2")
}

private fun lcm(l1: Long, l2: Long) =
    l1 * l2 / gcd(l1, l2)

private fun gcd(l1: Long, l2: Long): Long {
    val (larger, smaller) = if (l1 > l2) l1 to l2 else l2 to l1
    if (larger % smaller == 0L) return smaller
    if (smaller == 1L) return 1L
    return gcd(smaller, larger % smaller)
}

private fun String.solve(
    directions: String,
    forks: Map<String, Pair<String, String>>,
    predicate: (String) -> Boolean
): Long {
    var result = 0L
    var currentNode = this

    while (!predicate(currentNode)) {
        currentNode = if (directions[(result % directions.length).toInt()] == 'L')
            forks[currentNode]!!.first
        else
            forks[currentNode]!!.second
        result++
    }

    return result
}
