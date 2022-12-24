package year2022.day21

import java.io.File

fun main() {
    val file = File("src/main/resources/2022/2022-day21.txt")
    val lines = file.readLines()


    val monkeys = mutableMapOf<String, Monkey>()
    val monkeyListeners = mutableMapOf<String, MutableList<String>>()
    lines.forEach { line ->
        val parts = line.split(": ")
        val name = parts[0]
        val rightParts = parts[1].split(" ")
        val monkey = if (rightParts.size == 1) {
            val value = rightParts[0].toLong()
            Monkey(name, value, listOf())
        } else {
            val dependants = listOf(rightParts[0], rightParts[2])
            val operation = when (rightParts[1]) {
                "+" -> Operation.ADD
                "-" -> Operation.SUBTRACT
                "*" -> Operation.MULTIPLY
                "/" -> Operation.DIVIDE
                else -> throw Exception("Unknown operation")
            }
            dependants.forEach { dependant ->
                if (dependant in monkeyListeners) {
                    monkeyListeners[dependant]!!.add(name)
                } else {
                    monkeyListeners[dependant] = mutableListOf(name)
                }
            }
            Monkey(name, null, dependants, operation)
        }
        monkeys[name] = monkey
    }

    var monkeysToProcess = monkeys.values.filter { it.value != null }.toSet()
    while (monkeysToProcess.isNotEmpty()) {
        val newMonkeysToProcess = mutableSetOf<Monkey>()
        monkeysToProcess.forEach { monkey ->
            val listeners = monkeyListeners[monkey.name] ?: listOf()
            listeners.forEach { listener ->
                val listenerMonkey = monkeys[listener]!!
                val oldValue = listenerMonkey.value
                if (oldValue == null &&
                    listenerMonkey.dependants.map { monkeys[it]!! }.all { it.value != null }) {
                    val value1 = monkeys[listenerMonkey.dependants[0]]!!.value!!
                    val value2 = monkeys[listenerMonkey.dependants[1]]!!.value!!
                    listenerMonkey.value = when (listenerMonkey.operation!!) {
                        Operation.ADD -> value1 + value2
                        Operation.SUBTRACT -> value1 - value2
                        Operation.MULTIPLY -> value1 * value2
                        Operation.DIVIDE -> value1 / value2
                    }
                    newMonkeysToProcess.add(listenerMonkey)
                }
            }
        }
        monkeysToProcess = newMonkeysToProcess
    }

//    println(monkeys["root"])
    val result1 = monkeys["root"]!!.value
    println("Result 1: $result1")

    println()
    println(monkeys["humn"])
    println(monkeyListeners["humn"])

    // Part 2
    val dependOnHuman = mutableSetOf("humn")
    var currentMonkeys = setOf("humn")
    while (currentMonkeys.isNotEmpty()) {
        val newMonkeys = mutableSetOf<String>()
        currentMonkeys.forEach { monkey ->
            val listeners = monkeyListeners[monkey] ?: listOf()
            listeners.forEach { listener ->
                dependOnHuman.add(listener)
                newMonkeys.add(listener)
            }
        }
        currentMonkeys = newMonkeys
    }
    println(dependOnHuman)

    var currentMonkey = monkeys["root"]!!
    var currentMonkeyShouldHaveValue = currentMonkey.value
    while (currentMonkey.name != "humn") {
        println(currentMonkey)
        val firstDependant = currentMonkey.dependants[0]
        val secondDependant = currentMonkey.dependants[1]
        if (currentMonkey.name == "root") {
            if (firstDependant in dependOnHuman) {
                currentMonkeyShouldHaveValue = monkeys[secondDependant]!!.value
                currentMonkey = monkeys[firstDependant]!!
            } else {
                currentMonkeyShouldHaveValue = monkeys[firstDependant]!!.value
                currentMonkey = monkeys[secondDependant]!!
            }
        } else if (firstDependant in dependOnHuman) {
            currentMonkeyShouldHaveValue = when (currentMonkey.operation) {
                Operation.ADD -> currentMonkeyShouldHaveValue!! - monkeys[secondDependant]!!.value!!
                Operation.SUBTRACT -> currentMonkeyShouldHaveValue!! + monkeys[secondDependant]!!.value!!
                Operation.MULTIPLY -> currentMonkeyShouldHaveValue!! / monkeys[secondDependant]!!.value!!
                Operation.DIVIDE -> currentMonkeyShouldHaveValue!! * monkeys[secondDependant]!!.value!!
                else -> throw Exception("Unknown operation")
            }
            currentMonkey = monkeys[firstDependant]!!
        } else {
            currentMonkeyShouldHaveValue = when (currentMonkey.operation) {
                Operation.ADD -> currentMonkeyShouldHaveValue!! - monkeys[firstDependant]!!.value!!
                Operation.SUBTRACT -> monkeys[firstDependant]!!.value!! - currentMonkeyShouldHaveValue!!
                Operation.MULTIPLY -> currentMonkeyShouldHaveValue!! / monkeys[firstDependant]!!.value!!
                Operation.DIVIDE -> monkeys[firstDependant]!!.value!! / currentMonkeyShouldHaveValue!!
                else -> throw Exception("Unknown operation")
            }
            currentMonkey = monkeys[secondDependant]!!
        }
        if (firstDependant in dependOnHuman && secondDependant in dependOnHuman) {
            throw Exception("Both dependants depend on human")
        }
    }

    println(currentMonkey)
    println(currentMonkeyShouldHaveValue)

    println()
    println("Result 2: ${currentMonkeyShouldHaveValue!!}")


    println("Development time: 55m")
}

private enum class Operation {
    ADD, SUBTRACT, MULTIPLY, DIVIDE
}

private data class Monkey(
    val name: String,
    var value: Long?,
    val dependants: List<String>,
    val operation: Operation? = null,
)
