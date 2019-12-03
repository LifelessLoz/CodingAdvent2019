/**
 * Advent of Code 2019, Day 1 - The Tyranny of the Rocket Equation
 *
 * Problem solved by - Lawrence Barraclough
 */

package Day1

import java.io.File

class Day1 (rawInput: List<String>){
    private val inputs: List<Int> = rawInput.map {it.toInt()}

    fun part1(): Int {
        var fuelCounterUpper = 0
        for (input in inputs){
            fuelCounterUpper += (input / 3) - 2
        }
        return fuelCounterUpper
    }

    fun part2(): Int {
        var fuelModulusCounterUpper = 0
        var fuelInput = 0
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
    val fileName = "C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day1\\Input.txt"
    println(Day1(readFile(fileName)).part1())
    println(Day1(readFile(fileName)).part2())
}