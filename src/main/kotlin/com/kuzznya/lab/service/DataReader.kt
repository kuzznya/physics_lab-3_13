package com.kuzznya.lab.service

import com.kuzznya.lab.model.Point
import com.kuzznya.lab.model.Vector
import java.io.File
import java.util.*

class DataReader(private val path: String) {
    fun loadData(): Map<Point, Vector> {
        val data: MutableMap<Point, Vector> = emptyMap<Point, Vector>().toMutableMap()
        File(path).forEachLine {
            if (it.startsWith('%'))
                return@forEachLine

            val scanner = Scanner(it).useLocale(Locale.US)

            val x: Double
            val y: Double
            val z: Double
            val bx: Double
            val by: Double
            val bz: Double

            if (scanner.hasNextDouble())
                x = scanner.nextDouble()
            else
                return@forEachLine

            if (scanner.hasNextDouble())
                y = scanner.nextDouble()
            else
                return@forEachLine

            if (scanner.hasNextDouble())
                z = scanner.nextDouble()
            else
                return@forEachLine

            if (scanner.hasNextDouble())
                bx = scanner.nextDouble()
            else
                return@forEachLine

            if (scanner.hasNextDouble())
                by = scanner.nextDouble()
            else
                return@forEachLine

            if (scanner.hasNextDouble())
                bz = scanner.nextDouble()
            else
                return@forEachLine

            data[Point(x, y, z)] = Vector(bx, by, bz)
        }
        return data
    }
}