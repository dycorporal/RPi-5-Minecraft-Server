package org.tuffplugin.dcplugin.commands;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math;

import static java.lang.Integer.parseInt;
import static org.bukkit.Bukkit.getServer;

public class shutdownCommand implements CommandExecutor{
    private final JavaPlugin plugin;
    public shutdownCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        // if sender isnt a player
        // here if sender is not a player
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!player.hasPermission("tutorial.shutdown")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to use this command");
                return true;
            }
            player.sendMessage(ChatColor.RED + "This command is intended to run via terminal");
        }

        commandSender.sendMessage(ChatColor.RED + "[SENDER] INITIATED SERVER SHUTDOWN.");

        Integer requestedTime = Integer.parseInt(strings[0]);
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime timePlusRequest = currentTime.plusMinutes(requestedTime);
        LocalDate thedate = timePlusRequest.toLocalDate();
        String hours = String.format("%02d", timePlusRequest.getHour());
        String minutes = String.format("%02d", timePlusRequest.getMinute());
        String suffix = "";
        if (timePlusRequest.getHour() > 12) {
            suffix = "PM";
        }
        else {
            suffix = "AM";
        }
        if (requestedTime == -1) {
            try {
                Process process = Runtime.getRuntime().exec("sudo shutdown -c");
                Bukkit.getScheduler().cancelTasks(plugin);
                Bukkit.broadcastMessage(ChatColor.GREEN + "[GLOBAL] SERVER SHUTDOWN CANCELLED");
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if ( requestedTime < 15) {
            commandSender.sendMessage(ChatColor.RED + "You can't shutdown in less than 15 minutes. Only integer values.");
        }
        else {
            try {
                String shutdownTime = "" + (requestedTime + 5);
                Process process2 = Runtime.getRuntime().exec("sudo shutdown "+ strings[0]);
                Bukkit.broadcastMessage(ChatColor.RED + "[GLOBAL] SERVER SHUTDOWN SCHEDULED AT " + hours + ":" + minutes + " " + suffix + ", " + thedate);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Bukkit.broadcastMessage(ChatColor.RED + "Server stopping in 10 MINUTES.");
                }, (requestedTime * 60 * 20) - (600 * 20));
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Bukkit.broadcastMessage(ChatColor.RED + "Server stopping in 5 MINUTES.");
                }, (requestedTime * 60 * 20) - (300 * 20));
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Bukkit.broadcastMessage(ChatColor.RED + "Server is stopping...");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                }, (requestedTime * 60 * 20) - (30 * 20));
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
