/**
 * Advent of Code 2019, Day 11 - Space Police
 *
 * Problem solved by - Lawrence Barraclough
 */

import java.io.File
import java.lang.Exception
import kotlin.math.pow

enum class IntcodeState{
    Running, Halted, Terminated
}

class Day11 (val input: LongArray) {
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
            9 -> {
                b += memory[i, b, 1].toInt()
                i += 2
            }
            99 -> {
                currentState = IntcodeState.Terminated
                i += 1
            }
            else -> throw Exception("Error, Unexpected Value : ${memory[i].toInt()}")
        }
    }

    fun runIntcode(): MutableList<Long>{
        currentState = IntcodeState.Running
        while (currentState == IntcodeState.Running)
            pointer()
        return output
    }
}

class PaintingRobot(rawInput: LongArray, initialInput: Long){
    private var currentAngle = 0
    private var currentPointX = 150
    private var currentPointY = 100
    var mapOfPoints = Array(200){Array(300){"."}}
    private var paintSteps = 0
    private val computer = Day11(rawInput)
    init{
        computer.numInput.add(initialInput)
    }

    private fun paint(input: Long){
        if (mapOfPoints[currentPointY][currentPointX] == ".")
            paintSteps++
        when (input){
            0L -> {
                mapOfPoints[currentPointY][currentPointX] = ","
            }
            1L -> {
                mapOfPoints[currentPointY][currentPointX] = "X"
            }
            else -> throw Exception("Error, unexpected paint value : $input")
        }
    }

    private fun moveRobot(input: Long){
        when (input){
            0L -> {
                if (currentAngle == 0)
                    currentAngle = 270
                else
                    currentAngle -= 90
            }
            1L -> {
                if (currentAngle == 270)
                    currentAngle = 0
                else
                    currentAngle += 90
            }
            else -> throw Exception("Error, unexpected move input value : $input")
        }
        when(currentAngle){
            0 -> currentPointY++
            90 -> currentPointX++
            180 -> currentPointY--
            270 -> currentPointX--
            else -> throw Exception("Error, unexpected angle value : $currentAngle")
        }
    }

    fun runRobot(): Int{
        do{
            computer.runIntcode()
            if (computer.currentState == IntcodeState.Halted){
                val col = computer.output.removeAt(0)
                val move = computer.output.removeAt(0)
                paint(col)
                moveRobot(move)
            }
            if(mapOfPoints[currentPointY][currentPointX] == "," || mapOfPoints[currentPointY][currentPointX] == ".")
                computer.numInput.add(0)
            else
                computer.numInput.add(1)
        }while (computer.currentState != IntcodeState.Terminated)
        return paintSteps
    }
}

fun readFile(fileName: String)
        = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)

fun main(){
    val fileName = """C:\Users\lawre\IdeaProjects\Advent of code\src\Day11\Input.txt"""

    val memInput = readFile(fileName).split(",").map { it.toLong() }.toLongArray()

    val robot = PaintingRobot(memInput, 0L)

    println(robot.runRobot())

    for(i in robot.mapOfPoints.indices){
        for(j in robot.mapOfPoints.indices)
            print(robot.mapOfPoints[i][j])
        print("\n")
    }

    val robot2 = PaintingRobot(memInput, 1L)

    println(robot2.runRobot())

    for(i in robot2.mapOfPoints.indices){
        for(j in robot2.mapOfPoints.indices)
            print(robot2.mapOfPoints[i][j])
        print("\n")
    }
}