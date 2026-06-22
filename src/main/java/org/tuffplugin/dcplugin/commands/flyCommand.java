package org.tuffplugin.dcplugin.commands;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class flyCommand implements CommandExecutor{
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

        if (!player.hasPermission("tutorial.fly")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command");
            return true;
        }
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.setFlying(false);

            player.sendMessage(ChatColor.GREEN + "You can no longer fly");

            return true;
        }
        player.setAllowFlight(true);
        player.setFlying(true);

        player.sendMessage(ChatColor.GREEN + "You can now fly");

        return true;
    }
}
