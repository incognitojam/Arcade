package dev.incognitojam.arcade

import dev.incognitojam.arcade.command.PingCommand
import dev.incognitojam.arcade.listener.JoinListener
import gg.flyte.twilight.twilight
import io.papermc.lib.PaperLib
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.bukkit.BukkitCommandHandler

class ArcadePlugin : JavaPlugin() {
    companion object {
        lateinit var instance: ArcadePlugin
    }

    override fun onEnable() {
        instance = this
        twilight(this) { }

        BukkitCommandHandler.create(this).apply {
            enableAdventure()
            register(PingCommand())
            registerBrigadier()
        }

        JoinListener()

        PaperLib.suggestPaper(this)
    }
}
