package common.datastructures

class DisjointSets<T> {
    val rank: MutableMap<T, Int> = mutableMapOf()
    val parent: MutableMap<T, T> = mutableMapOf()

    private fun checkInit(x: T) {
        if (x in rank) return
        rank[x] = 0
        parent[x] = x
    }

    fun find(x: T): T {
        checkInit(x)
        if (parent[x]!! != x) {
            parent[x] = find(parent[x]!!)
        }
        return parent[x]!!
    }

    fun union(x: T, y: T) {
        val rootX = find(x)
        val rootY = find(y)
        if (rootX == rootY) {
            return
        }
        if (rank[rootX]!! < rank[rootY]!!) {
            parent[rootX] = rootY
        } else if (rank[rootY]!! < rank[rootX]!!) {
            parent[rootY] = rootX
        } else {
            parent[rootY] = rootX
            rank[rootX] = rank[rootX]!! + 1
        }
    }
}
