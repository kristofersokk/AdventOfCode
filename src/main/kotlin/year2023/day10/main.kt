package year2023.day10

import java.io.File

fun main() {
    val file = File("src/main/resources/2023/2023-day10.txt")
    val lines = file.readLines()

    val start = findStart(lines)

    val validStartDirections = findValidStartDirections(start, lines)


    val distances = mutableMapOf(start to 0)
    validStartDirections.forEach { validStartDirection ->
        doOneLoop(start, validStartDirection, lines) { node, _, distance ->
            if (node != start && (node !in distances || distances[node]!! > distance)) {
                distances[node] = distance
            }
        }
    }

    val result1 = distances.values.max()
    println("Part 1: $result1")

    val (direction1, direction2) = validStartDirections
    val turningsOfDirection1 = findTurningsOfStartingDirection(start, direction1, lines)

    // Go around clockwise, hence want positive turnings
    val part2StartingDirection = if (turningsOfDirection1 >= 3) direction1 else direction2
    val loopSegments = distances.keys
    val innerSegments = mutableSetOf<Pair<Int, Int>>()

    fun startFillingInnerSegments(node: Pair<Int, Int>, direction: Direction) {
        var currentNode = node
        do {
            currentNode = advanceNodeByDirection(direction, currentNode)
            if (currentNode.first !in lines.indices || currentNode.second !in lines[currentNode.first].indices)
                break
            if (currentNode !in loopSegments)
                innerSegments.add(currentNode)
        } while (currentNode !in loopSegments)
    }

    doOneLoop(start, part2StartingDirection, lines) { node, direction, _ ->
        val char = lines[node.first][node.second]
        when (char) {
            'F' -> if (direction == Direction.SOUTH) {
                startFillingInnerSegments(node, Direction.WEST)
                startFillingInnerSegments(node, Direction.NORTH)
            }

            '7' -> if (direction == Direction.WEST) {
                startFillingInnerSegments(node, Direction.NORTH)
                startFillingInnerSegments(node, Direction.EAST)
            }

            'J' -> if (direction == Direction.NORTH) {
                startFillingInnerSegments(node, Direction.EAST)
                startFillingInnerSegments(node, Direction.SOUTH)
            }

            'L' -> if (direction == Direction.EAST) {
                startFillingInnerSegments(node, Direction.SOUTH)
                startFillingInnerSegments(node, Direction.WEST)
            }

            '-' -> if (direction == Direction.EAST) {
                startFillingInnerSegments(node, Direction.SOUTH)
            } else if (direction == Direction.WEST) {
                startFillingInnerSegments(node, Direction.NORTH)
            }

            '|' -> if (direction == Direction.NORTH) {
                startFillingInnerSegments(node, Direction.EAST)
            } else if (direction == Direction.SOUTH) {
                startFillingInnerSegments(node, Direction.WEST)
            }

            else -> {}
        }
    }

    val result2 = innerSegments.size

    println("Part 2: $result2")

}

private fun findTurningsOfStartingDirection(
    start: Pair<Int, Int>,
    startingDirection: Direction,
    lines: List<String>
): Int {
    var turnings = 0
    doOneLoop(start, startingDirection, lines) { node, direction, _ ->
        val char = lines[node.first][node.second]
        when (direction) {
            Direction.WEST -> when (char) {
                'J' -> turnings++
                '7' -> turnings--
            }

            Direction.EAST -> when (char) {
                'F' -> turnings++
                'L' -> turnings--
            }

            Direction.NORTH -> when (char) {
                'L' -> turnings++
                'J' -> turnings--
            }

            Direction.SOUTH -> when (char) {
                '7' -> turnings++
                'F' -> turnings--
            }

            else -> {}
        }
    }
    return turnings
}

