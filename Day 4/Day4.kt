/**
 * Advent of Code 2019, Day 4 - Secure Container
 *
 * Problem solved by - Lawrence Barraclough
 */

package Day4

class Day4(){

   fun answer(lowRange: Int, topRange: Int): Pair<Int, Int>{
       var part1 = 0
       var part2 = 0

       for (i in lowRange..topRange){
           val currentNumCharArray = i.toString().map { it }
           if(notDecreasing(currentNumCharArray)){
               val add = containsDouble(currentNumCharArray)
               if (add.first)
                   part1++
               if (add.second)
                   part2++
           }
       }
       return Pair(part1, part2)
   }

    private fun notDecreasing(numCharArray: List<Char>): Boolean{
        return numCharArray == numCharArray.sorted()
    }

    private fun containsDouble(numCharArray: List<Char>): Pair<Boolean, Boolean>{
        var containsDouble = false
        var containsDuplicates = false
        val check = numCharArray.groupBy { it }
        for ((k,v) in check){
            if (v.size >= 2)
                containsDuplicates = true
            if (v.size == 2)
                containsDouble = true
        }
        return Pair(containsDuplicates, containsDouble)
    }

}

fun main(){
    println(Day4().answer(307237, 769058))
}