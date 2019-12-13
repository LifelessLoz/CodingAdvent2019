/**
 * Advent of Code 2019, Day 9 - Amplification Circuit
 *
 * Problem solved by - Lawrence Barraclough
*/
package Day9
import java.io.File
import java.lang.Exception
import kotlin.math.pow

class Day9 (rawInput: String, val numInput: MutableList<Long>) {
    private val inputs: List<Long> = rawInput.split(",").map { it.toLong() }
    var input = inputs.toLongArray()
    var memTest = LongArray(10000) {0}
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
            else -> error("bad mode $mode")
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

    fun pointer(): List<Long> {
        var i = 0
        var b = 0
        var output = mutableListOf<Long>()
        var running = true
        while (running) {
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
                    }
                    i += 2
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
                99 -> return output
                else -> throw Exception("Error, Unexpected Value!")
            }
        }
        return output
    }
}

fun readFile(fileName: String)
        = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)

fun main(){
    val fileName = "C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day9\\Input.txt"
    var input1 = MutableList(1){1L}
    var input2 = MutableList(1){2L}
    println(Day9(readFile(fileName),input1).pointer())
    println(Day9(readFile(fileName),input2).pointer())
}