package com.kuzznya.lab.model

import kotlin.math.*

data class Vector (
    var x: Double,
    var y: Double,
    var z: Double
) {
    val value: Double
        get() = sqrt(x.pow(2) + y.pow(2) + z.pow(2))

    operator fun plus(b: Vector): Vector =
        Vector(x + b.x, y + b.y, z + b.z)

    operator fun minus(b: Vector): Vector =
        this + b * -1.0

    operator fun times(k: Double): Vector =
        Vector(x * k, y * k, z * k)

    operator fun div(k: Double): Vector =
        Vector(x / k, y / k, z / k)
}