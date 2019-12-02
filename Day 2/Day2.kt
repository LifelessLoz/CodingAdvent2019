/**
 * Advent of Code 2019, Day 2 - 1202 Program Alarm
 *
 * Problem solved by - Lawrence Barraclough
 */

package Day2

import java.io.File
import java.lang.Exception

class Day2 (rawInput: String) {
    private val inputs: List<Int> = rawInput.split(",").map { it.toInt() }
    private var input = inputs.toMutableList()
    private val targetNumber = 19690720
    private var nounVerbInput = 0

    fun part1(): Int {
        input[1] = 12
        input[2] = 2
        for (i in input.indices step 4) {
            if (input[i] == 1)
                input[input[i+3]] = input[input[i+1]] + input[input[i+2]]
            else if (input[i] == 2)
                input[input[i+3]] = input[input[i+1]] * input[input[i+2]]
            else if (input[i] == 99)
                break
            else
                throw Exception("Unexpected Value!")
        }
        return(input[0])
    }

    fun part2(): Int {
        start@ for(noun in 0..100) {
            for (verb in 0..100) {
                input = inputs.toMutableList()
                input[1] = noun
                input[2] = verb
                for (i in input.indices step 4) {
                    if (input[i] == 1)
                        input[input[i + 3]] = input[input[i + 1]] + input[input[i + 2]]
                    else if (input[i] == 2)
                        input[input[i + 3]] = input[input[i + 1]] * input[input[i + 2]]
                    else if (input[i] == 99)
                        break
                    else
                        throw Exception("Unexpected Value!")

                }
                println(100 * noun + verb)
                if (input[0] == targetNumber){
                    nounVerbInput = 100 * noun + verb
                    break@start
                }
            }
        }
        return(nounVerbInput)
    }
}

fun readFile(fileName: String)
        = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)

fun main(){
    val output = Day2(readFile("C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day2\\Input.txt")).part1()
    println(output)
    val output2 = Day2(readFile("C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day2\\Input.txt")).part2()
    println(output2)
}