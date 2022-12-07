package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day7Test {
    private val sample = Input("""
    $ cd /
    $ ls
    dir a
    14848514 b.txt
    8504156 c.dat
    dir d
    $ cd a
    $ ls
    dir e
    29116 f
    2557 g
    62596 h.lst
    $ cd e
    $ ls
    584 i
    $ cd ..
    $ cd ..
    $ cd d
    $ ls
    4060174 j
    8033020 d.log
    5626152 d.ext
    7214296 k
    """.trimIndent())

    private val day = Day7(sample)

    @Test
    fun solveLevel1() {
        assertEquals(95437, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(24933642, day.solveLevel2())
    }
}
