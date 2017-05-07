package com.github.incognitojam.arcade.command;

import com.github.incognitojam.arcade.Arcade;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ArcadeCommands implements CommandExecutor {

    private final Arcade arcade;

    public ArcadeCommands(Arcade arcade) {
        this.arcade = arcade;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args[0].toLowerCase()) {
            // arcade machine
            case "machine": {
                if (args.length == 0) {

                } else {
                    switch (args[1]) {
                        // arcade machine list
                        case "list": {

                        }
                        break;

                        // arcade machine create [id]
                        case "create": {

                        }
                        break;

                        // arcade machine remove [id]
                        case "remove": {

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
                if (args.length == 0) {

                } else {
                    switch (args[1]) {

                    }
                }
            }
            break;
        }
        return true;
    }

}
