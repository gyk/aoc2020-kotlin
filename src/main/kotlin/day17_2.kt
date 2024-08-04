typealias Quad = Pair<Pair<Int, Int>, Pair<Int, Int>>

fun main() {
    var space = HashSet<Quad>()
    generateSequence { readlnOrNull() }
        .forEachIndexed { r, line ->
            line.forEachIndexed { c, char ->
                if (char == '#') {
                    space.add((c to r) to (0 to 0))
                }
            }
        }

    repeat(6) {
        val count = HashMap<Quad, Int>()
        for ((xy, zw) in space) {
            val (x, y) = xy
            val (z, w) = zw
            for (dx in -1..1) {
                for (dy in -1..1) {
                    for (dz in -1..1) {
                        for (dw in -1..1) {
                            if (dx == 0 && dy == 0 && dz == 0 && dw == 0) {
                                continue
                            }
                            val coord = (x + dx to y + dy) to (z + dz to w + dw)
                            count[coord] = (count[coord] ?: 0) + 1
                        }
                    }
                }
            }
        }

        val newSpace = HashSet<Quad>()
        for ((coord, cnt) in count) {
            if (cnt == 3 || (cnt == 2 && space.contains(coord))) {
                newSpace.add(coord)
            }
        }
        space = newSpace
    }
    println("${space.size}")
}
