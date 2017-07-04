package com.github.incognitojam.arcade.ui

import com.github.incognitojam.arcade.display.Display
import com.github.incognitojam.arcade.machine.Context
import com.github.incognitojam.arcade.machine.Machine
import java.awt.Color

class Button(val text: String, x: Int, y: Int, width: Int, height: Int) : Drawable(x, y, width, height) {

    constructor(text: String, rect: Rect) : this(text, rect.x, rect.y, rect.w, rect.y)

    var fillColorDefault: Color = Color.DARK_GRAY
    var fillColorHighlighted: Color = Color.GRAY
    var fillColorClicked: Color = Color.YELLOW

    var textColorDefault: Color = Color.BLACK
    var textColorHighlighted: Color = Color.BLUE
    var textColorClicked: Color = Color.RED

    override fun onDraw(machine: Machine) {
        // Calculate color for button fill
        val fillColor: Color
        if (display.isClicked(rect)) {
            fillColor = fillColorClicked
        } else if (display.isHighlighted(rect)) {
            fillColor = fillColorHighlighted
        } else {
            fillColor = fillColorDefault
        }

        // Draw the background of the button
        display.display.setColor(fillColor) {
            display.display.drawFilledRect(x, y, width, height)
        }


        // Calculate color for button text
        val textColor: Color
        if (machine.isClicked(rect)) {
            textColor = textColorClicked
        } else if (machine.isHighlighted(rect)) {
            textColor = textColorHighlighted
        } else {
            textColor = textColorDefault
        }

        // Draw the button text
        machine.display.setColor(textColor) {
            machine.display.drawText(x, y + height, text)
        }

        super.onDraw(context)
    }

}