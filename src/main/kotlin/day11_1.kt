enum class Seat(val value: Char) {
    Wall('X'),
    Floor('.'),
    Empty('L'),
    Occupied('#');

    val isOccupied
        get() = this == Occupied

    companion object {
        fun fromChar(value: Char): Seat? {
            return when (value) {
                '.' -> Floor
                'L' -> Empty
                '#' -> Occupied
                else -> null
            }
        }
    }
}

@JvmInline
value class Layout(val state: Array<Array<Seat>>) {
    val Layout.nRows
        get() = this.state.size - 2
    val Layout.nColumns
        get() = this.state.first().size - 2

    fun countOccupied(): Int {
        var nOccupied = 0
        for (row in this.state) {
            nOccupied += row.count { it.isOccupied }
        }
        return nOccupied
    }

    fun step(): Layout {
        val layout = this.state
        val newLayout = layout.map { it.clone() }.toTypedArray()
        for (r in 1..this.nRows) {
            for (c in 1..this.nColumns) {
                if (layout[r][c] == Seat.Floor) continue

                var neighbors = 0
                for (rr in -1..1) {
                    for (cc in -1..1) {
                        if (rr == 0 && cc == 0) continue
                        if (layout[r + rr][c + cc].isOccupied) {
                            neighbors++
                        }
                    }
                }

                if (!layout[r][c].isOccupied && neighbors == 0) {
                    newLayout[r][c] = Seat.Occupied
                } else if (layout[r][c].isOccupied && neighbors >= 4) {
                    newLayout[r][c] = Seat.Empty
                }
            }
        }
        return Layout(newLayout)
    }

    fun hash(): Int =
        state.map { row -> row.contentHashCode() }.hashCode()

    fun print() {
        for (r in 1..nRows) {
            for (c in 1..nColumns) {
                print(state[r][c].value)
            }
            println()
        }
    }
}

fun inputSeatLayout(): Layout {
    val lines = generateSequence { readlnOrNull()?.takeIf { it.isNotEmpty() } }.toList()
    val nRowsW = lines.size + 2
    val nColumnsW = lines[0].length + 2

    val layout = Array(nRowsW) { r ->
        if (r == 0 || r == nRowsW - 1) {
            return@Array Array<Seat>(nColumnsW) { Seat.Wall }
        }
        val r0 = r - 1
        val row = lines[r0]
        val rowSeq = sequenceOf(Seat.Wall) +
                row.asSequence().map { Seat.fromChar(it)!! } +
                sequenceOf(Seat.Wall)
        val iter = rowSeq.iterator()
        Array(nColumnsW) { iter.next() }
    }

    return Layout(layout)
}

fun Layout.stepUntilStabilized(): Layout {
    var layout = this
    var hash = layout.hash()
    do {
        val oldHash = hash
        layout = layout.step()
        hash = layout.hash()
    } while (hash != oldHash)
    return layout
}

fun main() {
    val layout = inputSeatLayout()
    val finalLayout = layout.stepUntilStabilized()
    println("${finalLayout.countOccupied()}")
}
