package com.github.incognitojam.arcade;

import com.github.incognitojam.arcade.command.ArcadeCommands;
import com.github.incognitojam.arcade.machine.MachineManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Arcade extends JavaPlugin {

    private ArcadeCommands arcadeCommands;
    private MachineManager machineManager;

    @Override
    public void onEnable() {
        this.arcadeCommands = new ArcadeCommands(this);
        this.machineManager = new MachineManager(this);
    }

    @Override
    public void onDisable() {

    }

    public MachineManager getMachineManager() {
        return machineManager;
    }
}
