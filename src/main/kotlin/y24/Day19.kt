package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle


fun main() = solvePuzzle(year = 2024, day = 19) { Day19(it) }

class Day19(val input: Input) : Puzzle {
    private val patterns = input.lines[0].split(", ")
    private val designs = input.lines.subList(2, input.lines.size)

    data class TrieNode(
        val c: Char,
        val children: MutableMap<Char, TrieNode> = mutableMapOf(),
        var pattern: String? = null,
    )

    private fun buildTrie(patterns: List<String>): TrieNode {
        val root = TrieNode('-')
        patterns.forEach { pattern ->
            var node = root
            pattern.forEach { c ->
                node = node.children[c] ?: TrieNode(c).also { node.children[c] = it }
            }
            node.pattern = pattern
        }

        return root
    }

    private fun numPossibilities(design: String, trie: TrieNode, cache: MutableMap<String, Long>): Long {
        if (design.isEmpty()) {
            return 1
        }

        cache[design]?.let { return it }

        var node = trie
        var i = 0
        var num = 0L
        while (i < design.length) {
            val c = design[i]
            i++

            node = node.children[c] ?: run {
                cache[design] = num
                return num
            }

            node.pattern?.let {
                num += numPossibilities(design.substring(i), trie, cache)
            }
        }

        cache[design] = num
        return num
    }

    override fun solveLevel1(): Any {
        val trie = buildTrie(patterns)
        return designs.count { design -> numPossibilities(design, trie, mutableMapOf()) > 0 }
    }

    override fun solveLevel2(): Any {
        val trie = buildTrie(patterns)
        return designs.sumOf { design -> numPossibilities(design, trie, mutableMapOf()) }
    }
}
