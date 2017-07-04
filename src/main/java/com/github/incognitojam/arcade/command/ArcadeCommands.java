package com.github.incognitojam.arcade.command;

import com.github.incognitojam.arcade.Arcade;
import com.github.incognitojam.arcade.display.Display;
import com.github.incognitojam.arcade.display.DisplayManager;
import com.github.incognitojam.arcade.machine.Machine;
import com.github.incognitojam.arcade.machine.Context;
import com.github.incognitojam.arcade.machine.MachineManager;
import com.github.incognitojam.arcade.machine.User;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

import java.util.ArrayList;
import java.util.UUID;

public class ArcadeCommands implements CommandExecutor {

    private final Arcade arcade;

    public ArcadeCommands(Arcade arcade) {
        this.arcade = arcade;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            // TODO: Display help menu

            return false;
        } else {
            switch (args[0].toLowerCase()) {
                // arcade machine
                case "machine": {
                    if (args.length == 1) {
                        // TODO: Command not found

                        return false;
                    } else {
                        MachineManager machineManager = arcade.getMachineManager();
                        switch (args[1]) {
                            // arcade machine list
                            case "list": {
                                ArrayList<Machine> machines = machineManager.getMachines();

                                sender.sendMessage(ChatColor.RED + "Machine list:");
                                for (Machine machine : machines) {
                                    UUID ownerUniqueId = machine.getOwnerUniqueId();
                                    OfflinePlayer player = Bukkit.getOfflinePlayer(ownerUniqueId);
                                    sender.sendMessage("[" + machine.getMachineId() + "] owned by " + player.getName());
                                }
                            }
                            break;

                            // arcade machine info [machine id]
                            case "info": {
                                if (args.length == 3) {

                                    Short machineId;
                                    try {
                                        machineId = Short.parseShort(args[2]);
                                    } catch (Exception ignored) {
                                        sender.sendMessage("Expected [machine id].");
                                        return false;
                                    }

                                    if (!machineManager.doesMachineExist(machineId)) {
                                        sender.sendMessage("The machine [" + machineId + "] does not exist.");
                                        return false;
                                    }

                                    Machine machine = machineManager.getMachine(machineId);
                                    Context context = machine.getContext();
                                    OfflinePlayer player = Bukkit.getOfflinePlayer(machine.getOwnerUniqueId());
                                    sender.sendMessage("[" + machineId + "] is owned by " + player.getName() + ". It is currently running " + context
                                            .getApp().getDescription().getName() + " and " + (context.isDisplayConnected() ? "does" : "does not") +
                                            " have a display connected.");

                                } else {
                                    sender.sendMessage("Incorrect number of arguments.");
                                    return false;
                                }
                            }
                            break;

                            // arcade machine create
                            case "create": {
                                if (sender instanceof Player) {
                                    Player player = (Player) sender;
                                    Machine machine = machineManager.createMachine(player.getUniqueId());
                                    short machineId = machine.getMachineId();

                                    sender.sendMessage("You created a new machine with id [" + machineId + "].");
                                } else {
                                    sender.sendMessage("You must be a player to create machines.");
                                }
                            }
                            break;

                            // arcade machine remove [id]
                            case "remove": {
                                if (args.length == 3) {
                                    short machineId;
                                    try {
                                        machineId = Short.parseShort(args[2]);
                                    } catch (Exception ignored) {
                                        sender.sendMessage("You must specify the id of the machine to remove.");
                                        return false;
                                    }

                                    Machine machine = machineManager.getMachine(machineId);
                                    boolean auth = !(sender instanceof Player) || ((Player) sender).getUniqueId().equals(machine.getOwnerUniqueId())
                                            || sender.isOp();

                                    if (auth) {
                                        machineManager.removeMachine(machineId);
                                    } else {
                                        sender.sendMessage("You do not have permission to remove that machine");
                                    }

                                } else {
                                    sender.sendMessage("You must specify the id of the machine to remove.");
                                }
                            }
                            break;

                            // arcade machine join [id]
                            case "join": {
                                if (args.length == 3) {
                                    short machineId;
                                    try {
                                        machineId = Short.parseShort(args[2]);
                                    } catch (Exception ignored) {
                                        sender.sendMessage("You must specify the id of the machine to join.");
                                        return false;
                                    }

                                    if (!machineManager.doesMachineExist(machineId)) {
                                        sender.sendMessage("The machine [" + machineId + "] does not exist.");
                                        return false;
                                    }

                                    if (!(sender instanceof Player)) {
                                        sender.sendMessage("You must be a player to join a machine!");
                                        return false;
                                    }

                                    Player player = (Player) sender;
                                    Machine machine = machineManager.getMachine(machineId);
                                    Context context = machine.getContext();

                                    User user = context.addUser(player.getUniqueId());
                                    player.sendMessage("You have joined machine [" + machineId + "] as user [" + user.getId() + "]");

                                } else {
                                    sender.sendMessage("You must specify the id of the machine to join.");
                                    return false;
                                }
                            }
                            break;

                            // arcade machine leave [id]
                            case "leave": {
                                if (args.length == 3) {
                                    short machineId;
                                    try {
                                        machineId = Short.parseShort(args[2]);
                                    } catch (Exception ignored) {
                                        sender.sendMessage("You must specify the id of the machine to leave.");
                                        return false;
                                    }

                                    if (!machineManager.doesMachineExist(machineId)) {
                                        sender.sendMessage("The machine [" + machineId + "] does not exist.");
                                        return false;
                                    }

                                    if (!(sender instanceof Player)) {
                                        sender.sendMessage("You must be a player to leave a machine!");
                                        return false;
                                    }

                                    Player player = (Player) sender;
                                    Machine machine = machineManager.getMachine(machineId);
                                    Context context = machine.getContext();

                                    context.removeUser(player.getUniqueId());
                                    player.sendMessage("You have left machine [" + machineId + "]");

                                } else {
                                    sender.sendMessage("You must specify the id of the machine to leave.");
                                    return false;
                                }
                            }
                            break;

                            default: {
                                // TODO: Command not found

                                return false;
                            }
                        }
                    }
                }
                break;

                // arcade display
                case "display": {
                    if (args.length == 1) {
                        // TODO: Command not found

                        return false;
                    } else {
                        DisplayManager displayManager = arcade.getDisplayManager();
                        switch (args[1]) {
                            // arcade display list
                            case "list": {
                                ArrayList<Display> displays = displayManager.getDisplays();

                                sender.sendMessage(ChatColor.RED + "Display list:");
                                for (Display display : displays) {
                                    UUID ownerUniqueId = display.getOwnerUniqueId();
                                    OfflinePlayer player = Bukkit.getOfflinePlayer(ownerUniqueId);
                                    sender.sendMessage("[" + display.getDisplayId() + "] owned by " + player.getName());
                                }
                            }
                            break;

                            // arcade display create
                            case "create": {
                                if (sender instanceof Player) {
                                    Player player = (Player) sender;
                                    Display display = displayManager.createDisplay(player.getUniqueId(), player.getWorld());
                                    sender.sendMessage("You created a new display with id [" + display.getDisplayId() + "].");
                                    sender.sendMessage("You can link this display to a machine using the /arcade display link [display id] [machine" +
                                            " id] command");

                                    short mapId = display.getMapView().getId();
                                    ItemStack mapItem = new ItemStack(Material.MAP, 1, mapId);
                                    int slot = player.getInventory().firstEmpty();
                                    if (slot > -1) {
                                        player.getInventory().setItem(slot, mapItem);
                                    } else {
                                        player.getWorld().dropItem(player.getLocation(), mapItem);
                                    }

                                    sender.sendMessage("You have been given the map item corresponding to this display.");
                                } else {
                                    sender.sendMessage("You must be a player to create machines.");
                                }
                            }
                            break;

                            // arcade display spawn [display id]
                            case "spawn": {
                                if (sender instanceof Player) {
                                    Player player = (Player) sender;

                                    if (args.length == 3) {

                                        Short displayId;
                                        try {
                                            displayId = Short.parseShort(args[2]);
                                        } catch (Exception ignored) {
                                            sender.sendMessage("Expected [display id].");
                                            return false;
                                        }

                                        if (!displayManager.doesDisplayExist(displayId)) {
                                            sender.sendMessage("Unknown display [" + displayId + "]");
                                            return false;
                                        }

                                        Display display = displayManager.getDisplay(displayId);
                                        MapView mapView = display.getMapView();
                                        short mapId = mapView.getId();

                                        ItemStack mapItem = new ItemStack(Material.MAP, mapId);
                                        int slot = player.getInventory().firstEmpty();
                                        if (slot > -1) {
                                            player.getInventory().setItem(slot, mapItem);
                                        } else {
                                            player.getWorld().dropItem(player.getLocation(), mapItem);
                                        }
                                        sender.sendMessage("You have been given the map item corresponding to this display.");

                                    } else {
                                        sender.sendMessage("Incorrect number of arguments.");
                                        return false;
                                    }
                                } else {
                                    sender.sendMessage("You must be a player to use this command.");
                                    return false;
                                }
                            }
                            break;

                            // arcade display remove [id]
                            case "remove": {
                                if (args.length == 3) {

                                    Short displayId;
                                    try {
                                        displayId = Short.parseShort(args[2]);
                                    } catch (Exception ignored) {
                                        sender.sendMessage("Expected [display id].");
                                        return false;
                                    }

                                    if (!displayManager.doesDisplayExist(displayId)) {
                                        sender.sendMessage("Display [" + displayId + "] does not exist.");
                                        return false;
                                    }

                                    displayManager.removeDisplay(displayId);
                                    sender.sendMessage("Removed the display [" + displayId + "] successfully.");

                                } else {
                                    sender.sendMessage("Incorrect number of arguments.");
                                    return false;
                                }
                            }
                            break;

                            // arcade display link [display id] [machine id]
                            case "link": {
                                if (args.length == 4) {
                                    Short displayId;
                                    try {
                                        displayId = Short.parseShort(args[2]);
                                    } catch (Exception ignored) {
                                        sender.sendMessage("Expected [display id]");
                                        return false;
                                    }

                                    if (!displayManager.doesDisplayExist(displayId)) {
                                        sender.sendMessage("Display [" + displayId + "] does not exist!");
                                        return false;
                                    }

                                    Short machineId;
                                    try {
                                        machineId = Short.parseShort(args[3]);
                                    } catch (Exception ignored) {
                                        sender.sendMessage("Expected [machine id]");
                                        return false;
                                    }

                                    MachineManager machineManager = arcade.getMachineManager();
                                    if (!machineManager.doesMachineExist(machineId)) {
                                        sender.sendMessage("Machine [" + machineId + "] does not exist!");
                                        return false;
                                    }

                                    Display display = displayManager.getDisplay(displayId);
                                    Machine machine = machineManager.getMachine(machineId);
                                    Context context = machine.getContext();

                                    if (context.isDisplayConnected()) {
                                        sender.sendMessage("Display [" + context.getDisplay().getDisplayId() + "] will be disconnected.");
                                    }

                                    context.setDisplay(display);
                                    sender.sendMessage("Display [" + displayId + "] has been connected to machine [" + machineId + "].");
                                } else {
                                    sender.sendMessage("Incorrect number of arguments.");
                                    return false;
                                }
                            }
                            break;

                            default: {
                                // TODO: Command not found

                                return false;
                            }
                        }
                    }
                }
                break;
            }
        }
        return true;
    }

}
