package common.datastructures

data class Cell<T>(val row: Int, val col: Int, var value: T)

private val neighsX = listOf(-1, 0, 1, 0)
private val neighsY = listOf(0, -1, 0, 1)
private val neighsXDiags = listOf(-1, -1, 0, 1, 1, 1, 0, -1)
private val neighsYDiags = listOf(0, 1, 1, 1, 0, -1, -1, -1)

class Grid<T>(val rows: List<List<Cell<T>>>) {
    constructor(rows: Int, cols: Int, constructor: (row: Int, col: Int) -> T) : this(Array(rows) { row ->
        Array(cols) { col ->
            Cell<T>(row, col, constructor(row, col))
        }.toList()
    }.toList())

    operator fun get(row: Int) = rows[row]

    fun cells(): List<Cell<T>> = this.rows.flatten()

    val numRows: Int = rows.size
    val numCols: Int = rows[0].size

    fun withinBounds(row: Int, col: Int) = row in rows.indices && col in rows[row].indices

    fun neighbors(cell: Cell<T>, includeDiagonals: Boolean = false): List<Cell<T>> {
        val xs = if (includeDiagonals) neighsXDiags else neighsX
        val ys = if (includeDiagonals) neighsYDiags else neighsY
        return xs.mapIndexedNotNull { index, dx ->
            val dy = ys[index]

            val x = cell.col + dx
            val y = cell.row + dy
            if (x < 0 || x >= numCols || y < 0 || y >= numRows) {
                null
            } else {
                rows[y][x]
            }
        }
    }

    fun copy(): Grid<T> = Grid(rows.map { row -> row.map { cell -> cell.copy() } })

    class CellFormatter<T>(val cellFormat: (Cell<T>) -> CharSequence, val cellSeparator: String = ",")
    val boolFormatter = CellFormatter<T>({ cell -> if (cell.value as Boolean) "#" else "." }, "")
    val anyFormatter = CellFormatter<T>({ it.value.toString() })

    var cellFormatter: CellFormatter<T> = if (cells().firstOrNull()?.value is Boolean) boolFormatter else anyFormatter

    override fun toString(): String {
        return rows.joinToString("\n") { it.joinToString(cellFormatter.cellSeparator, transform = cellFormatter.cellFormat) }
    }
}

fun List<Point>.toGrid(): Grid<Boolean> {
    val minRow = minOf(minOf { it.row }, 0)
    val minCol = minOf(minOf { it.col }, 0)
    val translated = map { it.copy(col = it.col - minCol, row = it.row - minRow) }
        .toSet()
    val maxRow = translated.maxOf { it.row }
    val maxCol = translated.maxOf { it.col }
    return Grid(maxRow + 1, maxCol + 1) { row, col ->
        Point(col, row) in translated
    }
}
