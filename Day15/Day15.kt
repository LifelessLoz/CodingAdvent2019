/**
 * Advent of Code 2019, Day 15 - Oxygen System
 *
 * Problem solved by - Lawrence Barraclough
 */

package Day15

import java.io.File
import java.lang.Exception
import kotlin.math.pow

enum class IntcodeState{
    Running, Halted, Terminated
}

class IntcodeComputer(val input: LongArray) {
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

class RepairRobot(val input: LongArray, private val controller: Boolean){
    var i = 0L
    private var robotX = 21
    private var robotY = 21
    private var lastMovement = 0L
    private var currentDirection = 0L
    private var hasMoved = false
    private var foundOxygenSystem = false
    var mapOfPoints = Array(42){Array(42){" "}}
    private val robotComp = IntcodeComputer(input)

    private fun inputMovement(): Long{
        when (readLine()){
            "w" -> lastMovement = 1
            "a" -> lastMovement = 3
            "s" -> lastMovement = 2
            "d" -> lastMovement = 4
            else -> {
                println("Invalid input : ${readLine()} please try again")
                inputMovement()
            }
        }
        return lastMovement
    }

    //****************************************************************************************************************//
    //Implementation of wall follower algorithm using right hand rule
    private fun autoMovement(): Long{
        if (lastMovement == 0L)          lastMovement = 3
        if (hasMoved){
            if      (lastMovement == 3L) lastMovement = 2
            else if (lastMovement == 1L) lastMovement = 3
            else if (lastMovement == 4L) lastMovement = 1
            else if (lastMovement == 2L) lastMovement = 4
        }
        else if(!hasMoved && testDir(currentDirection, lastMovement))
            lastMovement = currentDirection
        else if(!hasMoved && !testDir(currentDirection, lastMovement)){
            lastMovement = changeDir(lastMovement)
            currentDirection = lastMovement
        }
        return lastMovement
    }

    private fun changeDir(lastMov: Long): Long{
        return if   (lastMov == 1L) 4
        else if     (lastMov == 4L) 2
        else if     (lastMov == 2L) 3
        else if     (lastMov == 3L) 1
        else        lastMov
    }

    private fun testDir(currDir: Long, lastMov: Long): Boolean{
        return  currDir == 1L && lastMov == 3L ||
                currDir == 4L && lastMov == 1L ||
                currDir == 2L && lastMov == 4L ||
                currDir == 3L && lastMov == 2L
    }
    //****************************************************************************************************************//

    private fun outputMovement(resultOfMovement: Long){
        when (resultOfMovement){
            0L -> {
                foundWall(lastMovement)
                hasMoved = false
            }
            1L -> {
                move(lastMovement)
                hasMoved = true
            }
            2L -> {
                move(lastMovement)
                foundOxygenSystem = true
            }
            else -> throw Exception("Error output : $resultOfMovement not recognised")
        }
    }

    private fun move(input: Long){
        when(input){
            1L -> robotY--
            2L -> robotY++
            3L -> robotX--
            4L -> robotX++
        }
    }

    private fun foundWall(input: Long){
        when(input){
            1L -> mapOfPoints[robotY-1][robotX] = "#"
            2L -> mapOfPoints[robotY+1][robotX] = "#"
            3L -> mapOfPoints[robotY][robotX-1] = "#"
            4L -> mapOfPoints[robotY][robotX+1] = "#"
        }
    }

    private fun paint(input: Long){
        mapOfPoints[robotY][robotX] = "."
        outputMovement(input)
        mapOfPoints[robotY][robotX] = "D"
        if (foundOxygenSystem)
            mapOfPoints[robotY][robotX] = "N"
    }

    fun robot(){
        do {
            if (controller)
                robotComp.numInput.add(inputMovement())
            else
                robotComp.numInput.add(autoMovement())
            robotComp.runIntcode()
            paint(robotComp.output.last())
            i++
        } while (i < 10000) //Change to while !foundOxygenSystem to find quickest way to end
        mapOfPoints[21][21] = "S"
        for(i in mapOfPoints.indices){
            for(j in mapOfPoints.indices)
                print(mapOfPoints[i][j])
            print("\n")
        }
    }
}

//Need to implement Dijkstra's or A* algorithm
class Dijkstra(){

}

fun readFile(fileName: String)
        = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)

fun main(){
    val fileName = """C:\Users\lawre\IdeaProjects\Advent of code\src\Day15\Input.txt"""
    val robotInput = Day15.readFile(fileName).split(",").map { it.toLong() }.toLongArray()

    val humanControl = false
    val test = RepairRobot(robotInput, humanControl)
    test.robot()
}