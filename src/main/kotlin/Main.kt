
import common.puzzle.solvePuzzle
import common.util.printError
import kotlin.system.exitProcess

// Run a solution. By default, runs the latest solution.
// Can specify a specific solution through an argument, e.g.:
// - `--args="1"` to run current year, day 1.
// - `--args="21.1" to run year 2021, day 1.
fun main(args: Array<String>) {
    var year = 22
    var day: Int? = null

    if (args.size > 0) {
        val split = args[0].split(".")
        if (split.size > 1) {
            year = split[0].toInt()
            day = split[1].toInt()
        } else {
            day = split[0].toInt()
        }
    }

    if (day != null) {
        if (!run(year, day)) {
            printError("Did not find puzzle y$year day $day")
            exitProcess(1)
        }
        return
    }

    for (i in 26 downTo 1) {
        if (run(year, i)) {
            return
        }
    }

    printError("Did not find a puzzle for y$year")
    exitProcess(1)
}

private fun run(year: Int, day: Int): Boolean {
    return try {
        val cls = Class.forName("y${year}.Day${day}Kt")
        val main = cls.getDeclaredMethod("main")
        main.invoke(null)
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}
