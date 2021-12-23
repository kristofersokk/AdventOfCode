package year2021.day23

import java.io.File
import kotlin.math.max

private fun getCost(amphipod: Char): Int = when (amphipod) {
    'A' -> 1
    'B' -> 10
    'C' -> 100
    'D' -> 1000
    else -> 0
}

private data class Situation(
    val rooms: List<MutableList<Char?>>,
    val corridor: MutableList<Char?> = MutableList(11) { null }
) {
    fun deepCopy(): Situation =
        Situation(
            rooms.map { it.toMutableList() },
            corridor.map { it }.toMutableList()
        )

    fun print() {
        println("#".repeat(13))
        println("#${corridor.joinToString(separator = "") { (it ?: '.').toString() }}#")
        (rooms[0].size - 1 downTo 0).forEach { index ->
            println(
                "###${
                    rooms.map { it[index] ?: '.' }.joinToString(separator = "#")
                }###"
            )
        }
        println("  ${"#".repeat(9)}  ")
    }
}

private val IntRange.size
    get() = max(0, last - first + 1)

private fun findBestOrganization(
    currentSituation: Situation,
    energySpent: Int,
    procedure: List<String>,
    level: Int = 0,
    setNewOrganization: (Pair<Int, List<String>>) -> Unit,
) {
    val (rooms, corridor) = currentSituation
    if (corridor.all { it == null } &&
        rooms.withIndex().all { (index, room) ->
            room.all { it == Char(65 + index) }
        }
    ) {
        setNewOrganization(energySpent to procedure)
        return
    }
    if (level == 0) {
        println()
        currentSituation.print()
    }

    corridor.withIndex().filter { it.value != null }.forEach { (charLoc, char) ->
        val roomIndex = char!!.code - 65
        val roomLoc = 2 + 2 * roomIndex
        val room = rooms[roomIndex]
        val way = if (charLoc < roomLoc) charLoc + 1..roomLoc else roomLoc until charLoc
        val wayFree = corridor.subList(way.first, way.last + 1).all { it == null }
        val roomFree = room.all { it == null || it == char }
        if (wayFree && roomFree) {
            val newLocInRoom = (room.withIndex().lastOrNull { (_, ch) ->
                ch != null
            }?.index ?: -1) + 1
            val journeyLength = way.size + room.size - newLocInRoom
            val newEnergy = energySpent + journeyLength * getCost(char)
            val newSituation = currentSituation.deepCopy()
            newSituation.corridor[charLoc] = null
            newSituation.rooms[roomIndex][newLocInRoom] = char
            if (level <= 2) {
                println("${"  ".repeat(level)}Level$level: Corridor[$charLoc] - $char")
            }
            findBestOrganization(
                newSituation,
                newEnergy,
                procedure + "$char: move from corridor-$charLoc to room-$roomIndex, energy: ${
                    journeyLength * getCost(
                        char
                    )
                }",
                level + 1,
                setNewOrganization
            )
        }

    }
    rooms.forEachIndexed { roomIndex, room ->
        if (room.any { it != null }) {
            val (lastAmphInRoomIndex, lastAmphInRoom) = room.withIndex().last { (_, ch) ->
                ch != null
            }
            val roomLoc = 2 + 2 * roomIndex
            val roomCorrespondingChar = Char(65 + roomIndex)
//            println(listOf(roomIndex, room, lastAmphInRoomIndex, lastAmphInRoom, roomCorrespondingChar))
            if (lastAmphInRoom != null && room.any { it != roomCorrespondingChar && it != null }) {
                listOf(0, 1, 3, 5, 7, 9, 10).forEach { destination ->
                    val way = if (destination > roomLoc) roomLoc..destination else destination..roomLoc
                    val wayFree = corridor.subList(way.first, way.last + 1).all { it == null }
                    if (wayFree) {
                        val journeyLength = way.size + room.size - 1 - lastAmphInRoomIndex
                        val newEnergy = energySpent + journeyLength * getCost(lastAmphInRoom)
                        val newSituation = currentSituation.deepCopy()
                        newSituation.rooms[roomIndex][lastAmphInRoomIndex] = null
                        newSituation.corridor[destination] = lastAmphInRoom
                        if (level <= 2) {
                            println("${"  ".repeat(level)}Level$level: Rooms[$roomIndex] - $lastAmphInRoom - Dest: $destination")
                        }
                        findBestOrganization(
                            newSituation,
                            newEnergy,
                            procedure + "$lastAmphInRoom: move from room-$roomIndex to corridor-$destination, energy: ${
                                journeyLength * getCost(
                                    lastAmphInRoom
                                )
                            }",
                            level + 1,
                            setNewOrganization
                        )
                    }
                }
            }
        }
    }
}

fun main() {
    val file = File("src/main/resources/2021/2021-day23.txt")
    val lines = file.readLines().map { it.trim() }
    val startRoomUppers = lines[2].split("#+".toRegex()).filter { it.isNotBlank() }.map { it[0] }
    val startRoomLowers = lines[3].split("#+".toRegex()).filter { it.isNotBlank() }.map { it[0] }

//    val startingSituation1 = Situation(
//        startRoomLowers.indices.map { mutableListOf(startRoomLowers[it], startRoomUppers[it]) }
//    )
//    var bestOrganization1: Pair<Int, List<String>> = Int.MAX_VALUE to listOf()
//    findBestOrganization(startingSituation1, 0, listOf()) { newOrganization: Pair<Int, List<String>> ->
//        if (newOrganization.first < bestOrganization1.first) {
//            bestOrganization1 = newOrganization
//        }
//    }
//    println(bestOrganization1.first)
//    bestOrganization1.second.forEach(::println)


    val foldedPart = listOf(listOf('D', 'B', 'A', 'C'), listOf('D', 'C', 'B', 'A'))
    val startingSituation2 = Situation(
        startRoomLowers.indices.map {
            mutableListOf(
                startRoomLowers[it],
                foldedPart[0][it],
                foldedPart[1][it],
                startRoomUppers[it]
            )
        }
    )
    var bestOrganization2: Pair<Int, List<String>> = Int.MAX_VALUE to listOf()
    findBestOrganization(startingSituation2, 0, listOf()) { newOrganization: Pair<Int, List<String>> ->
        if (newOrganization.first < bestOrganization2.first) {
            bestOrganization2 = newOrganization
        }
    }
    println(bestOrganization2.first)
    bestOrganization2.second.forEach(::println)

    // Part 1
    // Correct: 11332

    // Part 2
    // Correct: 49936
}
