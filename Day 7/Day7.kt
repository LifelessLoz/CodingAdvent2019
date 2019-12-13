/**
 * Advent of Code 2019, Day 7 - Amplification Circuit
 *
 * Problem solved by - Lawrence Barraclough
 */

package Day7

import java.io.File
import java.lang.Exception
import kotlin.math.pow

enum class IntcodeState{
    Running, Halted, Terminated
}

class Day7 (val input: LongArray) {
    private var memTest = LongArray(10000) {0}
    var currentState = IntcodeState.Halted
    var numInput = mutableListOf<Long>()

    private var i = 0
    private var b = 0
    var output = mutableListOf<Long>()

    operator fun get(address: Int) = memTest[address]
    operator fun set(address: Int, value: Long) {
        memTest[address] = value
    }

    init {
        input.copyInto(memTest)
    }
    private var memory = memTest.toMutableList()

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

    private fun pointer() {
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

class amplifier(val rawInput: String, val input: List<Long>, repeat: Boolean){
    private val amps: List<Day7>
    var output = mutableListOf<Long>()
    private val intcodeInput = rawInput.split(",").map { it.toLong() }.toLongArray()
    private val numInput = input.toMutableList()

    init {
        amps = arrayListOf (Day7(intcodeInput),
                            Day7(intcodeInput),
                            Day7(intcodeInput),
                            Day7(intcodeInput),
                            Day7(intcodeInput))
        for (i in 0..3) {
            amps[i + 1].numInput = amps[i].output
            amps[i].output.add(numInput[i + 1])
        }
        if (repeat) {
            amps[0].numInput = amps[4].output
            amps[4].output.add(numInput[0])
            amps[4].output.add(0)
        } else {
            amps[0].numInput.add(numInput[0])
            amps[0].numInput.add(0)
        }
    }

    fun runAmps() {
        do {
            amps.forEach { it.runIntcode() }
        } while (!amps.all { it.currentState == IntcodeState.Terminated })
        output = amps[4].output
    }
}

fun heapsAlgorithm(n: Int, listElements: MutableList<Long>, output: MutableList<List<Long>>){
    if (n == 1) {
        output.add(listElements.toList())
    }
    else {
        for(i in 0 until n){
            heapsAlgorithm(n-1, listElements, output)
            if(i < n-1){
                if (n % 2 == 0){
                    listElements[i] = listElements[n-1].also { listElements[n-1] = listElements[i] }
                }
                else{
                    listElements[0] = listElements[n-1].also { listElements[n-1] = listElements[0] }
                }
            }
        }
    }
}

fun readFile(fileName: String)
        = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)

fun main(){
    val fileName = "C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day7\\Input.txt"

    val intList1 = mutableListOf(0L,1,2,3,4)
    val intList2 = mutableListOf(5L,6,7,8,9)
    val part1 = mutableListOf<List<Long>>()
    val part2 = mutableListOf<List<Long>>()
    heapsAlgorithm(intList1.size, intList1, part1)
    heapsAlgorithm(intList2.size, intList2, part2)

    println(part1.map {
        val thruster1 = amplifier(readFile(fileName), it, false)
        thruster1.runAmps()
        it to thruster1.output.joinToString("").toInt()
    }.maxBy{it.second})

    println(part2.map {
        val thruster2 = amplifier(readFile(fileName), it, true)
        thruster2.runAmps()
        it to thruster2.output.joinToString("").toInt()
    }.maxBy{it.second})
}