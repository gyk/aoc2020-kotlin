import java.util.Collections
import kotlin.math.*

fun xform(base: List<Int>, apply: List<Int>) = listOf(
    0,
    base[apply[1].absoluteValue] * apply[1].sign,
    base[apply[2].absoluteValue] * apply[2].sign,
)

fun Tile.rotate(times: Int = 1): Tile {
    if (times == 0) return this
    val newBorders = this.borders.toList()
    Collections.rotate(newBorders, times)
    var signs = this.signs
    repeat(times) {
        signs = xform(listOf(0, -2, 1), signs)
    }

    return this.copy(borders = newBorders, signs = signs)
}

fun Tile.flipH(): Tile {
    val b = this.borders
    return this.copy(
        borders = listOf(b[0], b[3], b[2], b[1]).map { it.reversed() },
        signs = xform(listOf(0, 1, -2), signs),
    )
}

fun Tile.get(r: Int, c: Int): Boolean {
    val n = this.pixels[0].size
    val f = { sgn: Int ->
        when (sgn) {
            -1 -> n - 1 - r
            -2 -> n - 1 - c
            1 -> r
            2 -> c
            else -> 0
        }
    }
    val (_, rr, cc) = this.signs.map(f)
    return this.pixels[rr][cc]
}

fun List<Tile>.select(remainingSet: Set<Int>, target: List<Boolean>): Tile? {
    this.filter { remainingSet.contains(it.id) }
        .forEach { tile ->
            for ((i, b) in tile.borders.withIndex()) {
                if (b == target) {
                    return tile.rotate((-i).mod(4))
                } else if (b.reversed() == target) {
                    return tile.rotate((-i).mod(4)).flipH()
                }
            }
        }
    return null
}

fun main() {
    val lines = generateSequence { readlnOrNull() }.iterator()
    val tiles = ArrayList<Tile>()
    while (lines.hasNext()) {
        tiles.add(Tile.parse(lines))
    }

    val nInner = tiles[0].pixels.size
    val nOuter = sqrt(tiles.size.toFloat()).roundToInt()
    val image: Array<Array<Tile?>> = Array(nOuter) { Array(nOuter) { null } }
    val corner = tiles.findCorners()[0]

    loop@ for (i in 0..3) {
        val remaining = tiles.map { it.id }.toMutableSet()
        for (r in 0..<nOuter) {
            for (c in 0..<nOuter) {
                val tile = if (r == 0 && c == 0) {
                    corner.rotate(i)
                } else if (r == 0) {
                    val b = image[r][c - 1]!!.borders[1]
                    tiles.select(remaining, b.asReversed())?.rotate(3) ?: continue@loop
                } else {
                    val b = image[r - 1][c]!!.borders[2]
                    tiles.select(remaining, b.asReversed()) ?: continue@loop
                }
                image[r][c] = tile
                remaining -= tile.id
            }
        }
        break@loop
    }

    val img = (0..<nOuter).flatMap { rOuter ->
        (1..<(nInner - 1)).map { rInner ->
            (0..<nOuter).flatMap { cOuter ->
                (1..<(nInner - 1)).map { cInner ->
                    image[rOuter][cOuter]!!.get(rInner, cInner)
                }
            }
        }
    }
    val water = img.sumOf { row -> row.count { it } }

    val monsterPattern =
        "(?=#[.#\\n]{${img.size - 18}}(#....#){3}##[.#\\n]{${img.size - 19}}.(#..){5}#)".toRegex()

    val imageTile = Tile(id = -1, borders = List(4) { emptyList() }, pixels = img)
    for (imgTile in sequenceOf(imageTile, imageTile.flipH())) {
        for (i in 0..3) {
            val rotated = imgTile.rotate(i)
            val s = buildString {
                for (r in img.indices) {
                    for (c in img[0].indices) {
                        append(if (rotated.get(r, c)) '#' else '.')
                    }
                    appendLine()
                }
            }

            val matches = monsterPattern.findAll(s)
            if (matches.any()) {
                println("${water - 15 * matches.count()}")
                return
            }
        }
    }
}
