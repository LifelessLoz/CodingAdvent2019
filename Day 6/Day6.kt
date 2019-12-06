/**
 * Advent of Code 2019, Day 6 - Universal Orbit Map
 *
 * Problem solved by - Lawrence Barraclough
 */

package Day6

import java.io.File

class Day6 (rawInput: List<String>){
    private val inputs = rawInput.map{ it.split(")")}.map{it[1] to it[0]}.toMap()

    private fun orbitPath(pointer: String) : List<String> {
        return if(pointer == "COM")
            listOf("COM")
        else
            orbitPath(inputs.getValue(pointer)) + pointer
    }

    private fun pathDistance(object1: String, object2: String) : Int {
        val path1 = orbitPath(object1)
        val path2 = orbitPath(object2)
        var i = 0
        while(path1[i] == path2[i]){
            i++
        }
        return (path1.size + path2.size - (2*i) - 2)
    }

    fun part1() = inputs.keys.map { orbitPath(it).size-1 }.sum()
    fun part2() = pathDistance("YOU", "SAN")
}

fun readFile(fileName: String): List<String>
        = File(fileName).readLines()

fun main(){
    val fileName = "C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day6\\Input.txt"
    println(Day6(readFile(fileName)).part1())
    println(Day6(readFile(fileName)).part2())
}