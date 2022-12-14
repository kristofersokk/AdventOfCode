package year2022.day14

private const val RUN_PART_2 = false

fun main() {
    val file = java.io.File("src/main/resources/2022/2022-day14.txt")
    val lines = file.readLines()

    val width = 1000
    val walls = mutableListOf<List<Pair<Int, Int>>>()
    lines.forEach { line ->
        val parts = line.split(" -> ")
        val vertices = parts.map { part ->
            val (x, y) = part.split(",").map { it.toInt() }
            x to y
        }.toMutableList()
        walls.add(vertices)
    }
    val height = walls.flatten().maxOf { it.second } + 4

    val grid = Array(height) { Array(width) { '.' } }
    grid[0][500] = '+'
    walls.forEach { wall ->
        wall.zipWithNext { v1, v2 ->
            if (v1.first == v2.first) {
                val y1 = minOf(v1.second, v2.second)
                val y2 = maxOf(v1.second, v2.second)
                for (y in y1..y2) {
                    grid[y][v1.first] = '#'
                }
            } else {
                val x1 = minOf(v1.first, v2.first)
                val x2 = maxOf(v1.first, v2.first)
                for (x in x1..x2) {
                    grid[v1.second][x] = '#'
                }
            }
        }
    }

    fun printGrid() {
        val minGrainLoc =
            maxOf(grid.minOf { it.indexOfFirst { it == 'O' }.let { if (it == -1) Int.MAX_VALUE else it } } - 10, 0)
        val maxGrainLoc = minOf(grid.maxOf { it.indexOfLast { it == 'O' } } + 10, width - 1)

        grid.forEach { println(it.joinToString("").substring(minGrainLoc..maxGrainLoc)) }
    }

    if (RUN_PART_2) {
        grid[height - 2] = grid[height - 2].map { '#' }.toTypedArray()
    }

//    grid.forEach { println(it.joinToString("")) }

    var grains = 0
    fun simulateGrain(): Boolean {
        var y = 0
        var x = 500
        if (grid[y][x] == 'O') {
            return false
        }

        fun tryToMoveGrain(): Boolean {
            if (grid[y + 1][x] == '.') {
                y++
                return true
            }
            if (grid[y + 1][x - 1] == '.') {
                y++
                x--
                return true
            }
            if (grid[y + 1][x + 1] == '.') {
                y++
                x++
                return true
            }
            return false
        }

        var simulateFurther = true
        while (simulateFurther) {
            simulateFurther = tryToMoveGrain()
//            println("x=$x, y=$y")
            if (y >= height - 1)
                return false
        }
        grid[y][x] = 'O'
//        println("Putting O at x=$x, y=$y")
        return true
    }

    var continueSimulating = true
    while (continueSimulating) {
        continueSimulating = simulateGrain()
        grains++
    }
    grains--

    printGrid()

    println("Grains: $grains")
    println("Width: $width, height: $height")

    println("Development time: 50m48s")
}
