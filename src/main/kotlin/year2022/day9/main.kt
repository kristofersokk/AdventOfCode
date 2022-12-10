package year2022.day9

import kotlin.math.abs

fun main() {
    val file = java.io.File("src/main/resources/2022/2022-day9.txt")
    val lines = file.readLines()

    run {
        var hx = 0
        var hy = 0
        var tx = 0
        var ty = 0
        val tailVisited = mutableSetOf(tx to ty)
        lines.forEach { line ->
            val (command, moveCountStr) = line.split(" ")
            val moveCount = moveCountStr.toInt()
            val moveCommand = when (command) {
                "U" -> {
                    {
                        hy++
                        if (hy - ty >= 2) {
                            ty = hy - 1
                            tx = hx
                        }
                    }
                }

                "D" -> {
                    {
                        hy--
                        if (ty - hy >= 2) {
                            ty = hy + 1
                            tx = hx
                        }
                    }
                }

                "L" -> {
                    {
                        hx--
                        if (tx - hx >= 2) {
                            tx = hx + 1
                            ty = hy
                        }
                    }
                }

                "R" -> {
                    {
                        hx++
                        if (hx - tx >= 2) {
                            tx = hx - 1
                            ty = hy
                        }
                    }
                }

                else -> throw IllegalArgumentException("Unknown command: $command")
            }
            repeat(moveCount) {
                moveCommand()
                tailVisited.add(tx to ty)
            }
        }
        val result1 = tailVisited.size

        println("Result1: $result1")
    }

    run {
        var hx = 0
        var hy = 0
        val tails = (0 until 9).map { 0 to 0 }.toMutableList()
        val tailVisited = mutableSetOf(0 to 0)
        lines.forEach { line ->
            val (command, moveCountStr) = line.split(" ")
            val moveCount = moveCountStr.toInt()
            fun move() {
                when (command) {
                    "U" -> hy++
                    "D" -> hy--
                    "L" -> hx--
                    "R" -> hx++
                    else -> throw IllegalArgumentException("Unknown command: $command")
                }
                for (tailIndex in tails.indices) {
                    val tail = tails[tailIndex]
                    val prev = if (tailIndex == 0) hx to hy else tails[tailIndex - 1]

                    val (prevX, prevY, tailX, tailY) = prev.toList() + tail.toList()

                    val dx = prevX - tailX
                    val dy = prevY - tailY
                    if (abs(dx) == 2 && abs(dy) == 2) {
                        tails[tailIndex] = tailX + dx / 2 to tailY + dy / 2
                    } else {
                        if (dy >= 2) {
                            tails[tailIndex] = prevX to prevY - 1
                        }
                        if (dy <= -2) {
                            tails[tailIndex] = prevX to prevY + 1
                        }
                        if (dx <= -2) {
                            tails[tailIndex] = prevX + 1 to prevY
                        }
                        if (dx >= 2) {
                            tails[tailIndex] = prevX - 1 to prevY
                        }
                    }
                }
            }
            repeat(moveCount) {
                move()
                tailVisited.add(tails.last().first to tails.last().second)
            }
//            println(listOf(hx to hy) + tails)
        }

        val result2 = tailVisited.size
//        println(tailVisited)
//        println(tails)

        println("Result2: $result2")
    }


    println("Development time: 58m40s")
}
