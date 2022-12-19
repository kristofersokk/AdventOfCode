package year2022.day18

import java.io.File
import kotlin.math.abs

private typealias Vec3 = Triple<Int, Int, Int>

fun main() {
    val file = File("src/main/resources/2022/2022-day18.txt")
    val lines = file.readLines()

    // 2,2,2
    val cubes = lines.map { line ->
        val (x, y, z) = line.split(",").map { it.toInt() }
        Vec3(x, y, z)
    }.toSet()

    val openSides = cubes.flatMap { cube ->
        val (x, y, z) = cube
        (-1..1).flatMap { dx ->
            (-1..1).flatMap { dy ->
                (-1..1).mapNotNull { dz ->
                    if (abs(dx) + abs(dy) + abs(dz) == 1 &&
                        Vec3(x + dx, y + dy, z + dz) !in cubes
                    )
                        cube to Vec3(dx, dy, dz)
                    else null
                }
            }
        }
    }

    println("Result1: ${openSides.size}")

    var surfaces = mutableListOf<MutableSet<Pair<Vec3, Vec3>>>()

    openSides.forEach { openSide ->
        val (openSideCube, openSideVector) = openSide
        val (dx, dy, dz) = openSideVector

        val connectedSides = mutableListOf(openSide)
        fun computeConnectedSides(
            startCube: Vec3,
            mainVector: Vec3,
            sideVector: Vec3,
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
                        computeConnectedSides(openSideCube, openSideVector, Vec3(0, ddy, ddz))
                    }
                }
            }
        }
        if (abs(dy) == 1) {
            (-1..1).forEach { ddx ->
                (-1..1).forEach { ddz ->
                    if (abs(ddx) + abs(ddz) == 1) {
                        computeConnectedSides(openSideCube, openSideVector, Vec3(ddx, 0, ddz))
                    }
                }
            }
        }
        if (abs(dz) == 1) {
            (-1..1).forEach { ddx ->
                (-1..1).forEach { ddy ->
                    if (abs(ddx) + abs(ddy) == 1) {
                        computeConnectedSides(openSideCube, openSideVector, Vec3(ddx, ddy, 0))
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
    val chosenCube = Vec3(chosenX, chosenY, maxZ)
    val externalSide = chosenCube to Vec3(0, 0, 1)
    val externalSurface = surfaces.first { externalSide in it }
    println("Alt result2: ${externalSurface.size}")

    println("Development time: 55m")
}

private operator fun Vec3.plus(other: Vec3) =
    Vec3(first + other.first, second + other.second, third + other.third)

private fun Vec3.inverted() =
    Vec3(-first, -second, -third)
