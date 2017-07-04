package com.github.incognitojam.arcade

import com.github.incognitojam.arcade.application.App
import com.github.incognitojam.arcade.application.AppDescription
import com.github.incognitojam.arcade.machine.Machine
import com.github.incognitojam.arcade.ui.Button
import com.github.incognitojam.arcade.ui.Rect
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class HomeApp(machine: Machine) : App(AppDescription("Home", "1.0.0", "IncognitoJam"), machine) {

    val cursor: BufferedImage = ImageIO.read(getResource("wiimote.png"))
    val drawing: BufferedImage = BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB)
    val graphics: Graphics = drawing.graphics

    val button: Button = Button("Button", Rect(10, 10, 40, 10))

    override fun onCreate() {
        graphics.color = Color.RED
        button.debug = true
    }

    override fun onDraw() {
        display.clear(Color.WHITE)
        display.drawImage(0, 0, drawing)

        button.onDraw(context)

        for (user in context.users) {
            display.drawImage(user.mousePosition, cursor)
            if (user.clicked) {
                if (user.clickedLast) {
                    drawLine(user.mousePosition, user.mousePositionLast)
//                    println("Drawing line from ${user.mousePosition} to ${user.mousePositionLast}")
                } else {
                    drawPixel(user.mousePosition)
                }
            }
        }
    }

    override fun onDispose() {
        graphics.dispose()
    }

    internal fun drawPixel(point: Point) = graphics.drawRect(point.x, point.y, 0, 0)

    internal fun drawLine(pointA: Point, pointB: Point) = graphics.drawLine(pointA.x, pointA.y, pointB.x, pointB.y)

}