package org.tuffplugin.dcplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class mobLoreCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Only players can execute this command");
            // if sender isnt a player
            // here if sender is not a player
            return true;
        }
        // here if sender is a player
        final Player player = (Player) commandSender;
        Integer numOfElements = strings.length;

        if (numOfElements < 1) { // arguments
            player.sendMessage(ChatColor.GREEN + "Usage: /moblore <mob name>");
            return true;
        }

        StringBuilder mobNameBuilder = new StringBuilder();
        for (String arg : strings) {
            mobNameBuilder.append(arg).append(" ");
        }
        String mobName = mobNameBuilder.toString().trim();

        if (mobName.equalsIgnoreCase("Enhanced Zombie")) {
            // Do something with the "Enhanced Zombie" mob
            player.sendMessage(ChatColor.YELLOW + "Enhanced Zombie - Zombie Variant");
            player.sendMessage(ChatColor.GREEN+ "Info: The upgraded model of the zombie. Chainmail armor and an Axe.");
            player.sendMessage(ChatColor.DARK_GREEN + "Notes: Disables shields, hits hard but is taller than 2 blocks.");
            return true;
        }
        else if (mobName.equalsIgnoreCase("Rusher Zombie")) {
            player.sendMessage(ChatColor.YELLOW + "Rusher Zombie - Zombie Variant");
            player.sendMessage(ChatColor.GREEN+ "Info: The fastest zombie alive. Leather armor and a Pumpkin Head.");
            player.sendMessage(ChatColor.DARK_GREEN + "Notes: Not as fast in water. Very weak and can climb 1 block high ledges.");
            return true;
        }
        else if (mobName.equalsIgnoreCase("Coal Head")) {
            player.sendMessage(ChatColor.YELLOW + "Coal Head - Zombie Variant");
            player.sendMessage(ChatColor.GREEN+ "Info: The tank of the undead. A gilded chestplate and a Burning Iron Pickaxe. Coal for a head.");
            player.sendMessage(ChatColor.DARK_GREEN + "Notes: Not fast. Run and fight it when it is alone, and bring water.");

            return true;
        }
        else if (mobName.equalsIgnoreCase("Trickster Skeleton")) {
            player.sendMessage(ChatColor.YELLOW + "Trickster - Skeleton Triad Variant");
            player.sendMessage(ChatColor.GREEN+ "Info: Leather armor, Iron Helmet. Shoots darkness arrows, blinding players on hit with huge knockback.");
            player.sendMessage(ChatColor.DARK_GREEN + "Notes: Be aware of your environment. Bring a shield.");
            return true;
        }
        else if (mobName.equalsIgnoreCase("Sapper Skeleton")) {
            player.sendMessage(ChatColor.YELLOW + "Sapper - Skeleton Triad Variant");
            player.sendMessage(ChatColor.GREEN+ "Info: Leather armor, Gold Helmet. Shoots weakness and mining fatigue arrows.");
            player.sendMessage(ChatColor.DARK_GREEN + "Notes: Don't be caught in a crowd. Your swords and axes will fail you. Bring a shield.");
            return true;
        }
        else if (mobName.equalsIgnoreCase("Curser Skeleton")) {
            player.sendMessage(ChatColor.YELLOW + "Curser - Skeleton Triad Variant");
            player.sendMessage(ChatColor.GREEN+ "Info: Leather armor, Diamond Helmet. Shoots infestation and hunger arrows.");
            player.sendMessage(ChatColor.DARK_GREEN + "Notes: Ignore the silverfish. Kill this skeleton first.");
            return true;
        }
        else if (mobName.equalsIgnoreCase("Firecracker")) {
            player.sendMessage(ChatColor.YELLOW + "Firecracker - Pillager Variant");
            player.sendMessage(ChatColor.GREEN+ "Info: Not implemented yet");
            player.sendMessage(ChatColor.DARK_GREEN + "Notes: You shouldn't know this yet.");
            return true;
        }
        else if (mobName.equalsIgnoreCase("Day Phantom")) {
            player.sendMessage(ChatColor.YELLOW + "Day Phantom - Phantom Variant");
            player.sendMessage(ChatColor.GREEN+ "Info: The strongest of the phantoms. Strong enough to bear the sun.");
            player.sendMessage(ChatColor.DARK_GREEN + "Notes: Run like you would from any other phantom. It's big. Use it to your advantage.");
            return true;
        }
        else {
            player.sendMessage(ChatColor.RED + "Sorry, I don't have any information about a " + mobName + ".");
            return true;
        }
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> validArguments = new ArrayList<>();

        if (args.length ==1) {
            StringUtil.copyPartialMatches(args[0],List.of("Enhanced Zombie", "Rusher Zombie", "Coal Head", "Trickster Skeleton","Sapper Skeleton", "Curser Skeleton", "Day Phantom"), validArguments);
            return validArguments;
        }
        return List.of();
    }
    
}

