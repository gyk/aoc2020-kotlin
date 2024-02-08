fun Layout.step2(): Layout {
    val layout = this.state
    val newLayout = layout.map { it.clone() }.toTypedArray()
    for (r in 1..this.nRows) {
        for (c in 1..this.nColumns) {
            if (layout[r][c] == Seat.Floor) continue

            var occupied = 0
            for (dr in -1..1) {
                loop@ for (dc in -1..1) {
                    if (dr == 0 && dc == 0) continue@loop
                    var currR = r
                    var currC = c
                    do {
                        currR += dr
                        currC += dc
                        if (layout[currR][currC].isOccupied) {
                            occupied++
                            continue@loop
                        } else if (layout[currR][currC] == Seat.Empty) {
                            continue@loop
                        }
                    } while (layout[currR][currC] != Seat.Wall)
                }
            }

            if (!layout[r][c].isOccupied && occupied == 0) {
                newLayout[r][c] = Seat.Occupied
            } else if (layout[r][c].isOccupied && occupied > 4) {
                newLayout[r][c] = Seat.Empty
            }
        }
    }
    return Layout(newLayout)
}

fun Layout.step2UntilStabilized(): Layout {
    var layout = this
    var hash = layout.hash()
    do {
        val oldHash = hash
        layout = layout.step2()
        hash = layout.hash()
    } while (oldHash != hash)
    return layout
}

fun main() {
    var layout = inputSeatLayout()
    layout = layout.step2UntilStabilized()
    println("! ${layout.countOccupied()}")
}
