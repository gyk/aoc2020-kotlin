fun main() {
    val startingNumbers = readlnOrNull()!!
        .split(',')
        .map { it.toInt() }
    println("${recite(startingNumbers, 30000000)}")
}
