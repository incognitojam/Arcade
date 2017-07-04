package com.github.incognitojam.arcade.display

import java.awt.Point
import java.util.*

/**
 * Work-in-progress class to manage multiple displays connected to a single machine
 */
class DisplayInterface {

    val displayList: ArrayList<DisplayHolder>

    init {
        this.displayList = ArrayList<DisplayHolder>()
    }

    fun addDisplay(display: Display): DisplayHolder? {
        //        DisplayHolder displayHolder = new DisplayHolder();
        return null
    }

    inner class DisplayHolder(var displayId: Short, var position: Point, var display: Display)

}
