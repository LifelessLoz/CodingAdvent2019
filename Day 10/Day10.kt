/**
 * Advent of Code 2019, Day 10 - Monitoring Station
 *
 * Problem solved by - Lawrence Barraclough
 */

package Day10

import java.io.File

class Day10(rawInput: List<String>){
    val input = rawInput.map{it.toCharArray().toList()}

    fun part1(){
        var test = asteroidPosit(input)
        print(inline(test))
    }

    fun asteroidPosit(inputMap: List<List<Char>>): List<Pair<Int, Int>>{
        var output = mutableListOf<Pair<Int, Int>>()
        for (i in input.indices){
            for (j in input.indices) {
                if (input[i][j] == '#')
                    output.add(Pair(j, i))
            }
        }
        return output
    }

    fun inline(asteroids: List<Pair<Int, Int>>): Int{
        var totAngles = mutableListOf<Int>()
        for (i in asteroids.indices){
            var angles = mutableListOf<Double>()
            for (j in asteroids.indices){
                angles.add(kotlin.math.atan2((asteroids[i].first - asteroids[j].first).toDouble(), (asteroids[i].second - asteroids[j].second).toDouble()))
            }
            totAngles.add(angles.distinct().size)
        }
        return totAngles.max()!!
    }
}

fun readFile(fileName: String): List<String>
        = File(fileName).readLines()

fun main(){
    val fileName = "C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day10\\Input.txt"
    Day10(readFile(fileName)).part1()
}