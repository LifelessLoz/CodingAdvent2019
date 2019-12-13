/**
 * Advent of Code 2019, Day 9 - Amplification Circuit
 *
 * Problem solved by - Lawrence Barraclough
*/
package Day9
import Day7.IntcodeState
import java.io.File
import java.lang.Exception
import kotlin.math.pow

enum class IntcodeState{
    Running, Halted, Terminated
}

class Day9 (val input: LongArray) {
    var memTest = LongArray(10000) {0}
    var currentState: IntcodeState = IntcodeState.Halted
    var numInput = mutableListOf<Long>()

    var i = 0
    var b = 0
    var output = mutableListOf<Long>()

    operator fun get(address: Int) = memTest[address]
    operator fun set(address: Int, value: Long) {
        memTest[address] = value
    }

    init {
        input?.copyInto(memTest)
    }
    var memory = memTest.toMutableList()

    private fun arg(i: Int, b: Int, n: Int): Int =
        when (val mode = memory[i].toInt() / 10.0.pow((n + 1).toDouble()).toInt() % 10) {
            0 -> memory[i + n].toInt()
            1 -> i + n
            2 -> b + memory[i + n].toInt()
            else -> error("Cannot recognise mode : $mode")
        }

    private operator fun List<Long>.get(i: Int, b: Int, n: Int): Long =
        memory.getOrElse(arg(i, b, n)) { 0 }

    private operator fun MutableList<Long>.set(i: Int, b: Int, n: Int, value: Long) {
        val addr = arg(i, b, n)
        if (addr < size) {
            memory[arg(i, b, n)] = value
        } else {
            if (this is ArrayList) {
                ensureCapacity(addr + 1)
            }
            addAll(List(addr - size) { 0L })
            add(value)
        }
    }

    fun pointer() {
        when (memory[i].toInt() % 100){
            1 -> {
                memory[i, b, 3] = memory[i, b, 1] + memory[i, b, 2]
                i += 4
            }
            2 -> {
                memory[i, b, 3] = memory[i, b, 1] * memory[i, b, 2]
                i += 4
            }
            3 -> {
                if (numInput.isNotEmpty()) {
                    memory[i, b, 1] = numInput[0]
                    numInput.removeAt(0)
                    currentState = IntcodeState.Running
                    i += 2
                }
                else currentState = IntcodeState.Halted
            }
            4 -> {
                output.add(memory[i, b, 1])
                i += 2
            }
            5 -> {
                if(memory[i, b, 1] != 0L)
                    i = memory[i, b, 2].toInt()
                else
                    i += 3
            }
            6 -> {
                if(memory[i, b, 1] == 0L)
                    i = memory[i, b, 2].toInt()
                else
                    i += 3
            }
            7 -> {
                if(memory[i, b, 1] < memory[i, b, 2])
                    memory[i, b, 3] = 1
                else
                    memory[i, b, 3] = 0
                i += 4
            }
            8 -> {
                if(memory[i, b, 1] == memory[i, b, 2])
                    memory[i, b, 3] = 1
                else
                    memory[i, b, 3] = 0
                i += 4
            }
            9 -> {
                b += memory[i, b, 1].toInt()
                i += 2
            }
            99 -> {
                currentState = IntcodeState.Terminated
                i += 1
            }
            else -> throw Exception("Error, Unexpected Value!")
        }
    }

    fun runIntcode(): MutableList<Long>{
        currentState = IntcodeState.Running
        while (currentState == IntcodeState.Running)
            pointer()
        return output
    }
}

fun readFile(fileName: String)
        = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)

fun main(){
    val fileName = "C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day9\\Input.txt"

    var memInput = readFile(fileName).split(",").map { it.toLong() }.toLongArray()

    val computer1 = Day9(memInput)
    computer1.numInput.add(1L)
    computer1.runIntcode()
    println(computer1.output)

    val computer2 = Day9(memInput)
    computer2.numInput.add(2L)
    computer2.runIntcode()
    println(computer2.output)
}