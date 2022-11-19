fun main() {
    val lines = generateSequence { readLine() }
    val count = lines.map { parsePasswordLine(it) }.count {
        it?.let {
            (it.password[it.d1 - 1] == it.char) xor (it.password[it.d2 - 1] == it.char)
        } ?: false
    }
    println(count)
}
