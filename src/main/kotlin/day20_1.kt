data class Tile(
    val id: Int,
    val borders: List<List<Boolean>>,
    val pixels: List<List<Boolean>>, // for Part 2
    val signs: List<Int> = listOf(0, 1, 2), // for Part 2
) {
    companion object {
        fun parse(lines: Iterator<String>): Tile {
            val head = lines.next().let { it.ifBlank { lines.next() } }
            val id = head.substringAfter("Tile ").substringBefore(":").toInt()

            val rows = mutableListOf<List<Boolean>>()
            for (line in lines) {
                rows += line.map { ch ->
                    when (ch) {
                        '.' -> false
                        '#' -> true
                        else -> error("Invalid input")
                    }
                }

                if (rows.size == line.length) {
                    break
                }
            }

            val borders = listOf(
                rows.first(),
                rows.map { it.last() },
                rows.last().reversed(),
                rows.map { it.first() }.reversed(),
            )

            return Tile(id, borders, rows)
        }
    }
}

fun List<Tile>.findCorners(): List<Tile> {
    val borderCount = this.flatMap { it.borders }.groupingBy { it }.eachCount()
    return this.filter { tile ->
        tile.borders.count { borderCount[it]!! + (borderCount[it.asReversed()] ?: 0) == 1 } == 2
    }
}

fun main() {
    val lines = generateSequence { readlnOrNull() }.iterator()
    val tiles = ArrayList<Tile>()
    while (lines.hasNext()) {
        tiles.add(Tile.parse(lines))
    }
    val corners = tiles.findCorners()
    require(corners.size == 4)
    val product = corners.map { it.id.toLong() }.reduce(Long::times)
    println("$product")
}
