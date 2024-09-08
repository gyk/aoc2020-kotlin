private fun findDestination(cups: List<Int>, value: Int): Int {
    for (x in value downTo 1) {
        val i = cups.indexOf(x)
        if (i != -1) {
            return i
        }
    }
    return cups.indices.maxBy { cups[it] }
}

fun arrangeCups(cups0: List<Int>, nMoves: Int): List<Int> {
    var cups = cups0
    var curr = 0
    repeat(nMoves) {
        val currLabel = cups[curr]
        cups = cups.subList(curr, cups.size) + cups.subList(0, curr)
        val pickedUp = cups.subList(1, 3 + 1)
        cups = cups.subList(0, 1) + cups.subList(3 + 1, cups.size)
        val dest = findDestination(cups, currLabel - 1)
        cups = cups.subList(0, dest + 1) + pickedUp + cups.subList(dest + 1, cups.size)
        curr = (cups.indexOf(currLabel) + 1) % cups.size
    }
    return cups
}

fun main() {
    val cups = readln().map { char -> char.digitToInt() }
    val res = arrangeCups(cups, 100)
    val k = res.indexOf(1)
    for (i in (k + 1)..<(k + res.size)) {
        print("${res[i % res.size]}")
    }
}
