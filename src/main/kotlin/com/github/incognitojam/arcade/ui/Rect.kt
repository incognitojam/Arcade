package com.github.incognitojam.arcade.ui

import java.awt.Point

data class Rect(val x: Int, val y: Int, val w: Int, val h: Int) {

    fun contains(x1: Int, y1: Int): Boolean {
        return x1 >= x && x1 <= x + w && y1 >= y && y1 <= y + h
    }

    fun contains(point: Point): Boolean = contains(point.x, point.y)

    /**
     * This isn't tested
     */
    fun intersects(rect: Rect): Boolean {
        return x < rect.x + rect.w && x + w > rect.x && y > rect.y + rect.h && y + h < rect.y
    }

}