package com.example.findpath

class TSP(
    graph: MutableList<MutableList<Int>>
) {
    private var size = graph.size-1

    private var MAX = 1000000

    private var distance = graph

    private var tempPath: MutableList<Int> = MutableList(1){1}
    private var resultPath: MutableList<Int> = mutableListOf()

    private var memo = Array(size + 1) { IntArray(1 shl size + 1) }
    private var memoPath = Array(size + 1) { Array(1 shl size + 1) {tempPath} }

    private fun tempPathCheck(){
        if(tempPath.size == size) {
            tempPath.clear()
            tempPath.add(1)
        }
    }

    private fun findPath(i: Int, mask: Int): Int {
        if (mask == 1 shl i or 3) return distance[1][i]
        if (memo[i][mask] != 0) {
            resultPath = memoPath[i][mask]

            return memo[i][mask]
        }
        var result = MAX
        val tempResult = result
        for (j in 1..size){
            if (mask and (1 shl j) != 0 && j != i && j != 1) {
                result =
                    result.coerceAtMost(
                        findPath(
                            j,
                            mask and (1 shl i).inv()
                        ) + distance[j][i]
                    )
            }
        }
        if(result != tempResult){
            tempPathCheck()
            tempPath.add(i)
        }
        return result.also {
            memo[i][mask] = it
            memoPath[i][mask] = tempPath
        }
    }

    fun shortestDistance(): ResultPath {
        var answer = MAX
        for (i in 1 until size)
            answer = answer.coerceAtMost(findPath(i, (1 shl size + 1) - 1) + distance[i][1])
        println("resultPath $resultPath")
        println("The cost of most efficient tour = $answer")
        return ResultPath(answer, resultPath)
    }
}