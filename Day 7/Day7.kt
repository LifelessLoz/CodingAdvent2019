/**
 * Advent of Code 2019, Day 7 - Amplification Circuit
 *
 * Problem solved by - Lawrence Barraclough
 */

package Day7

import java.io.File
import java.lang.Exception

class Day7 (rawInput: String) {
    private val inputs: List<Int> = rawInput.split(",").map { it.toInt() }
    var input = inputs.toMutableList()

    fun pointer(numInput: Pair<Int, Int>): Int {
        var i = 0
        var swap = 0
        while (true) {
            when (input[i] % 100){
                1 -> {
                    input[input[i + 3]] = parameters(1, i) + parameters(2, i)
                    i += 4
                }
                2 -> {
                    input[input[i + 3]] = parameters(1, i) * parameters(2, i)
                    i += 4
                }
                3 -> {
                    if (swap == 0) {
                        input[input[i + 1]] = numInput.first
                        swap++
                    }
                    else
                        input[input[i + 1]] = numInput.second
                    i += 2
                }
                4 -> {
                    return input[input[i + 1]]
                }
                5 -> {
                    if(parameters(1, i) != 0)
                        i = parameters(2, i)
                    else
                        i += 3
                }
                6 -> {
                    if(parameters(1, i) == 0)
                        i = parameters(2, i)
                    else
                        i += 3
                }
                7 -> {
                    if(parameters(1, i) < parameters(2, i))
                        input[input[i + 3]] = 1
                    else
                        input[input[i + 3]] = 0
                    i += 4
                }
                8 -> {
                    if(parameters(1, i) == parameters(2, i))
                        input[input[i + 3]] = 1
                    else
                        input[input[i + 3]] = 0
                    i += 4
                }
                99 -> return input[input[i + 1]]
                else -> throw Exception("Error, Unexpected Value!")
            }
        }
    }

    private fun parameters(decider: Int, i: Int): Int{
        var output = 0
        var divideBy = 0
        if (decider == 1)
            divideBy = 100
        if (decider == 2)
            divideBy = 1000
        output = if (input[i] / divideBy % 10 == 1){
            input[i + decider]
        } else
            input[input[i + decider]]
        return output
    }

    fun ampCircuitTester(input: MutableList<List<Int>>): List<Int>{
        var output = mutableListOf<Int>()
        for (i in input.indices)
            output.add(ampCircuit(input[i]))
        return output
    }

    private fun ampCircuit(phaseSetting: List<Int>): Int{
        var output = 0
        for (i in phaseSetting.indices){
            output = pointer(Pair(phaseSetting[i], output) )
        }
        return output
    }
}

fun heapsAlgorithm(n: Int, listElements: MutableList<Int>, output: MutableList<List<Int>>){
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
    val intList = mutableListOf(0,1,2,3,4)
    var part1 = mutableListOf<List<Int>>()
    heapsAlgorithm(intList.size, intList, part1)
    val results = Day7(readFile(fileName)).ampCircuitTester(part1)
    print(results.max())
}