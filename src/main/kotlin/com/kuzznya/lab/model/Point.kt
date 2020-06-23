package com.kuzznya.lab.model

data class Point (
    var x: Double,
    var y: Double,
    var z: Double
) {
    fun move(v: Vector) {
        x += v.x
        y += v.y
        z += v.z
    }

    operator fun plus(v: Vector) =
        Point(x + v.x, y + v.y, z + v.z)

    operator fun minus(p: Point): Vector =
        Vector(x - p.x, y - p.y, z - p.z)
}