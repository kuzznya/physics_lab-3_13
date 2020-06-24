package com.kuzznya.lab.model

import kotlin.math.pow

class TheoreticalMagneticField (
    private val a: Double,
    private val I: Double,
    private val R: Double
) {
    fun getB(z: Double) =
        MU0 * I * R.pow(2) / 2.0 *
                (1 / ((z + a / 2.0).pow(2) + R.pow(2)).pow(3.0 / 2.0) + 1 / ((z + a / 2.0 - a).pow(2) + R.pow(2)).pow(3.0 / 2.0))

    fun getBValues(points: Collection<Point>): Map<Point, Double> =
        points
            .map { Pair(it, getB(it.z)) }
            .toMap()
}