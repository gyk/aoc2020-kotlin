sealed class Instruction {
    data class Nop(val operand: Int) : Instruction()
    data class Acc(val number: Int) : Instruction()
    data class Jmp(val offset: Int) : Instruction()

    companion object {
        fun parse(s: String): Instruction? {
            val (instructionS, operandS) = s.split(' ', limit = 2)
            val operand = operandS.toIntOrNull() ?: return null
            return when (instructionS) {
                "nop" -> Nop(operand)
                "acc" -> Acc(operand)
                "jmp" -> Jmp(operand)
                else -> null
            }
        }
    }
}

sealed class ExecResult {
    data class Halt(val result: Int) : ExecResult()
    data class Loop(val lastResult: Int) : ExecResult()
}

class Machine(var pc: Int = 0, var acc: Int = 0) {
    fun execute(
        instructions: MutableList<Instruction>,
        accessed: BooleanArray = BooleanArray(instructions.size) { false },
        hacked: Boolean = false,
    ): ExecResult {
        while (true) {
            if (pc >= instructions.size) {
                return ExecResult.Halt(acc)
            }
            if (accessed[pc]) {
                return ExecResult.Loop(acc)
            }

            val oldPc = pc
            when (val ins = instructions[pc]) {
                is Instruction.Nop -> {
                    if (hacked) {
                        instructions[pc] = Instruction.Jmp(ins.operand)

                        val forked = Machine(pc, acc)
                        when (val res = forked.execute(instructions, accessed.clone())) {
                            is ExecResult.Halt -> return res
                            else -> {}
                        }

                        instructions[pc] = Instruction.Nop(ins.operand)
                    }

                    ++pc
                }
                is Instruction.Acc -> {
                    acc += ins.number
                    ++pc
                }
                is Instruction.Jmp -> {
                    if (hacked) {
                        instructions[pc] = Instruction.Nop(ins.offset)

                        val forked = Machine(pc, acc)
                        when (val res = forked.execute(instructions, accessed.clone())) {
                            is ExecResult.Halt -> return res
                            else -> {}
                        }

                        instructions[pc] = Instruction.Jmp(ins.offset)
                    }

                    pc += ins.offset
                }
            }

            accessed[oldPc] = true
        }
    }
}

fun main() {
    val instructions = generateSequence { readLine() }.map { Instruction.parse(it)!! }.toMutableList()
    println((Machine().execute(instructions) as ExecResult.Loop).lastResult)
}
