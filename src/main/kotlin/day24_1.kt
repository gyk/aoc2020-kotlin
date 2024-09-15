data class CubeCoord(val q: Int, val r: Int, val s: Int) {
    operator fun plus(other: CubeCoord): CubeCoord {
        val (q1, r1, s1) = this
        val (q2, r2, s2) = other
        return CubeCoord(q1 + q2, r1 + r2, s1 + s2)
    }

    companion object {
        val E = CubeCoord(1, 0, -1)
        val W = CubeCoord(-1, 0, 1)
        val SE = CubeCoord(1, -1, 0)
        val SW = CubeCoord(0, -1, 1)
        val NW = CubeCoord(-1, 1, 0)
        val NE = CubeCoord(0, 1, -1)
    }
}

fun parseHex(s: Iterator<Char>): List<CubeCoord> {
    val res = mutableListOf<CubeCoord>()
    while (s.hasNext()) {
        val ch = s.next()
        res += when (ch) {
            'e' -> CubeCoord.E
            'w' -> CubeCoord.W
            else -> {
                val ch2 = s.next()
                when ("$ch$ch2") {
                    "se" -> CubeCoord.SE
                    "sw" -> CubeCoord.SW
                    "nw" -> CubeCoord.NW
                    "ne" -> CubeCoord.NE
                    else -> error("Invalid move")
                }
            }
        }
    }
    return res
}

fun main() {
    val m = mutableSetOf<CubeCoord>()
    generateSequence { readlnOrNull() }
        .map { parseHex(it.iterator()).reduce(CubeCoord::plus) }
        .forEach { if (!m.add(it)) m.remove(it) }
    println("${m.size}")
}
