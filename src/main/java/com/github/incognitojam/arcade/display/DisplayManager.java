package com.github.incognitojam.arcade.display;

import com.github.incognitojam.arcade.Arcade;
import com.github.incognitojam.arcade.machine.Machine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DisplayManager {

    private final Arcade arcade;
    private Map<Short, Display> displayMap;

    public DisplayManager(Arcade arcade) {
        this.arcade = arcade;

        loadDisplays();
    }

    public void dispose() {
        saveDisplays();
    }

    private void loadDisplays() {
        this.displayMap = new HashMap<>();
    }

    private void verifyDisplays() {

    }

    private void saveDisplays() {

    }

    public boolean doesDisplayExist(short displayId) {
        return this.displayMap.containsKey(displayId);
    }

    public Display createDisplay(UUID ownerUniqueId, World world) {
        short displayId = getFirstAvailableId();

        MapView map = Bukkit.getMap(displayId);
        if (map == null) map = Bukkit.createMap(world);
        displayId = map.getId();

        Display display = new Display(displayId, ownerUniqueId, map);
        display.onCreate();

        displayMap.put(displayId, display);

        return display;
    }

    /**
     * Retrieve a display using its id
     *
     * @param displayId The id number of the display
     * @return Returns the requested display
     */
    public Display getDisplay(short displayId) {
        return this.displayMap.get(displayId);
    }

    public Display findDisplay(Block block) {
        for (Display display : displayMap.values()) {
            if (display.getAttachedBlock().equals(block)) return display;
        }
        return null;
    }

    /**
     * Retrieve all of the created displays
     *
     * @return Returns the list of displays
     */
    public ArrayList<Display> getDisplays() {
        return new ArrayList<>(this.displayMap.values());
    }

    /**
     * Retrieve all displays with a specified owner
     *
     * @param ownerUniqueId The unique id of the owner
     * @return Returns the list of displays which match this requirement
     */
    public ArrayList<Display> getDisplays(UUID ownerUniqueId) {
        ArrayList<Display> displays = getDisplays();
        displays.removeIf(display -> !display.getOwnerUniqueId().equals(ownerUniqueId));
        return displays;
    }

    public void removeDisplay(short displayId) {
        Display display = this.displayMap.remove(displayId);
        display.dispose();
    }

    private short getFirstAvailableId() {
        short id = 0;
        while (doesDisplayExist(id)) id++;
        return id;
    }

    public Machine registerMap(ItemFrame frame) {
        ItemStack item = frame.getItem();
        if (item == null) return null;
        if (!item.getType().equals(Material.MAP)) return null;

        short mapId = item.getDurability();
        if (!doesDisplayExist(mapId)) return null;

        Display display = getDisplay(mapId);
        display.setupFrame(frame);

        return arcade.getMachineManager().findMachine(display);
    }
}
