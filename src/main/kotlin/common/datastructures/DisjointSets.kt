package common.datastructures

class DisjointSets<T> {
    val rank: MutableMap<T, Int> = mutableMapOf()
    val parent: MutableMap<T, T> = mutableMapOf()

    val size get() = rank.size
    val sets: MutableMap<T, MutableSet<T>> = mutableMapOf()

    private fun checkInit(x: T) {
        if (x in rank) return
        rank[x] = 0
        parent[x] = x
        sets[x] = mutableSetOf(x)
    }

    fun find(x: T): T {
        checkInit(x)
        if (parent[x]!! != x) {
            parent[x] = find(parent[x]!!)
        }
        return parent[x]!!
    }

    fun elementsInSet(root: T): Set<T> {
        return sets.getValue(root)
    }

    fun union(x: T, y: T) {
        val rootX = find(x)
        val rootY = find(y)
        if (rootX == rootY) {
            return
        }
        if (rank[rootX]!! < rank[rootY]!!) {
            parent[rootX] = rootY
            sets.getValue(rootY).addAll(sets.getValue(rootX))
            sets -= rootX
        } else if (rank[rootY]!! < rank[rootX]!!) {
            parent[rootY] = rootX
            sets.getValue(rootX).addAll(sets.getValue(rootY))
            sets -= rootY
        } else {
            parent[rootY] = rootX
            rank[rootX] = rank[rootX]!! + 1
            sets.getValue(rootX).addAll(sets.getValue(rootY))
            sets -= rootY
        }
    }
}
