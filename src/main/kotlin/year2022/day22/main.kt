package year2022.day22

import java.io.File

fun main() {
    val file = File("src/main/resources/2022/2022-day22.txt")
    val lines = file.readLines()

    val gridStrs = lines.take(lines.size - 2)
    val instructionsString = lines.last()
    val instructions = mutableListOf<Instruction>()

    var index = 0
    var stepBuilder = ""
    while (index < instructionsString.length) {
        when (val ch = instructionsString[index]) {
            'L' -> {
                if (stepBuilder.isNotEmpty()) {
                    instructions.add(Instruction(null, stepBuilder.toInt()))
                    stepBuilder = ""
                }
                instructions.add(Instruction(Direction.LEFT, null))
            }

            'R' -> {
                if (stepBuilder.isNotEmpty()) {
                    instructions.add(Instruction(null, stepBuilder.toInt()))
                    stepBuilder = ""
                }
                instructions.add(Instruction(Direction.RIGHT, null))
            }

            else -> {
                stepBuilder += ch
            }
        }
        index++
    }
    if (stepBuilder.isNotEmpty()) {
        instructions.add(Instruction(null, stepBuilder.toInt()))
        stepBuilder = ""
    }
//    instructions.forEach { println(it) }

    val rowRanges = gridStrs.map {
        val firstChar = it.indexOfFirst { it != ' ' }
        val lastChar = it.indexOfLast { it != ' ' }
        firstChar..lastChar
    }
    val colRanges = (0 until gridStrs.maxOf { it.length }).map { x ->
        val colStr = String(gridStrs.map { it.getOrElse(x) { ' ' } }.toCharArray())
        val firstChar = colStr.indexOfFirst { it != ' ' }
        val lastChar = colStr.indexOfLast { it != ' ' }
        firstChar..lastChar
    }

    val minX = 0
    val maxX = rowRanges.maxOf { it.last }
    val minY = 0
    val maxY = colRanges.maxOf { it.last }

    val beenToLocs = mutableMapOf<Pair<Int, Int>, Direction>()

    var loc = rowRanges[0].first to 0
    var dir = Direction.RIGHT
    instructions.forEach { instruction ->
        when (instruction.direction) {
            Direction.LEFT -> dir = when (dir) {
                Direction.UP -> Direction.LEFT
                Direction.LEFT -> Direction.DOWN
                Direction.DOWN -> Direction.RIGHT
                Direction.RIGHT -> Direction.UP
            }

            Direction.RIGHT -> dir = when (dir) {
                Direction.UP -> Direction.RIGHT
                Direction.RIGHT -> Direction.DOWN
                Direction.DOWN -> Direction.LEFT
                Direction.LEFT -> Direction.UP
            }

            null -> {
                for (i in 0 until instruction.steps!!) {
                    var newLoc = when (dir) {
                        Direction.UP -> loc.first to loc.second - 1
                        Direction.RIGHT -> loc.first + 1 to loc.second
                        Direction.DOWN -> loc.first to loc.second + 1
                        Direction.LEFT -> loc.first - 1 to loc.second
                    }
                    if (newLoc.first < minX) {
                        newLoc = rowRanges[newLoc.second].last to newLoc.second
                    } else if (newLoc.first > maxX) {
                        newLoc = rowRanges[newLoc.second].first to newLoc.second
                    } else if (newLoc.second < minY) {
                        newLoc = newLoc.first to colRanges[newLoc.first].last
                    } else if (newLoc.second > maxY) {
                        newLoc = newLoc.first to colRanges[newLoc.first].first
                    } else if (dir in arrayOf(Direction.LEFT, Direction.RIGHT)) {
                        if (newLoc.first < rowRanges[newLoc.second].first) {
                            newLoc = rowRanges[newLoc.second].last to newLoc.second
                        } else if (newLoc.first > rowRanges[newLoc.second].last) {
                            newLoc = rowRanges[newLoc.second].first to newLoc.second
                        }
                    } else if (newLoc.second < colRanges[newLoc.first].first) {
                        newLoc = newLoc.first to colRanges[newLoc.first].last
                    } else if (newLoc.second > colRanges[newLoc.first].last) {
                        newLoc = newLoc.first to colRanges[newLoc.first].first
                    }
                    if (gridStrs[newLoc.second][newLoc.first] == '#') {
//                        println("Hit a wall at $newLoc")
                        break
                    }
                    beenToLocs[loc] = dir
                    loc = newLoc
                }
            }

            else -> throw IllegalStateException()
        }
    }

    println()

    (0 until maxY).forEach { y ->
        (0 until maxX).forEach { x ->
            val loc = x to y
            val dir = beenToLocs[loc]
            if (dir != null) {
                print(
                    when (dir) {
                        Direction.UP -> '^'
                        Direction.RIGHT -> '>'
                        Direction.DOWN -> 'v'
                        Direction.LEFT -> '<'
                    }
                )
            } else {
                print(gridStrs[y].getOrElse(x) { ' ' })
            }
        }
        println()
    }
    println()
    println("Been to ${beenToLocs.size} locations")
    println("Final location: $loc")
    println("Final direction: $dir")

    val result1 = 1000 * (loc.second + 1) + 4 * (loc.first + 1) + when (dir) {
        Direction.RIGHT -> 0
        Direction.DOWN -> 1
        Direction.LEFT -> 2
        Direction.UP -> 3
    }
    println("Result 1: $result1")

}

private data class Instruction(
    val direction: Direction?,
    val steps: Int?,
)

private enum class Direction {
    UP, DOWN, LEFT, RIGHT
}
