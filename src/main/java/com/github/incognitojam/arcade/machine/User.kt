package com.github.incognitojam.arcade.machine

import java.awt.Point
import java.util.*

class User(val id: Int, val userUniqueId: UUID) {

    var mousePosition: Point = Point(0, 0)
    var mousePositionLast: Point = Point(0, 0)
    var hotbarSlot: Int = 0

    var clicked: Boolean = false
    var clickedLast: Boolean = false
    var clickTime: Long = 0L

}
