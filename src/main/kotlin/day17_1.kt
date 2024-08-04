fun main() {
    var space = HashSet<Triple<Int, Int, Int>>()
    generateSequence { readlnOrNull() }
        .forEachIndexed { r, line ->
            line.forEachIndexed { c, char ->
                if (char == '#') {
                    space.add(Triple(c, r,0))
                }
            }
        }

    repeat(6) {
        val count = HashMap<Triple<Int, Int, Int>, Int>()
        for ((x, y, z) in space) {
            for (dx in -1..1) {
                for (dy in -1..1) {
                    for (dz in -1..1) {
                        if (dx == 0 && dy == 0 && dz == 0) {
                            continue
                        }
                        val coord = Triple(x + dx, y + dy, z + dz)
                        count[coord] = (count[coord] ?: 0) + 1
                    }
                }
            }
        }

        val newSpace = HashSet<Triple<Int, Int, Int>>()
        for ((coord, cnt) in count) {
            if (cnt == 3 || (cnt == 2 && space.contains(coord))) {
                newSpace.add(coord)
            }
        }
        space = newSpace
    }
    println("${space.size}")
}
