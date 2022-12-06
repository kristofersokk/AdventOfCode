package year2022.day5

import java.io.File

fun main() {
    val file = File("src/main/resources/2022/2022-day5.txt")
    val lines = file.readLines()

    run {
        val stackLines = lines.take(8)
        val stacks = mutableListOf<MutableList<Char>>()
        val stackCount = (lines[8].length + 1) / 4
        repeat(stackCount) {
            stacks.add(mutableListOf())
        }

        stackLines.asReversed().forEach { line ->
            line.chunked(4).forEachIndexed { stackIndex, stackInput ->
                // 0 '[A] '
                if (stackInput.isNotBlank()) {
                    stacks[stackIndex].add(stackInput[1])
                }
            }
        }

        println(stacks)

        val instructions = lines.drop(10)
        instructions.forEach { instruction ->
            val splitInstruction = instruction.split(" ")
            val moveCount = splitInstruction[1].toInt()
            val a = splitInstruction[3].toInt()
            val b = splitInstruction[5].toInt()

            repeat(moveCount) {
                val temp = stacks[a - 1].removeLast()
                stacks[b - 1].add(temp)
            }
        }

        val result1 = stacks.map { it.last() }.joinToString(separator = "")
        println("Result1: $result1")
    }

    run {
        val stackLines = lines.take(8)
        val stacks = mutableListOf<MutableList<Char>>()
        val stackCount = (lines[8].length + 1) / 4
        repeat(stackCount) {
            stacks.add(mutableListOf())
        }

        stackLines.asReversed().forEach { line ->
            line.chunked(4).forEachIndexed { stackIndex, stackInput ->
                // 0 '[A] '
                if (stackInput.isNotBlank()) {
                    stacks[stackIndex].add(stackInput[1])
                }
            }
        }

        println(stacks)

        val instructions = lines.drop(10)
        instructions.forEach { instruction ->
            val splitInstruction = instruction.split(" ")
            val moveCount = splitInstruction[1].toInt()
            val a = splitInstruction[3].toInt()
            val b = splitInstruction[5].toInt()

            val temp = stacks[a - 1].takeLast(moveCount)
            stacks[a - 1] = stacks[a - 1].dropLast(moveCount).toMutableList()
            stacks[b - 1].addAll(temp)
        }

        val result2 = stacks.map { it.last() }.joinToString(separator = "")
        println("Result2: $result2")

        println("Development time: 17m20s")
    }


}