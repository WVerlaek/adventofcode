package common.models

class Cell<T>(val row: Int, val col: Int, var value: T)

class Grid<T>(val rows: List<List<Cell<T>>>) {
    constructor(rows: Int, cols: Int, constructor: (row: Int, col: Int) -> T) : this(Array(rows) { row ->
        Array(cols) { col ->
            Cell<T>(row, col, constructor(row, col))
        }.toList()
    }.toList())

    operator fun get(row: Int) = rows[row]

    fun cells(): List<Cell<T>> = this.rows.flatten()

    var cellFormatter: (Cell<T>) -> String = { it.value.toString() }
    override fun toString(): String {
        return rows.joinToString("\n") { it.joinToString(",") { cell -> cellFormatter(cell) } }
    }
}
