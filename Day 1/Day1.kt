/**
 * Advent of Code 2019, Day 1 - The Tyranny of the Rocket Equation
 *
 * Problem solved by - Lawrence Barraclough
 */

package Day1

import java.io.File

class Day1 (rawInput: List<String>){
    private val inputs: List<Int> = rawInput.map {it.toInt()}
    private var fuelCounterUpper: Int = 0
    private var fuelModulusCounterUpper: Int = 0
    private var fuelInput: Int = 0

    fun part1(): Int {
        for (input in inputs){
            fuelCounterUpper += (input / 3) - 2
        }
        return fuelCounterUpper
    }

    fun part2(): Int {
        for (input in inputs){
            fuelInput = input
            while (fuelInput > 0){
                fuelInput = (fuelInput / 3) - 2
                if (fuelInput > 0)
                    fuelModulusCounterUpper += fuelInput
            }
        }
        return fuelModulusCounterUpper
    }
}

fun readFile(fileName: String): List<String>
        = File(fileName).readLines()

fun main(){
    val total: Int = Day1(readFile("C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day1\\Input.txt")).part1()
    println(total)
    val total2: Int = Day1(readFile("C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day1\\Input.txt")).part2()
    println(total2)
}