import java.lang.Exception

val PASSWORD_PATTERN = Regex("""(?<d1>\d+)-(?<d2>\d+)\s+(?<char>\w):\s*(?<password>\w*)""")

data class PasswordEntry(val d1: Int, val d2: Int, val char: Char, val password: String)

fun parsePasswordLine(line: String): PasswordEntry? {
    return try {
        val m = PASSWORD_PATTERN.find(line)!!.groups
        val d1 = m["d1"]!!.value.toIntOrNull()!!
        val d2 = m["d2"]!!.value.toIntOrNull()!!
        val char = m["char"]!!.value[0]
        val password = m["password"]!!.value

        PasswordEntry(d1, d2, char, password)
    } catch (e: NullPointerException) {
        null
    } catch (e: Exception) {
        throw e
    }
}

fun main() {
    val lines = generateSequence { readLine() }
    val count = lines.map { parsePasswordLine(it) }.count {
        it?.let {
            val count = it.password.count { char -> char == it.char }
            count in it.d1..it.d2
        } ?: false
    }
    println(count)
}
