package com.github.incognitojam.arcade.machine

import com.github.incognitojam.arcade.HomeApp
import com.github.incognitojam.arcade.application.App
import com.github.incognitojam.arcade.display.CursorUtils
import com.github.incognitojam.arcade.display.Display
import com.github.incognitojam.arcade.ui.Rect
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.awt.Color
import java.util.*

class Context(val machine: Machine) {
    var app: App = HomeApp(machine)
        set(app) {
            field = app
            this.app.display = this.display!!
        }

    val display: Display? = null
        set(newDisplay) {
            if (display == newDisplay) return

            if (display is Display) {
                val buffer = display?.buffer

                (newDisplay as Display).drawImage(0, 0, buffer)
                newDisplay.color = display?.color

                display.clear(Color.BLACK)
                (display as Display).color = Color.WHITE
                (display as Display).drawText(15, 15, "No signal")

                field = newDisplay
                this.app = HomeApp(machine)
                this.app.display = newDisplay
            } else {
                field = display
                display!!.clear(Color.BLACK)
                this.app = HomeApp(machine)
                this.app.display = display!!
            }
        }

    private val userMap: HashMap<Int, User> = HashMap()

    val isDisplayConnected: Boolean
        get() = this.display != null

    val users: Set<User>
        get() = HashSet(userMap.values)

    fun doesUserExist(userId: Int): Boolean {
        return userMap[userId] != null
    }

    fun doesUserExist(userUniqueId: UUID): Boolean {
        return getUser(userUniqueId) != null
    }

    fun getUser(userId: Int): User? {
        return userMap[userId]
    }

    fun getUser(userUniqueId: UUID): User? {
        return userMap.values.firstOrNull { it.userUniqueId == userUniqueId }
    }

    fun isHighlighted(x: Int, y: Int, width: Int, height: Int): Boolean {
        return isHighlighted(Rect(x, y, width, height))
    }

    fun isHighlighted(rect: Rect): Boolean {
        return userMap.values.stream().anyMatch { user -> rect.contains(user.mousePosition) }
    }

    fun isClicked(rect: Rect): Boolean {
        return userMap.values.stream().anyMatch { user -> user.clicked && rect.contains(user.mousePosition) }
    }

    fun isClicked(x: Int, y: Int, width: Int, height: Int): Boolean {
        return isClicked(Rect(x, y, width, height))
    }

    private val firstAvailableId: Int
        get() {
            var id = 0
            while (doesUserExist(id)) id++
            return id
        }

    fun addUser(userUniqueId: UUID): User {
        if (doesUserExist(userUniqueId)) return getUser(userUniqueId)!!
        val id = firstAvailableId
        val user = User(id, userUniqueId)
        userMap.put(id, user)
        return user
    }

    fun removeUser(userUniqueId: UUID): Boolean {
        return userMap.values.removeIf({ it.userUniqueId == userUniqueId })
    }

    fun onClick(player: Player): Boolean {
        val user = getUser(player.uniqueId) ?: return false
        user.clickTime = System.currentTimeMillis()
        return true
    }

    fun onUpdate() {
        for ((key, user) in this.userMap) {
            val userUniqueId = user.userUniqueId
            val player = Bukkit.getPlayer(userUniqueId)

            if (player == null || !player.isOnline) {
                this.userMap.remove(key)
            } else {
                val cursor = CursorUtils.getCursorPoint(player, this.display!!)
                if (cursor.x < 0)
                    cursor.x = 0
                else if (cursor.x > 127) cursor.x = 127
                if (cursor.y < 0)
                    cursor.y = 0
                else if (cursor.y > 127) cursor.y = 127

                user.mousePositionLast = user.mousePosition
                user.mousePosition = cursor

                user.hotbarSlot = player.inventory.heldItemSlot

                user.clickedLast = user.clicked
                user.clicked = user.clickTime >= System.currentTimeMillis() - 210
            }
        }
        app.onDraw()
    }
}
