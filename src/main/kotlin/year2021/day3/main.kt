package year2021.day3

import java.io.File

fun main() {
    val file = File("src/main/resources/2021/2021-day3.txt")
    val lines = file.readLines().map { it.trim() }

    val length = lines[0].length

    val gammaStr = (0 until length).mapNotNull { index ->
        lines.map { it[index] }
            .groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
    }.joinToString(separator = "")
    val gamma = gammaStr.toInt(2)
    val sum = 2L.toPower(gammaStr.length) - 1
    val epsilon = sum - gamma
    val result1 = gamma * epsilon
    println(gamma)
    println(epsilon)
    println(result1)

    var oxygenList = lines.toList()
    var oxygenIndex = 0
    while (oxygenList.size > 1) {
        val counts = oxygenList.map { it[oxygenIndex] }
            .groupingBy { it }
            .eachCount()
        val commonChar = if (counts['1']!! >= counts['0']!!) '1' else '0'
        oxygenList = oxygenList.filter { it[oxygenIndex] == commonChar }
        oxygenIndex++
    }
    val oxygen = oxygenList[0].toInt(2)

    var co2List = lines.toList()
    var co2Index = 0
    while (co2List.size > 1) {
        val counts = co2List.map { it[co2Index] }
            .groupingBy { it }
            .eachCount()
        val rareChar = if (counts['1']!! >= counts['0']!!) '0' else '1'
        co2List = co2List.filter { it[co2Index] == rareChar }
        co2Index++
    }
    val co2 = co2List[0].toInt(2)
    val result2 = oxygen * co2

    println(oxygen)
    println(co2)
    println(result2)
}

private fun Long.toPower(power: Int): Long {
    var result = 1L
    repeat(power) {
        result *= this
    }
    return result
}
