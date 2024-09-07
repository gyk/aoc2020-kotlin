fun playGameR(players: List<ArrayDeque<Int>>): Int {
    val hashes = mutableSetOf<Int>()
    while (!players.any { it.isEmpty() }) {
        val h = players.map { it.toList().hashCode() }.hashCode()
        if (!hashes.add(h)) return 0

        val pair = (0..1).map { players[it].removeFirst() }
        val win = if ((0..1).all { pair[it] <= players[it].size }) {
            playGameR((0..1).map { ArrayDeque(players[it].take(pair[it])) })
        } else {
            (0..1).maxBy { pair[it] }
        }
        players[win] += listOf(pair[win], pair[1 - win])
    }
    return (0..1).first { players[it].isNotEmpty() }
}

fun main() {
    val lines = generateSequence { readlnOrNull() }.iterator()
    val players = (0..1).map { parseDeck(lines) }
    val win = playGameR(players)
    val res = players[win]
        .reversed()
        .zip(generateSequence(1) { it + 1 }.asIterable())
        .fold(0) { acc, pair -> acc + pair.first * pair.second }
    println("$res")
}
