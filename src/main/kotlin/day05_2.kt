fun main() {
    val lines = generateSequence { readLine() }
    val taken = lines.map { computeSeatId(locateSeat(it)) }.toSet()
    val vacancy = (0 until computeSeatId(128 to 0)).dropWhile { it !in taken }.dropWhile { it in taken }.first()
    println(vacancy)
}
