package year2022.day18

import kotlin.math.abs

fun main() {
    val file = java.io.File("src/main/resources/2022/2022-day18.txt")
    val lines = file.readLines()

    // 2,2,2
    val cubes = lines.map { line ->
        val (x, y, z) = line.split(",").map { it.toInt() }
        Triple(x, y, z)
    }.toSet()

    val openSides = cubes.flatMap { cube ->
        val (x, y, z) = cube
        (-1..1).flatMap { dx ->
            (-1..1).flatMap { dy ->
                (-1..1).mapNotNull { dz ->
                    if (abs(dx) + abs(dy) + abs(dz) == 1 &&
                        Triple(x + dx, y + dy, z + dz) !in cubes
                    )
                        cube to Triple(dx, dy, dz)
                    else null
                }
            }
        }
    }

    println("Result1: ${openSides.size}")

    var surfaces = mutableListOf<MutableSet<Pair<Triple<Int, Int, Int>, Triple<Int, Int, Int>>>>()

    openSides.forEach { openSide ->
        val (openSideCube, openSideVector) = openSide
        val (dx, dy, dz) = openSideVector

        val connectedSides = mutableListOf(openSide)
        fun computeConnectedSides(
            startCube: Triple<Int, Int, Int>,
            mainVector: Triple<Int, Int, Int>,
            sideVector: Triple<Int, Int, Int>
        ) {
            if (startCube + mainVector + sideVector in cubes) {
                connectedSides.add(startCube + mainVector + sideVector to sideVector.inverted())
                return
            }
            if (startCube + sideVector in cubes) {
                connectedSides.add(startCube + sideVector to mainVector)
                return
            }
            connectedSides.add(startCube to sideVector)
        }

        // Find connected sides
        if (abs(dx) == 1) {
            (-1..1).forEach { ddy ->
                (-1..1).forEach { ddz ->
                    if (abs(ddy) + abs(ddz) == 1) {
                        computeConnectedSides(openSideCube, openSideVector, Triple(0, ddy, ddz))
                    }
                }
            }
        }
        if (abs(dy) == 1) {
            (-1..1).forEach { ddx ->
                (-1..1).forEach { ddz ->
                    if (abs(ddx) + abs(ddz) == 1) {
                        computeConnectedSides(openSideCube, openSideVector, Triple(ddx, 0, ddz))
                    }
                }
            }
        }
        if (abs(dz) == 1) {
            (-1..1).forEach { ddx ->
                (-1..1).forEach { ddy ->
                    if (abs(ddx) + abs(ddy) == 1) {
                        computeConnectedSides(openSideCube, openSideVector, Triple(ddx, ddy, 0))
                    }
                }
            }
        }

        // Indexes of surfaces where connected sides are already present
        val surfaceIndexes = connectedSides.mapNotNull { connectedSide ->
            surfaces.indexOfFirst { surface ->
                connectedSide in surface
            }.let { if (it == -1) null else it }
        }.distinct()

        // Check how many surfaces
        // 0, add new surface
        if (surfaceIndexes.isEmpty()) {
            surfaces.add(connectedSides.toMutableSet())
        }
        // 1, add to existing surface
        if (surfaceIndexes.size == 1) {
            surfaces[surfaceIndexes[0]].addAll(connectedSides)
        }
        // 2 or more, merge surfaces
        if (surfaceIndexes.size >= 2) {
            val mergedSurface = surfaceIndexes.map { surfaces[it] }.flatten().toMutableSet()
            mergedSurface.addAll(connectedSides)
            // Remove old surfaces
            surfaces = surfaces.filterIndexed { index, _ -> index !in surfaceIndexes }.toMutableList()
            // Add new merged surface
            surfaces.add(mergedSurface)
        }
    }

    println()
//    println(surfaces.size)
    surfaces.forEachIndexed { index, surface ->
        println("Surface $index: ${surface.size}")
    }
    val biggestSurface = surfaces.maxBy { it.size }.size
    println("Result2: $biggestSurface")

    // More accurate way to find the external surface
    val randomCube = cubes.first()
    val (chosenX, chosenY) = randomCube
    val cubesWithThatXAndY = cubes.filter { it.first == chosenX && it.second == chosenY }
    val maxZ = cubesWithThatXAndY.maxBy { it.third }.third
    val chosenCube = Triple(chosenX, chosenY, maxZ)
    val externalSide = chosenCube to Triple(0, 0, 1)
    val externalSurface = surfaces.first { externalSide in it }
    println("Alt result2: ${externalSurface.size}")

    println("Development time: 55m")
}

private operator fun Triple<Int, Int, Int>.plus(other: Triple<Int, Int, Int>) =
    Triple(first + other.first, second + other.second, third + other.third)

private fun Triple<Int, Int, Int>.inverted() =
    Triple(-first, -second, -third)
