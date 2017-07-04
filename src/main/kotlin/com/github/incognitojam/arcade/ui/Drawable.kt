package com.github.incognitojam.arcade.ui

import com.github.incognitojam.arcade.machine.Context
import java.awt.Color

open class Drawable(val x: Int, val y: Int, val width: Int, val height: Int) {

    constructor(rect: Rect) : this(rect.x, rect.y, rect.w, rect.y)

    var debug: Boolean = false
    val rect: Rect = Rect(x, y, width, height)

    open fun onCreate() {

    }

    open fun onDraw(context: Context) {
        if (debug) context.display.setColor(Color.RED) { context.display.drawRect(x, y, width, height) }
    }

    open fun onDispose() {

    }

}