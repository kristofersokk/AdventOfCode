package year2022.day10

fun main() {
    val file = java.io.File("src/main/resources/2022/2022-day10.txt")
    val lines = file.readLines()

    var cycle = 1
    var registerX = 1
    var commandIndex = 0

    var cycleChangeIndex: Int? = null
    var cycleChange: (() -> Unit)? = null

    val cycleRegisterXValues = mutableListOf(0)

    while (cycle <= (cycleChangeIndex ?: -1) || commandIndex <= lines.size) {
        if (cycle in 20..220 step 40) {
            println("Cycle $cycle, registerX = $registerX")
        }
        cycleRegisterXValues.add(registerX)

        if (cycleChangeIndex == cycle) {
            cycleChange?.invoke()
            cycleChangeIndex = null
            cycleChange = null
        } else {
            val line = lines.getOrNull(commandIndex)
            if (line != null) {
                val parts = line.split(" ")
                val command = parts[0]
                if (command == "addx") {
                    cycleChangeIndex = cycle + 1
                    cycleChange = {
                        registerX += parts[1].toInt()
                    }
                }
            }
            commandIndex++
        }
        cycle++
    }

    val result1 = (20..220 step 40).sumOf { it * cycleRegisterXValues[it] }

    println("Result 1: $result1")

    println("Result 2: rendered")
    (0 until 6).forEach { y ->
        (0 until 40).forEach { x ->
            val spriteMiddle = cycleRegisterXValues[y * 40 + x + 1]
            print(if (x in spriteMiddle - 1..spriteMiddle + 1) "#" else ".")
        }
        println()
    }

    println("Development time: 35m48s")
}
