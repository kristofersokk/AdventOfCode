package year2021.day12

import java.io.File

private data class Node(
    val id: String,
    val neighbours: MutableSet<String>,
) {
    val type: NodeType
        get() = when {
            id == "start" -> NodeType.START
            id == "end" -> NodeType.END
            id[0].isLowerCase() -> NodeType.SMALL
            else -> NodeType.BIG
        }
}

private enum class NodeType {
    START,
    END,
    SMALL,
    BIG,
}

fun main() {
    val file = File("src/main/resources/2021/2021-day12.txt")
    val input = file.readLines()

    val nodes = mutableMapOf<String, Node>()
    input.forEach { inputLine ->
        val (first, second) = inputLine.trim().split("-")
        if (first !in nodes) {
            nodes[first] = Node(first, mutableSetOf())
        }
        if (second !in nodes) {
            nodes[second] = Node(second, mutableSetOf())
        }
        nodes[first]!!.neighbours.add(second)
        nodes[second]!!.neighbours.add(first)
    }

    // Part 1
    var paths = 0L
    var visitedLowers = mutableMapOf("start" to 1)
    var currentVisit = "start"
    var path = mutableListOf("start")
    fun algorithm(allowSmallCavesTwice: Boolean = false) {
        val maxSmallCavesVisits = if (allowSmallCavesTwice) 2 else 1
        if (currentVisit == "end") {
//            println(path.joinToString(separator = ","))
            paths++
            return
        }
        val neighbours = nodes[currentVisit]!!.neighbours.filter {
            it != "start" && (it !in visitedLowers || (visitedLowers[it]!! < maxSmallCavesVisits &&
                visitedLowers.values.none { it == maxSmallCavesVisits }))
        }
        neighbours.forEach {
            val node = nodes[it]!!
            val isSmall = node.type == NodeType.SMALL
            if (isSmall) {
                if (it in visitedLowers) {
                    visitedLowers[it] = visitedLowers[it]!! + 1
                } else {
                    visitedLowers[it] = 1
                }
            }
            path.add(it)
            currentVisit = it
            algorithm(allowSmallCavesTwice)
            if (isSmall) {
                if (it in visitedLowers && visitedLowers[it]!! > 1) {
                    visitedLowers[it] = visitedLowers[it]!! - 1
                } else {
                    visitedLowers.remove(it)
                }
            }
            path.removeLast()
        }
    }
    algorithm()
    println(paths)
    println()
    // Correct: 3738

    // Part 2
    paths = 0L
    visitedLowers = mutableMapOf("start" to 1)
    currentVisit = "start"
    path = mutableListOf("start")
    algorithm(true)
    println(paths)
    // Correct: 120506

}
