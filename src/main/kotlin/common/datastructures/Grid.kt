package common.datastructures

data class Cell<T>(val row: Int, val col: Int, var value: T)
fun Cell<*>.toPoint() = Point(col, row)

private val neighsX = listOf(-1, 0, 1, 0)
private val neighsY = listOf(0, -1, 0, 1)
private val neighsXDiags = listOf(-1, -1, 0, 1, 1, 1, 0, -1)
private val neighsYDiags = listOf(0, 1, 1, 1, 0, -1, -1, -1)

data class Dir(val dRow: Int, val dCol: Int)
fun Dir.toPoint() = Point(dCol, dRow)
val directions: List<Dir> = (0..3).map { i -> Dir(neighsY[i], neighsX[i]) }
val directionsWithDiagonals: List<Dir> = neighsYDiags.zip(neighsXDiags) { y, x -> Dir(y, x) }

class Grid<T>(val rows: List<List<Cell<T>>>) {
    constructor(rows: Int, cols: Int, constructor: (row: Int, col: Int) -> T) : this(Array(rows) { row ->
        Array(cols) { col ->
            Cell<T>(row, col, constructor(row, col))
        }.toList()
    }.toList())
    constructor(input: List<String>, constr: (row: Int, col: Int, c: Char) -> T) : this(Array(input.size) { row ->
        Array(input[0].length) { col ->
            Cell<T>(row, col, constr(row, col, input[row][col]))
        }.toList()
    }.toList())

    operator fun get(row: Int) = rows[row]
    operator fun get(p: Point) = rows[p.row][p.col]

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

    fun <V> map(operator: (Cell<T>) -> V): Grid<V> {
        return Grid(numRows, numCols) { r, c ->
            operator(this[r][c])
        }
    }

    class CellFormatter<T>(val cellFormat: (Cell<T>) -> CharSequence, val cellSeparator: String = ",")
    val boolFormatter = CellFormatter<T>({ cell -> if (cell.value as Boolean) "#" else "." }, "")
    val anyFormatter = CellFormatter<T>({ it.value.toString() })

    var cellFormatter: CellFormatter<T> = if (cells().firstOrNull()?.value is Boolean) boolFormatter else anyFormatter

    override fun toString(): String {
        return rows.joinToString("\n") { it.joinToString(cellFormatter.cellSeparator, transform = cellFormatter.cellFormat) }
    }

    override fun equals(other: Any?): Boolean {
        when (other) {
            is Grid<*> -> {
                if (numRows != other.numRows || numCols != other.numCols) {
                    return false
                }

                for (row in 0 until numRows) {
                    for (col in 0 until numCols) {
                        if (this[row][col].value != other[row][col].value) {
                            return false
                        }
                    }
                }

                return true
            }
            else -> return false
        }
    }

    override fun hashCode(): Int {
        var hashCode = 1
        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                hashCode = 31 * hashCode + this[row][col].value.hashCode()
            }
        }
        return hashCode
    }
}

fun List<Point>.fitGrid(): Grid<Boolean> {
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

fun String.toGrid(): Grid<Boolean> {
    return trimIndent().lines().toGrid()
}

fun List<String>.toGrid(): Grid<Boolean> {
    require(all { it.length == this[0].length })
    require(all { line -> line.all { c -> c == '.' || c == '#' } })
    return Grid(size, this[0].length) { r, c -> this[r][c] == '#' }
}

fun <T> Grid<T>.expand(padding: Int, value: T): Grid<T> {
    return Grid(numRows + 2 * padding, numCols + 2 * padding) { r, c ->
        if (r < padding || c < padding || r >= numRows + padding || c >= numCols + padding) {
            value
        } else {
            this[r - padding][c - padding].value
        }
    }
}

fun Grid<Boolean>.trim(): Grid<Boolean> {
    val fromX = rows.minOf { row -> row.indexOfFirst { cell -> cell.value }.let { if (it == -1) Int.MAX_VALUE else it } }
    val toX = rows.maxOf { row -> row.indexOfLast { cell -> cell.value }.let { if (it == numCols) Int.MIN_VALUE else it } }
    val fromY = (0 until numCols).minOf { col -> (0 until numRows).indexOfFirst { row -> this[row][col].value }.let { if (it == -1) Int.MAX_VALUE else it } }
    val toY = (0 until numCols).maxOf { col -> (0 until numRows).indexOfLast { row -> this[row][col].value }.let { if (it == numRows) Int.MAX_VALUE else it } }
    return Grid(toY - fromY + 1, toX - fromX + 1) { r, c ->
        this[r + fromY][c + fromX].value
    }
}
