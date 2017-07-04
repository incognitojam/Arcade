package com.github.incognitojam.arcade.display

import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.awt.Point

/**
 * Implemented using the whitepaper found here https://docs.google.com/file/d/0B9Sf4F-ig8lPUlhOdHZ2ZGtzdnM
 * written by https://github.com/ase34
 */
class CursorUtils {

    companion object {
        fun determinant(matrix: Array<DoubleArray>): Double {
            return matrix[0][0] * matrix[1][1] * matrix[2][2] + matrix[0][1] * matrix[1][2] * matrix[2][0] + matrix[0][2] *
                    matrix[1][0] * matrix[2][1] - matrix[0][2] * matrix[1][1] * matrix[2][0] - matrix[0][1] * matrix[1][0] *
                    matrix[2][2] - matrix[0][0] * matrix[1][2] * matrix[2][1]
        }

        fun getIntersection(linePoint: Vector, lineDirection: Vector, planeOrigin: Vector,
                            planeXDirection: Vector, planeYDirection: Vector): Vector {
            val coefficients = arrayOf(doubleArrayOf(lineDirection.x, -planeXDirection.x, -planeYDirection.x), doubleArrayOf(lineDirection.y, -planeXDirection.y, -planeYDirection.y), doubleArrayOf(lineDirection.z, -planeXDirection.z, -planeYDirection.z))

            val solutions = doubleArrayOf(planeOrigin.x - linePoint.x, planeOrigin.y - linePoint.y, planeOrigin.z - linePoint.z)

            val sMatrix = arrayOf(doubleArrayOf(coefficients[0][0], solutions[0], coefficients[0][2]), doubleArrayOf(coefficients[1][0], solutions[1], coefficients[1][2]), doubleArrayOf(coefficients[2][0], solutions[2], coefficients[2][2]))
            val tMatrix = arrayOf(doubleArrayOf(coefficients[0][0], coefficients[0][1], solutions[0]), doubleArrayOf(coefficients[1][0], coefficients[1][1], solutions[1]), doubleArrayOf(coefficients[2][0], coefficients[2][1], solutions[2]))

            val det = determinant(coefficients)

            val s = determinant(sMatrix) / det
            val t = determinant(tMatrix) / det

            return Vector(s, t, 0.0)
        }

        fun getCursorPoint(player: Player, display: Display): Point {
            val linePoint = player.eyeLocation.toVector()
            val lineDirection = player.eyeLocation.direction

            val intersection = getIntersection(linePoint, lineDirection, display.planeOrigin, display.planeXAxis, display.planeYAxis)

            return Point((128 * intersection.x).toInt(), (128 * intersection.y).toInt())
        }
    }

}
