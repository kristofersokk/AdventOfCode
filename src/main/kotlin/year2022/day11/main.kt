package year2022.day11

import java.math.BigInteger

fun main() {
    val file = java.io.File("src/main/resources/2022/2022-day11.txt")
    val fileParts = file.readText().split("\r?\n\r?\n".toRegex())
    fileParts.forEach {
        println(it)
        println()
    }
    println(fileParts.size)

    run {
        val monkeys = mutableListOf<Monkey>()
        fileParts.forEach { part ->
            val lines = part.split("\r?\n".toRegex())
            val monkeyIndex = lines[0].substringAfter(" ").substringBefore(":").toInt()
            val items = lines[1].substringAfter(": ").split(", ").map { it.toInt() }.toMutableList()
            val operationStrs = lines[2].substringAfter("new = ").split(" ")
            println(operationStrs)
            val operation = when (operationStrs[1]) {
                "*" -> { old: Int ->
                    val left = if (operationStrs[0] == "old") old else operationStrs[0].toInt()
                    val right = if (operationStrs[2] == "old") old else operationStrs[2].toInt()
                    left * right
                }

                "+" -> { old: Int ->
                    val left = if (operationStrs[0] == "old") old else operationStrs[0].toInt()
                    val right = if (operationStrs[2] == "old") old else operationStrs[2].toInt()
                    left + right
                }

                else -> throw IllegalArgumentException("Unknown operation: ${operationStrs[1]}")
            }
            val divisibilityTest = lines[3].substringAfter("by ").toInt()
            val divisibilityTrueMonkeyIndex = lines[4].substringAfter("monkey ").toInt()
            val divisibilityFalseMonkeyIndex = lines[5].substringAfter("monkey ").toInt()
            monkeys.add(
                Monkey(
                    monkeyIndex,
                    operation,
                    divisibilityTest,
                    divisibilityTrueMonkeyIndex,
                    divisibilityFalseMonkeyIndex,
                    items
                )
            )
        }

        monkeys.forEach { monkey ->
            println(monkey)
        }
        println()

        (1..20).forEach { round ->
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    val newWorry = monkey.operation(item) / 3
                    if (newWorry % monkey.divisibilityTest == 0) {
                        monkeys[monkey.divisibilityTrueMonkeyIndex].items.add(newWorry)
                    } else {
                        monkeys[monkey.divisibilityFalseMonkeyIndex].items.add(newWorry)
                    }
                }
                monkey.interactions += monkey.items.size
                monkey.items.clear()
            }
            println("After round $round:")
            monkeys.forEach { monkey ->
                println(monkey)
            }
            println()
        }

        val result1 = monkeys.map { it.interactions }.sortedDescending().take(2).map { it.toBigInteger() }.multiply()

        println("Result 1: $result1")
    }

    println()
    println()

    run {
        val monkeys = mutableListOf<Monkey2>()
        fileParts.forEach { part ->
            val lines = part.split("\r?\n".toRegex())
            val monkeyIndex = lines[0].substringAfter(" ").substringBefore(":").toInt()
            val operationStrs = lines[2].substringAfter("new = ").split(" ")
            println("\"${operationStrs[2]}\"")
            val operation = when (operationStrs[1]) {
                "*" ->
                    if (operationStrs[2] == "old") {
                        { monkeyItem: MonkeyItem -> monkeyItem.multiplyByItself() }
                    } else {
                        { monkeyItem: MonkeyItem -> monkeyItem.multiplyBy(operationStrs[2].toInt()) }
                    }

                "+" ->
                    { monkeyItem: MonkeyItem -> monkeyItem.addBy(operationStrs[2].toInt()) }

                else -> throw IllegalArgumentException("Unknown operation: ${operationStrs[1]}")
            }
            val divisibilityTest = lines[3].substringAfter("by ").toInt()
            val divisibilityTrueMonkeyIndex = lines[4].substringAfter("monkey ").toInt()
            val divisibilityFalseMonkeyIndex = lines[5].substringAfter("monkey ").toInt()

            monkeys.add(
                Monkey2(
                    monkeyIndex,
                    operation,
                    divisibilityTest,
                    divisibilityTrueMonkeyIndex,
                    divisibilityFalseMonkeyIndex
                )
            )
        }
        val divisers = monkeys.map { it.divisibilityTest }.toSet().toList()

        fileParts.forEachIndexed { partIndex, part ->
            val lines = part.split("\r?\n".toRegex())
            val itemsInts = lines[1].substringAfter(": ").split(", ").map { it.toInt() }
            val items = itemsInts.map { MonkeyItem(it, divisers) }.toMutableList()
            monkeys[partIndex].items = items
        }

        monkeys.forEach { monkey ->
            println(monkey)
        }
        println()

        for (round in 1..10000) {
            for (monkey in monkeys) {
                monkey.items.forEachIndexed { itemIndex, item ->
                    monkey.operation(item)
                    if (item.divisionTestsModulos[monkey.divisibilityTest] == 0) {
                        monkeys[monkey.divisibilityTrueMonkeyIndex].items.add(item)
                    } else {
                        monkeys[monkey.divisibilityFalseMonkeyIndex].items.add(item)
                    }
                }
                monkey.interactions += monkey.items.size
                monkey.items.clear()
            }
            if (round in listOf(1, 20, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000)) {
                println("After round $round:")
                monkeys.forEach { monkey ->
                    println(monkey)
                }
                println()
            }
        }

        val result2 = monkeys.map { it.interactions }.sortedDescending().take(2).map { it.toBigInteger() }.multiply()

        println("Result 2: $result2")
    }

    println("Development time: 3h3m10s")
}

private fun Iterable<BigInteger>.multiply(): BigInteger {
    var result = BigInteger.ONE
    this.forEach { result *= it }
    return result
}

private data class Monkey(
    val index: Int,
    val operation: (Int) -> Int,
    val divisibilityTest: Int,
    val divisibilityTrueMonkeyIndex: Int,
    val divisibilityFalseMonkeyIndex: Int,
    var items: MutableList<Int> = mutableListOf(),
    var interactions: Int = 0,
)

private data class Monkey2(
    val index: Int,
    val operation: (MonkeyItem) -> Unit,
    val divisibilityTest: Int,
    val divisibilityTrueMonkeyIndex: Int,
    val divisibilityFalseMonkeyIndex: Int,
    var interactions: Int = 0,
    var items: MutableList<MonkeyItem> = mutableListOf(),
)

private class MonkeyItem(initValue: Int, divisionTests: List<Int>) {
    val divisionTestsModulos = mutableMapOf<Int, Int>()

    init {
        divisionTests.forEach { divisionTest ->
            divisionTestsModulos[divisionTest] = initValue % divisionTest
        }
    }

    fun addBy(value: Int) {
        divisionTestsModulos.forEach { (divisionTest, modulo) ->
            divisionTestsModulos[divisionTest] = (modulo + value) % divisionTest
        }
    }

    fun multiplyBy(value: Int) {
        divisionTestsModulos.forEach { (divisionTest, modulo) ->
            divisionTestsModulos[divisionTest] = (modulo * value) % divisionTest
        }
    }

    fun multiplyByItself() {
        divisionTestsModulos.forEach { (divisionTest, modulo) ->
            divisionTestsModulos[divisionTest] = (modulo * modulo) % divisionTest
        }
    }

    override fun toString(): String {
        return divisionTestsModulos.toString()
    }
}