package com.kuzznya.lab.model

import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

class MagneticField (
    val data: Map<Point, Vector>
) {
    val values: Map<Point, Double>
        get() = data.keys
            .associateWith { sqrt(data[it]!!.x.pow(2) + data[it]!!.y.pow(2) + data[it]!!.z.pow(2)) }

    val gradBz: Map<Point, Double>
        get() = data
            .toSortedMap(Comparator { o1, o2 -> if (o1.z - o2.z < 0.0) -1 else 1 })
            .toList()
            .map { Pair(it.first, it.second.z) }
            .zipWithNext { a, b -> Pair(a.first, a.second / b.second) }
            .toMap()

    val gradBxy: Map<Point, Double>
        get() = data
            .toSortedMap(Comparator { o1, o2 -> if (o1.z - o2.z < 0.0) -1 else 1 })
            .toList()
            .map { Pair(it.first, sqrt(it.second.x.pow(2) + it.second.y.pow(2))) }
            .zipWithNext { a, b -> Pair(a.first, a.second / b.second) }
            .toMap()
}