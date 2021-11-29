package year2020.day7

import java.io.File
import java.math.BigInteger

data class Bag(val name: String, val containedBags: Map<String, Int>)

fun main() {
    val file = File("src/main/resources/2020-day7.txt")
    val lines = file.readLines()
    val bags = mutableMapOf<String, Bag>()
    lines.forEach { line ->
        val (firstPart, secondPart) = line.split("contain")
        val bag = firstPart.split(" bags")[0]
        val containedBags = if ("other" in secondPart) {
            mutableMapOf()
        } else {
            val bagStrings = secondPart.split(",")
                .map { it.split(" bag")[0] }
            bagStrings.associate {
                it.split("\\s*\\d+\\s+".toRegex())[1] to
                    ("\\d+".toRegex().find(it)?.value?.toInt() ?: 1)
            }
        }
        bags[bag] = Bag(bag, containedBags)
        println("$bag, ${bags[bag]}")
    }

    val search1 = "shiny gold"
    val result1 = mutableSetOf<String>()
    while (true) {
        val newResults = bags.filterKeys { it !in result1 }
            .filterValues { bag -> bag.containedBags.keys.any { it == search1 || it in result1 } }
            .keys
        if (newResults.isEmpty()) {
            break
        }
        result1.addAll(newResults)
    }
    println(result1.size)
    println(result1)

    val search2 = "shiny gold"
    val result2 = mutableMapOf<String, BigInteger>()
    fun getAmountOfBags(bagName: String): BigInteger {
        if (bagName in result2) {
            return result2[bagName] ?: BigInteger.ZERO
        }
        val bag = bags[bagName]
        bag ?: return BigInteger.ONE
        val result = BigInteger.ONE.add(bag.containedBags.map { (bagName, count) ->
            val countPerBag = getAmountOfBags(bagName)
            countPerBag.multiply(count.toBigInteger())
        }.fold(BigInteger.ZERO) { a, b -> a.add(b) })
        result2[bagName] = result
        return result
    }
    println(getAmountOfBags(search2).toLong() - 1)
    result2.forEach { (t, u) -> println("$t: $u") }

}