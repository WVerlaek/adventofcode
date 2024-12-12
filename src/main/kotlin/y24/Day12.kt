package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*


fun main() = solvePuzzle(year = 2024, day = 12) { Day12(it) }

class Day12(val input: Input) : Puzzle {

    data class Price(
        val area: Int,
        val perimeter: Int,
    ) {
        val total: Int = area * perimeter
    }

    private fun pricePerimeter(grid: Grid<Char>, cell: Cell<Char>, visited: MutableSet<Point>): Price {
        val p = cell.toPoint()
        if (p in visited) return Price(0, 0)

        visited += p

        var area = 1
        var perimeter = 0
        directions.forEach { dir ->
            val neigh = p + dir.toPoint()
            when {
                !grid.withinBounds(neigh.row, neigh.col) -> {
                    perimeter++
                }
                grid[neigh].value != cell.value -> {
                    perimeter++
                }
                else -> {
                    val (neighArea, neighPerimeter) = pricePerimeter(grid, grid[neigh], visited)
                    area += neighArea
                    perimeter += neighPerimeter
                }
            }
        }

        return Price(area, perimeter)
    }

    private fun perimeter(grid: Grid<Char>, cell: Cell<Char>): List<Dir> {
        val p = cell.toPoint()
        return directions.mapNotNull { dir ->
            val neigh = p + dir.toPoint()
            when {
                !grid.withinBounds(neigh.row, neigh.col) -> dir
                grid[neigh].value != cell.value -> dir
                else -> null
            }
        }
    }

    private fun priceNumSides(grid: Grid<Char>, cell: Cell<Char>, visited: MutableSet<Point>): Price {
        val disjointSets = DisjointSets<Pair<Point, Dir>>()
        val area = priceNumSidesInternal(grid, cell, null, emptyList(), visited, disjointSets)
        val numSides = disjointSets.sets.size
        return Price(area, numSides)
    }

    private fun priceNumSidesInternal(grid: Grid<Char>, cell: Cell<Char>, from: Point?, fromPerimeter: List<Dir>, visited: MutableSet<Point>, disjointSets: DisjointSets<Pair<Point, Dir>>): Int {
        val p = cell.toPoint()
        val perimeter = perimeter(grid, cell)
        perimeter.forEach { disjointSets.find(p to it) }

        from?.let {
            // Merge perimeter
            perimeter
                .filter { it in fromPerimeter }
                .forEach { dir ->
                    // dir is shared perimeter between p and from
                    disjointSets.union(p to dir, from to dir)
                }
        }

        if (p in visited) {
            return 0
        }

        visited += p
        return 1 + directions.sumOf { dir ->
            val neigh = p + dir.toPoint()
            when {
                !grid.withinBounds(neigh.row, neigh.col) -> 0
                grid[neigh].value != cell.value -> 0
                else -> priceNumSidesInternal(grid, grid[neigh], p, perimeter, visited, disjointSets)
            }
        }
    }

    private val grid = Grid(input.lines) { _, _, c -> c }

    override fun solveLevel1(): Any {
        val visited = mutableSetOf<Point>()
        var totalCost = 0

        grid.cells().forEach { cell ->
            totalCost += pricePerimeter(grid, cell, visited).total
        }

        return totalCost
    }

    override fun solveLevel2(): Any {
        val visited = mutableSetOf<Point>()
        var totalCost = 0

        grid.cells().forEach { cell ->
            totalCost += priceNumSides(grid, cell, visited).total
        }

        return totalCost
    }
}
