fun main() {
    val instructions = generateSequence { readLine() }.map { Instruction.parse(it)!! }.toMutableList()
    println((Machine().execute(instructions, hacked = true) as ExecResult.Halt).result)
}
