package com.example.findpath

import java.util.*

internal class SimpleTSP(private val graph: MutableList<MutableList<Int>>) {
    private val mDistanceMatrix: MutableList<MutableList<Int>> = graph
    private val mVisitedCities: BooleanArray = BooleanArray(graph.size)
    private var mStartAndEndTownIndex = 0
    private val mCurrentPath: IntArray = IntArray(graph.size)
    private var bestPath: IntArray = IntArray(graph.size)
    var bestPathCosts = 0f
        private set

    private fun runTSP(pStartAndEndTownIndex: Int): ResultPath {
        mStartAndEndTownIndex = pStartAndEndTownIndex
        Arrays.fill(mVisitedCities, false)
        mVisitedCities[pStartAndEndTownIndex] = true
        mCurrentPath[0] = pStartAndEndTownIndex
        bestPathCosts = Float.MAX_VALUE
        TSP(pStartAndEndTownIndex, 1, 0f)
        printBestPath()
        return ResultPath(bestPathCosts.toInt(), bestPath)
    }

    private fun TSP(pCurrentCity: Int, pCityCounter: Int, pCurrentTotalCost: Float): Float {
        if (pCityCounter >= mVisitedCities.size) {
            val distanceToStartTown = mDistanceMatrix[pCurrentCity][mStartAndEndTownIndex]
            if (distanceToStartTown > 0) return pCurrentTotalCost + distanceToStartTown
        }
        var localResult = Float.MAX_VALUE
        for (i in mVisitedCities.indices) {
            if (!mVisitedCities[i] && pCurrentCity != i) {
                mVisitedCities[i] = true
                mCurrentPath[pCityCounter] = i
                localResult =
                    TSP(i, pCityCounter + 1, pCurrentTotalCost + mDistanceMatrix[pCurrentCity][i])
                mVisitedCities[i] = false
                if (localResult < bestPathCosts) {
                    bestPathCosts = localResult
                    bestPath = mCurrentPath.copyOf(mCurrentPath.size)
                }
            }
        }
        return localResult
    }

    private fun printBestPath() {
        if(bestPathCosts > Int.MAX_VALUE) return
        print("Best path: ($bestPathCosts costs): ")
        for (i in bestPath) {
            print("$i -> ")
        }
        println(mStartAndEndTownIndex)
    }

    fun findPath(): ResultPath {
        val tsp = SimpleTSP(graph)

        for(i in 0 until graph.size){
            val result = tsp.runTSP(i)
            if(result.getDistance() >= Int.MAX_VALUE){
                continue
            }else{
                return result
            }
        }
        throw(Error("Not found"))
    }
}