private fun findValidStartDirections(
    start: Pair<Int, Int>,
    lines: List<String>
): MutableList<Direction> {
    val validStartDirections = mutableListOf<Direction>()
    if (start.first > 0 && lines[start.first - 1][start.second] in arrayOf('F', '7', '|')) {
        validStartDirections.add(Direction.NORTH)
    }
    if (start.first < lines.size - 1 && lines[start.first + 1][start.second] in arrayOf('J', 'L', '|')) {
        validStartDirections.add(Direction.SOUTH)
    }
    if (start.second > 0 && lines[start.first][start.second - 1] in arrayOf('F', 'L', '-')) {
        validStartDirections.add(Direction.WEST)
    }
    if (start.second < lines[start.first].length - 1 && lines[start.first][start.second + 1] in arrayOf(
            'J',
            '7',
            '-'
        )
    ) {
        validStartDirections.add(Direction.EAST)
    }
    return validStartDirections
}

private fun findStart(lines: List<String>): Pair<Int, Int> {
    var start = 0 to 0
    startloop@ for (i in lines.indices) {
        for (j in lines[i].indices) {
            if (lines[i][j] == 'S') {
                start = i to j
                break@startloop
            }
        }
    }
    return start
}

private fun doOneLoop(
    start: Pair<Int, Int>,
    startDirection: Direction,
    lines: List<String>,
    forEachNodeIndexed: (Pair<Int, Int>, Direction?, Int) -> Unit = { _, _, _ -> }
) {
    var currentNode = start
    var currentDirection: Direction? = startDirection
    var currentDistance = 0
    do {
        if (currentDirection == null) {
            println("Error, no direction")
            println("Current node: $currentNode")
            break
        }

        val newNode = advanceNodeByDirection(currentDirection, currentNode)
        if (newNode.first in lines.indices && newNode.second in lines[newNode.first].indices) {
            currentNode = newNode
        } else {
            println("Error, outside of map")
            println("Current node: $currentNode")
            println("Current direction: $currentDirection")
            break
        }
//            println("New node: $currentNode")
        currentDirection = when (lines[currentNode.first][currentNode.second]) {
            'F' -> when (currentDirection) {
                Direction.NORTH -> Direction.EAST
                Direction.WEST -> Direction.SOUTH
                else -> {
                    wrongDirection(currentNode, currentDirection)
                    break
                }
            }

            '7' -> when (currentDirection) {
                Direction.NORTH -> Direction.WEST
                Direction.EAST -> Direction.SOUTH
                else -> {
                    wrongDirection(currentNode, currentDirection)
                    break
                }
            }

            'J' -> when (currentDirection) {
                Direction.SOUTH -> Direction.WEST
                Direction.EAST -> Direction.NORTH
                else -> {
                    wrongDirection(currentNode, currentDirection)
                    break
                }
            }

            'L' -> when (currentDirection) {
                Direction.SOUTH -> Direction.EAST
                Direction.WEST -> Direction.NORTH
                else -> {
                    wrongDirection(currentNode, currentDirection)
                    break
                }
            }

            '|' -> when (currentDirection) {
                Direction.NORTH -> Direction.NORTH
                Direction.SOUTH -> Direction.SOUTH
                else -> {
                    wrongDirection(currentNode, currentDirection)
                    break
                }
            }

            '-' -> when (currentDirection) {
                Direction.EAST -> Direction.EAST
                Direction.WEST -> Direction.WEST
                else -> {
                    wrongDirection(currentNode, currentDirection)
                    break
                }
            }

            'S' -> null
            else -> {
                println("Error, invalid character")
                println("Current node: $currentNode")
                println("Current direction: $currentDirection")
                break
            }
        }
        currentDistance++
        forEachNodeIndexed(currentNode, currentDirection, currentDistance)
    } while (currentNode != start)
}

private fun wrongDirection(currentNode: Pair<Int, Int>, currentDirection: Direction?) {
    println("Error, invalid direction")
    println("Current node: $currentNode")
    println("Current direction: $currentDirection")
}

private fun advanceNodeByDirection(
    currentDirection: Direction,
    currentNode: Pair<Int, Int>
): Pair<Int, Int> {
    val newNode = when (currentDirection) {
        Direction.NORTH -> currentNode.first - 1 to currentNode.second
        Direction.EAST -> currentNode.first to currentNode.second + 1
        Direction.SOUTH -> currentNode.first + 1 to currentNode.second
        Direction.WEST -> currentNode.first to currentNode.second - 1
    }
    return newNode
}

private enum class Direction {
    NORTH, EAST, SOUTH, WEST
}
