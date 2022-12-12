package year2022.day12

fun main() {
    val file = java.io.File("src/main/resources/2022/2022-day12.txt")
    val lines = file.readLines()
    println(lines)

    val startCoordinates = lines.withIndex().first { "S" in it.value }.let {
        it.value.indexOf("S") to it.index
    }
    val endCoordinates = lines.withIndex().first { "E" in it.value }.let {
        it.value.indexOf("E") to it.index
    }
    val width = lines[0].length
    val height = lines.size

    data class Loc(
        val x: Int,
        val y: Int,
        val letter: Char,
        var direction: LocationDirection? = null,
        var distanceFromEnd: Int? = null,
    )

    val grid = (0 until height).map { y ->
        (0 until width).map { x ->
            Loc(x, y, lines[y][x])
        }.toTypedArray()
    }.toTypedArray()
    grid.forEach { gridLine ->
        println(gridLine.map { it.letter }.joinToString(""))
    }
    println()

    val startLoc = grid[startCoordinates.second][startCoordinates.first]
    val endLoc = grid[endCoordinates.second][endCoordinates.first]

    val needToVisit: MutableList<Loc> = mutableListOf(Loc(endLoc.x, endLoc.y, 'E', null, 0))
    while (needToVisit.isNotEmpty()) {
        val visiting = needToVisit.removeFirst()

        for (dx in -1..1) {
            for (dy in -1..1) {
                if ((dx + dy) % 2 == 0) continue
                val x = visiting.x + dx
                val y = visiting.y + dy
                if (x < 0 || y < 0 || y >= lines.size || x >= lines[y].length) continue
                val loc = grid[y][x]

                fun calculatedLetter(ch: Char) = when (ch) {
                    'E' -> 'z'
                    'S' -> 'a'
                    else -> ch
                }
                if (calculatedLetter(loc.letter).code - calculatedLetter(visiting.letter).code >= -1 &&
                    (loc.distanceFromEnd ?: Int.MAX_VALUE) > visiting.distanceFromEnd!! + 1
                ) {
                    loc.distanceFromEnd = visiting.distanceFromEnd!! + 1
                    loc.direction = when {
                        dx == 0 && dy == -1 -> LocationDirection.S
                        dx == 0 && dy == 1 -> LocationDirection.N
                        dx == -1 && dy == 0 -> LocationDirection.E
                        dx == 1 && dy == 0 -> LocationDirection.W
                        else -> throw IllegalStateException()
                    }

                    needToVisit.add(loc)
                }
            }
        }
    }

    grid.forEach { gridLine ->
        println(gridLine.map { it.letter }.joinToString(""))
    }
    println()

    grid.forEach { gridLine ->
        println(gridLine.map {
            if (it.x to it.y == endLoc.x to endLoc.y) {
                return@map 'E'
            }
            when (it.direction) {
                LocationDirection.N -> '^'
                LocationDirection.S -> 'v'
                LocationDirection.E -> '>'
                LocationDirection.W -> '<'
                else -> '.'
            }
        }.joinToString(""))
    }
    println()

    fun printPath(inputStartLoc: Loc) {
        var loc = inputStartLoc
        val stepsNeededVisited = mutableSetOf(endLoc, loc)
        while (loc != endLoc) {
            val dx = when (loc.direction) {
                LocationDirection.E -> 1
                LocationDirection.W -> -1
                else -> 0
            }
            val dy = when (loc.direction) {
                LocationDirection.N -> -1
                LocationDirection.S -> 1
                else -> 0
            }
            loc = grid[loc.y + dy][loc.x + dx]
            stepsNeededVisited.add(loc)
        }

        grid.forEach { gridLine ->
            println(gridLine.map {
                if (it.x to it.y == endLoc.x to endLoc.y) {
                    return@map 'E'
                }
                if (it !in stepsNeededVisited) {
                    return@map '.'
                }
                when (it.direction) {
                    LocationDirection.N -> '^'
                    LocationDirection.S -> 'v'
                    LocationDirection.E -> '>'
                    LocationDirection.W -> '<'
                    else -> '.'
                }
            }.joinToString(""))
        }
        println()
    }

    printPath(startLoc)

    println("Result1: ${startLoc.distanceFromEnd}")

    val trailBeginningLoc = grid.flatMap { it.toList() }.filter {
        it.letter in listOf('a', 'S')
    }.minBy { it.distanceFromEnd ?: Int.MAX_VALUE }

    println()
    printPath(trailBeginningLoc!!)

    println("Result2: ${trailBeginningLoc.distanceFromEnd}")

    println("Development time: 1h25m27s")
}

enum class LocationDirection {
    N, E, S, W
}
