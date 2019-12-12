package Day12

import java.io.File
import java.lang.reflect.Array.set
import kotlin.math.abs
import kotlin.math.sign

class Day12(rawInput: String){
    var input: MutableList<MutableList<Int>> = mutableListOf(
        mutableListOf(-8, -9, -7),
        mutableListOf(-5, 2, -1),
        mutableListOf(11, 8, -14),
        mutableListOf(1, -4, -11))

    var testInput: MutableList<MutableList<Int>> = mutableListOf(
        mutableListOf(-8, -10, 0),
        mutableListOf(5, 5, 10),
        mutableListOf(2, -7, 3),
        mutableListOf(9, -8, -3))

    var velocity: MutableList<MutableList<Int>> = mutableListOf(
        mutableListOf(0, 0, 0),
        mutableListOf(0, 0, 0),
        mutableListOf(0, 0, 0),
        mutableListOf(0, 0, 0))

    var testIt: List<Int> = listOf(1, 2, 3, 5)

    private fun step(){
        for (i in input.indices){
            for (j in input.indices) {
                for (k in 0 until 3){
                    if (input[i][k] > input[j][k]) {
                        velocity[i][k]--
                    }
                    if (input[i][k] < input[j][k]){
                        velocity[i][k]++
                    }
                }
            }
        }
        for (i in input.indices){
            for (k in 0 until 3){
                input[i][k] = input[i][k] + velocity[i][k]
            }
        }
    }

    fun iterateSteps(): Int{
        for (i in 0 until 1000)
            step()
        return totalEnergy(velocity, input)
    }

    private fun totalEnergy(inputPosit: List<List<Int>>, inputVel: List<List<Int>>): Int {
        var output = 0
        for (i in inputPosit.indices){
            output += absSum(inputPosit[i]) * absSum(inputVel[i])
        }
        return output
    }

    private fun absSum(input: List<Int>): Int{
        var output = 0
        input.forEach{
            output += abs(it)
        }
        return output
    }
}

fun readFile(fileName: String): List<String>
        = File(fileName).readLines()

fun readFileString(fileName: String)
        = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)

fun main(){
    val fileName = "C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day12\\Input.txt"
    println(Day12(readFileString(fileName)).iterateSteps())
}