package com.github.incognitojam.arcade;

import com.github.incognitojam.arcade.command.ArcadeCommands;
import com.github.incognitojam.arcade.display.Display;
import com.github.incognitojam.arcade.display.DisplayManager;
import com.github.incognitojam.arcade.machine.Context;
import com.github.incognitojam.arcade.machine.Machine;
import com.github.incognitojam.arcade.machine.MachineManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Arcade extends JavaPlugin implements Listener {

    public static File DATA_FOLDER = null;

    private DisplayManager displayManager;
    private MachineManager machineManager;

    @Override
    public void onEnable() {
        this.displayManager = new DisplayManager(this);
        this.machineManager = new MachineManager(this);

        getCommand("arcade").setExecutor(new ArcadeCommands(this));
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> machineManager.onUpdate(), 20L, 2L);

        DATA_FOLDER = getDataFolder();
    }

    @Override
    public void onDisable() {

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame) {
            ItemFrame frame = (ItemFrame) event.getRightClicked();
            ItemStack item = frame.getItem();
            if (item.getType() == Material.MAP && displayManager.doesDisplayExist(item.getDurability())) {
                event.setCancelled(true);
                Display display = displayManager.getDisplay(item.getDurability());
//                final Rotation rotation = frame.getRotation();
                Machine machine = machineManager.findMachine(display);
                machine.getContext().onClick(event.getPlayer());
//                frame.setRotation(rotation);
            } else {
                Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> displayManager.registerMap(frame));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block.getType() != Material.DIAMOND_BLOCK) return;

            Display display = displayManager.findDisplay(block);
            if (display == null) return;

            Machine machine = machineManager.findMachine(display);
            if (machine == null) return;

            Context context = machine.getContext();
            context.onClick(event.getPlayer());
        }
    }

    public DisplayManager getDisplayManager() {
        return displayManager;
    }

    public MachineManager getMachineManager() {
        return machineManager;
    }
}
