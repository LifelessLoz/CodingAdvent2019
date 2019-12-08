/**
 * Advent of Code 2019, Day 8 - Space Image Format
 *
 * Problem solved by - Lawrence Barraclough
 */

package Day8

import java.io.File

class Day8 (rawInput: String, height: Int, width: Int){
    private val h = height
    private val w = width
    private val input = rawInput.chunked(width*height)
    private var output = CharArray(width*height) {'3'}

    fun answer() = Pair(part1(input), part2(input, w, h, output))
}

fun part1(input: List<String>): Int{
    return input.minBy { it -> it.count() {it=='0'} }?.let { it -> it.count() {it=='1'} * it.count() {it=='2'} }!!
}

fun part2(input: List<String>, w: Int, h: Int, output: CharArray): String{
    for (i in input.indices){
        for(j in 0 until w*h){
            if((input[i][j] == '0' || input[i][j] == '1') && output[j] == '3'){
                output[j] = input[i][j]
            }
        }
    }
    return output.map { if(it == '1') 'X' else ' '}.chunked(25).map { it.joinToString("") }.joinToString("\n")
}

fun readFile(fileName: String): String
        = File(fileName).bufferedReader().readLine()

fun main(){
    val fileName = "C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day8\\Input.txt"
    val answer = Day8(readFile(fileName), 6, 25).answer()
    println(answer.first)
    println(answer.second)
}