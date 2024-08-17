data class Tile(val id: Int, val borders: List<Int>) {
    companion object {
        fun parse(lines: Iterator<String>): Tile {
            val head = lines.next().let { it.ifBlank { lines.next() } }
            val id = head.substringAfter("Tile ").substringBefore(":").toInt()

            val rows = ArrayList<String>()
            for ((r, line) in lines.withIndex()) {
                rows.add(line)
                if (r == line.length - 1) {
                    break
                }
            }

            val borders = listOf(
                borderCode(rows.first().asSequence()),
                borderCode(rows.asSequence().map { it.last() }),
                borderCode(rows.last().asSequence()),
                borderCode(rows.asSequence().map { it.first() }),
            )

            return Tile(id, borders)
        }

        private fun borderCode(chars: Sequence<Char>): Int {
            val s = chars
                .map { ch ->
                    when (ch) {
                        '.' -> '0'
                        '#' -> '1'
                        else -> '2'
                    }
                }.joinToString("")
            return minOf(s.toInt(2), s.reversed().toInt(2))
        }
    }
}

fun findCorners(tiles: List<Tile>): List<Tile> {
    val borderToId = mutableMapOf<Int, Int>()
    for (tile in tiles) {
        tile.borders.forEach { b ->
            borderToId.compute(b) { _, v -> if (v == null) 1 else v + 1 }
        }
    }

    return tiles.filter { tile ->
        tile.borders.count { borderToId[it] == 1 } == 2
    }
}

fun main() {
    val lines = generateSequence { readlnOrNull() }.iterator()
    val tiles = ArrayList<Tile>()
    while (lines.hasNext()) {
        tiles.add(Tile.parse(lines))
    }
    val corners = findCorners(tiles)
    require(corners.size == 4)
    val product = corners.map { it.id.toLong() }.reduce(Long::times)
    println("$product")
}
