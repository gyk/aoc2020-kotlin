fun main() {
    val area = inputAreaMap()

    val dxdyList = listOf(
        Pair(1, 1),
        Pair(3, 1),
        Pair(5, 1),
        Pair(7, 1),
        Pair(1, 2),
    )

    println(dxdyList.map { (dx, dy) ->
        countTreeEncounters(area, dx, dy).toLong()
    }.reduce(Long::times))
}
