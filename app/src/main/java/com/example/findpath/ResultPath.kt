package com.example.findpath

class ResultPath(
    private val distance: Int,
    private  val path: MutableList<Int>
) {
    fun getDistance(): Int {
        return distance
    }

    fun getPath(): MutableList<Int> {
        val correctedPath: MutableList<Int> = mutableListOf()
        for(element in path){
            correctedPath.add(element-1)
        }
        return correctedPath
    }
}