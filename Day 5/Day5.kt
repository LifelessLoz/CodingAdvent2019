/**
 * Advent of Code 2019, Day 5 - Sunny with a Chance of Asteroids
 *
 * Problem solved by - Lawrence Barraclough
 */

package Day5

import java.io.File
import java.lang.Exception

class Day2 (rawInput: String) {
    private val inputs: List<Int> = rawInput.split(",").map { it.toInt() }.toMutableList()
    var input = inputs.toMutableList()
    var i = 0

    fun pointer(numInput: Int): Int {
        var output = 0
        while (true) {
            when (input[i] % 100){
                1 -> {
                    input[input[i + 3]] = parameters(1) + parameters(2)
                    i += 4
                }
                2 -> {
                    input[input[i + 3]] = parameters(1) * parameters(2)
                    i += 4
                }
                3 -> {
                    input[input[i + 1]] = numInput
                    i += 2
                }
                4 -> {
                    output = input[input[i + 1]]
                    i += 2
                }
                5 -> {
                    if(parameters(1) != 0)
                        i = parameters(2)
                    else
                        i += 3
                }
                6 -> {
                    if(parameters(1) == 0)
                        i = parameters(2)
                    else
                        i += 3
                }
                7 -> {
                    if(parameters(1) < parameters(2))
                        input[input[i + 3]] = 1
                    else
                        input[input[i + 3]] = 0
                    i += 4
                }
                8 -> {
                    if(parameters(1) == parameters(2))
                        input[input[i + 3]] = 1
                    else
                        input[input[i + 3]] = 0
                    i += 4
                }
                99 -> return output
                else -> throw Exception("Error, Unexpected Value!")
            }
        }
        return(0)
    }

    private fun parameters(decider: Int): Int{
        var output = 0
        if (decider == 1) {
            output = if (input[i] / 100 % 10 == 1){
                input[i + decider]
            } else
                input[input[i + decider]]
        }
        if (decider == 2) {
            output = if (input[i] / 1000 % 10 == 1){
                input[i + decider]
            } else
                input[input[i+ decider]]
        }
        return output
    }
}

fun readFile(fileName: String)
        = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)

fun main(){
    var fileName = "C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day5\\Input.txt"
    val part1 = Day2(readFile(fileName)).pointer(1  )
    println("Output for part 1 : $part1")
    val part2 = Day2(readFile(fileName)).pointer(5  )
    println("Output for part 2 : $part2")
}

