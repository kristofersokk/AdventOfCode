package year2022.day7

fun main() {
    val file = java.io.File("src/main/resources/2022/2022-day7.txt")
    val lines = file.readLines()

    val rootDir = Directory("/")
    var pwd = "/"
    var pwdDir = rootDir

    lines.forEach { line ->
        val parts = line.split(" ")
        when (parts[0]) {
            "$" -> {
                val command = parts[1]
                if (command == "cd") {
                    when (parts[2]) {
                        "/" -> {
                            pwd = "/"
                            pwdDir = rootDir
                        }

                        ".." -> {
                            pwd = pwd.substring(0 until pwd.indexOfLast { it == '/' }).let {
                                it.ifEmpty { "/" }
                            }
                            pwdDir = pwdDir.parent!!
                        }

                        else -> {
                            val subDirName = parts[2]
                            pwd = if (pwd == "/") "/${subDirName}" else "$pwd/${subDirName}"
                            if (subDirName !in pwdDir.dirs) {
                                pwdDir.dirs[subDirName] = Directory(pwd, pwdDir)
                            }
                            pwdDir = pwdDir.dirs[subDirName]!!
                        }
                    }
                }
            }

            "dir" -> {
                val dirName = parts[1]
                if (dirName !in pwdDir.dirs) {
                    pwdDir.dirs[dirName] = Directory(dirName, pwdDir)
                }
            }

            else -> {
                val fileSize = parts[0].toInt()
                val fileName = parts[1]
                if (fileName !in pwdDir.files) {
                    pwdDir.files[fileName] = File(fileName, fileSize)
                }
            }
        }
    }

    println(rootDir.toBeautifulString())
    println()
    println("Root dir size: ${rootDir.totalSize}")

    fun Directory.allSubDirectories(): List<Directory> =
        listOf(this) + this.dirs.values.flatMap { it.allSubDirectories() }

    val allDirectories = rootDir.allSubDirectories()
    val result1 = allDirectories.map { it.totalSize }.filter { it <= 100000 }.sum()

    println("Result1: $result1")

    val currentFreeSpace = 70_000_000 - rootDir.totalSize
    val spaceNeededToDelete = 30_000_000 - currentFreeSpace
    val result2 = allDirectories.map { it.totalSize }.filter { it >= spaceNeededToDelete }.min()

    println("Result2: $result2")

    println("Development time: 1h8m22s")
}

private data class File(
    val name: String,
    val size: Int,
) {
    override fun toString(): String {
        return "$name (file, size=$size)"
    }
}

private data class Directory(
    val name: String,
    val parent: Directory? = null,
    val dirs: MutableMap<String, Directory> = mutableMapOf(),
    val files: MutableMap<String, File> = mutableMapOf(),
) {
    val totalSize: Int
        get() = files.values.sumOf { it.size } + dirs.values.sumOf { it.totalSize }

    override fun toString(): String {
        return "$name (dir)"
    }

    fun toBeautifulString(): String {
        val files = files.values.sortedBy { it.name }
        val dirs = dirs.values.sortedBy { it.name }
        return "- " + toString() + (files.map { it.toString() }.map {
            "  - $it"
        } + dirs.map {
            it.toBeautifulString().split("\n").joinToString(separator = "\n") {
                "  $it"
            }
        }).joinToString("\n").let { if (it == "") "" else "\n$it" }
    }
}
