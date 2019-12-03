package Day3

import java.io.File
import java.util.*
import kotlin.math.absoluteValue

class Day3 (rawInput: List<String>){
    private val input: List<String> = rawInput
    private val firstWire: List<Pair<Char, Int>> = input[0].split(",").map { it[0] to it.substring(1).toInt() }
    private val secondWire: List<Pair<Char, Int>> = input[1].split(",").map { it[0] to it.substring(1).toInt() }
    private val wirePoints1 = points(firstWire)
    private val wirePoints2 = points(secondWire)

    private fun points(wireDirections: List<Pair<Char, Int>>): List<Pair<Int, Int>>{
        var x = 0
        var y = 0
        val wirePoints = mutableListOf<Pair<Int, Int>>()

        for (wireDirection in wireDirections){
            when (wireDirection.first) {
                'R' -> for (i in 1..wireDirection.second){
                    x++
                    wirePoints.add(element = Pair(x , y))
                }
                'L' -> for (i in 1..wireDirection.second){
                    x--
                    wirePoints.add(element = Pair(x , y))
                }
                'U' -> for (i in 1..wireDirection.second){
                    y++
                    wirePoints.add(element = Pair(x , y))
                }
                'D' -> for (i in 1..wireDirection.second){
                    y--
                    wirePoints.add(element = Pair(x , y))
                }
                else -> println("Error direction not found")
            }
        }
        return wirePoints
    }

   private fun intersections(wire1s: List<Pair<Int, Int>>, wire2s: List<Pair<Int, Int>>): Pair<Int, Int> {
        val distFromCenters = mutableListOf<Int>()
        val intersectionDistance = mutableListOf<Int>()
        for (wire1 in wire1s){
            if (wire2s.contains(wire1)){
                distFromCenters.add(wire1.first.absoluteValue + wire1.second.absoluteValue)
                // Adding 2 to the intersection distance as Lists start on index 0
                intersectionDistance.add(wire1s.indexOf(wire1) + wire2s.indexOf(wire1) + 2)
            }
        }
        return Pair(Collections.min(distFromCenters), Collections.min(intersectionDistance))
    }

    fun part1()= intersections(wirePoints1, wirePoints2).first

    fun part2()= intersections(wirePoints1, wirePoints2).second
}

fun readFile(fileName: String): List<String>
        = File(fileName).readLines()

fun main(){
    val fileName = "C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day3\\Input.txt"
    val output = Day3(readFile(fileName)).part1()
    print(output)
    val output2 = Day3(readFile(fileName)).part2()
    print(output2)
}