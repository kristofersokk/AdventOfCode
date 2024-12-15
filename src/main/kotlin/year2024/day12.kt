package year2024

import java.io.File

fun main() {
    val file = File("src/main/resources/2024/2024-day12.txt")
    val lines = file.readLines()

    data class Location(val x: Int, val y: Int)
    data class Region(val id: Char, val locations: MutableSet<Location> = mutableSetOf()) {
        val area: Int
            get() = locations.size

        val perimeter: Int
            get() {
                val locationsSet = locations.toSet()
                return locations.sumOf { location ->
                    val neighbors = arrayOf(
                        Location(location.x + 1, location.y),
                        Location(location.x - 1, location.y),
                        Location(location.x, location.y + 1),
                        Location(location.x, location.y - 1)
                    )
                    neighbors.count { it !in locationsSet }
                }
            }

        val perimeterWalls: Int
            get() {
                val locationsSet = locations.toSet()
                return locations.sumOf { location ->
                    var contributions = 0
                    if (
                        Location(location.x + 1, location.y) !in locationsSet &&
                        (Location(location.x, location.y - 1) !in locationsSet ||
                            Location(location.x + 1, location.y - 1) in locationsSet)
                    ) {
                        contributions++
                    }
                    if (
                        Location(location.x - 1, location.y) !in locationsSet &&
                        (Location(location.x, location.y - 1) !in locationsSet ||
                            Location(location.x - 1, location.y - 1) in locationsSet)
                    ) {
                        contributions++
                    }
                    if (
                        Location(location.x, location.y + 1) !in locationsSet &&
                        (Location(location.x - 1, location.y) !in locationsSet ||
                            Location(location.x - 1, location.y + 1) in locationsSet)
                    ) {
                        contributions++
                    }
                    if (
                        Location(location.x, location.y - 1) !in locationsSet &&
                        (Location(location.x - 1, location.y) !in locationsSet ||
                            Location(location.x - 1, location.y - 1) in locationsSet)
                    ) {
                        contributions++
                    }
                    contributions
                }
            }
    }

    val regions = mutableListOf<Region>()
    val regionsMap = mutableMapOf<Location, Region>()

    lines.forEachIndexed { y, line ->
        line.forEachIndexed inner@{ x, c ->
            val location = Location(x, y)
            if (location in regionsMap) return@inner
            val region = Region(c)
            regions.add(region)

            val newLocations = mutableSetOf(location)
            var perimeterLocations = setOf(location)
            while (perimeterLocations.isNotEmpty()) {
                val newPerimeterLocations = mutableSetOf<Location>()
                perimeterLocations.forEach { perimeterLocation ->
                    val neighbors = arrayOf(
                        Location(perimeterLocation.x + 1, perimeterLocation.y),
                        Location(perimeterLocation.x - 1, perimeterLocation.y),
                        Location(perimeterLocation.x, perimeterLocation.y + 1),
                        Location(perimeterLocation.x, perimeterLocation.y - 1)
                    ).filter { it.x in line.indices && it.y in lines.indices }
                    neighbors.forEach { neighbor ->
                        if (neighbor in newLocations) return@forEach
                        if (lines[neighbor.y][neighbor.x] == c) {
                            newPerimeterLocations += neighbor
                            newLocations += neighbor
                        }
                    }
                }
                perimeterLocations = newPerimeterLocations
            }

            region.locations.add(location)
            regionsMap[location] = region

            newLocations.forEach {
                regionsMap[it] = region
                region.locations.add(it)
            }
        }
    }

//    regions.forEach {
//        println("Region ${it.id} has ${it.locations.size} locations, area ${it.area}, perimeter ${it.perimeter}, perimeter walls ${it.perimeterWalls}")
//    }

    val result1 = regions.sumOf { region -> region.area * region.perimeter }

    println("Part 1: $result1")

    val result2 = regions.sumOf { region -> region.area * region.perimeterWalls }

    println("Part 2: $result2")
}