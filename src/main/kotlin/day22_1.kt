fun parseDeck(lines: Iterator<String>): ArrayDeque<Int> {
    val header = lines.next()
    require(header.startsWith("Player "))
    val cards = ArrayDeque<Int>()
    for (line in lines) {
        if (line.isBlank()) {
            break
        }
        cards += line.toInt()
    }
    return cards
}

fun playGame(players: List<ArrayDeque<Int>>): List<List<Int>> {
    while (!players.any { it.isEmpty() }) {
        val pair = (0..1).map { players[it].removeFirst() }
        val win = (0..1).maxBy { pair[it] }
        players[win] += listOf(pair[win], pair[1 - win])
    }

    return players.map { it.toList() }
}

fun main() {
    val lines = generateSequence { readlnOrNull() }.iterator()
    val players = (1..2).map { parseDeck(lines) }
    val outcome = playGame(players)
    val res = outcome.first { it.isNotEmpty() }
        .reversed()
        .zip(generateSequence(1) { it + 1 }.asIterable())
        .fold(0) { acc, pair -> acc + pair.first * pair.second }
    println("$res")
}
