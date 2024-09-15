fun stepLobby(blacks: MutableSet<CubeCoord>, nTimes: Int): Int {
    repeat(nTimes) {
        val blackNeighbors = HashMap<CubeCoord, Int>()
        for (x in blacks) {
            blackNeighbors.putIfAbsent(x, 0)
            for (move in listOf(
                CubeCoord.E, CubeCoord.SE, CubeCoord.SW, CubeCoord.W, CubeCoord.NW, CubeCoord.NE
            )) {
                blackNeighbors.merge(x + move, 1) { old, new -> old + new }
            }
        }

        val (bList, wList) = blackNeighbors.asSequence().partition { blacks.contains(it.key) }
        bList.filter { (_, cnt) -> cnt == 0 || cnt > 2 }.forEach { blacks.remove(it.key) }
        wList.filter { (_, cnt) -> cnt == 2 }.forEach { blacks.add(it.key) }
    }
    return blacks.size
}

fun main() {
    val m = mutableSetOf<CubeCoord>()
    generateSequence { readlnOrNull() }
        .map { parseHex(it.iterator()).reduce(CubeCoord::plus) }
        .forEach { if (!m.add(it)) m.remove(it) }
    val res = stepLobby(m, 100)
    println("$res")
}
