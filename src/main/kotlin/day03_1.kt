fun inputAreaMap(): List<CharArray> {
    return generateSequence { readLine()?.toCharArray()?.takeIf { it.isNotEmpty() } }.toList()
}

fun countTreeEncounters(area: List<CharArray>, dx: Int, dy: Int): Int {
    val h = area.size
    val w = area[0].size

    var x = 0
    var y = 0

    var count = 0
    while (y < h) {
        if (area[y][x % w] == '#') {
            count += 1
        }
        x += dx
        y += dy
    }
    return count
}

fun main() {
    val area = inputAreaMap()

    val dx = 3
    val dy = 1

    println(countTreeEncounters(area, dx, dy))
}
