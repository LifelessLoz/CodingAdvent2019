/**
 * Advent of Code 2019, Day 10 - Monitoring Station
 *
 * Problem solved by - Lawrence Barraclough
 */

package Day10

import java.io.File
import java.text.FieldPosition

class Day10(rawInput: List<String>){
    val input = rawInput.map{it.toCharArray().toList()}

    fun answer(): Pair<Int, Int>{
        var test = asteroidPosit(input)
        return Pair(inline(test).first, laserDestroy(test, inline(test).second))
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

    fun inline(asteroids: List<Pair<Int, Int>>): Pair<Int, Int>{
        var totAngles = mutableListOf<Int>()
        for (i in asteroids.indices){
            var angles = mutableListOf<Double>()
            for (j in asteroids.indices){
                angles.add(kotlin.math.atan2((asteroids[i].first - asteroids[j].first).toDouble(), (asteroids[i].second - asteroids[j].second).toDouble()))
            }
            totAngles.add(angles.distinct().size)

        }
        return Pair(totAngles.max()!!, totAngles.indexOf(totAngles.max()!!))
    }

    fun laserDestroy(asteroids: List<Pair<Int, Int>>, asteroidPosition: Int): Int{
        var angles = mutableListOf<Double>()
        var k = 0
        var aster = 0.0
        for (i in asteroids.indices)
            angles.add(kotlin.math.atan2((asteroids[i].first - asteroids[asteroidPosition].first).toDouble(), (asteroids[i].second - asteroids[asteroidPosition].second).toDouble()))
        for (i in 0 until 200) {
            while (angles.sortedByDescending { it }[k] == aster)
                k++
            aster = angles.sortedByDescending { it }[k]
        }
        return (asteroids[angles.indexOf(aster)].first * 100 + asteroids[angles.indexOf(aster)].second)
    }
}

fun readFile(fileName: String): List<String>
        = File(fileName).readLines()

fun main(){
    val fileName = "C:\\Users\\lawre\\IdeaProjects\\Advent of code\\src\\Day10\\Input.txt"
    val answer = Day10(readFile(fileName)).answer()
    println(answer.first)
    println(answer.second)
}