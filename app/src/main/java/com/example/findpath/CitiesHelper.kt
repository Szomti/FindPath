package com.example.findpath

class CitiesHelper {

    fun distanceInit(graph: MutableList<String>): MutableList<MutableList<Int>> {
        val size = graph.size
        val tempGraph: MutableList<MutableList<Int>> = MutableList(size) {MutableList(size) {5000}}
        for (i in 0 until size){
            tempGraph[i][i] = 0
        }
        println(tempGraph)
        return tempGraph
    }

    fun randomDistance(graph: MutableList<MutableList<Int>>): MutableList<MutableList<Int>>{
        val graphSize = graph.size
        for(i in 0 until graphSize){
            for(j in 0 until graphSize){
                val randomDistance = (25..200).random()
                graph[i][j] = randomDistance
                graph[j][i] = randomDistance
                graph[i][i] = 0
                graph[j][j] = 0
            }
        }
        return graph
    }
}