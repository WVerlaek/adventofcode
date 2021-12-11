package common.models

data class Cell<T>(val row: Int, val col: Int, var value: T)

private val neighsX = listOf(-1, 0, 1, 0)
private val neighsY = listOf(0, -1, 0, 1)

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

    fun neighbors(cell: Cell<T>): List<Cell<T>> {
        return neighsX.mapIndexedNotNull { index, dx ->
            val dy = neighsY[index]

            val x = cell.col + dx
            val y = cell.row + dy
            if (x < 0 || x >= numCols || y < 0 || y >= numRows) {
                null
            } else {
                rows[y][x]
            }
        }
    }

    var cellFormatter: (Cell<T>) -> String = { it.value.toString() }
    override fun toString(): String {
        return rows.joinToString("\n") { it.joinToString(",") { cell -> cellFormatter(cell) } }
    }
}
