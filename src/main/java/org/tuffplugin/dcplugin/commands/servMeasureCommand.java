package org.tuffplugin.dcplugin.commands;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Math;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;


public class servMeasureCommand implements CommandExecutor{
    long lastUseTime = 0;
    long coolDown = 12000; // 12 seconds or 12000 milliseconds
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player)) {
            //commandSender.sendMessage(ChatColor.RED + "Only players can execute this command");
            // if sender isnt a player
            // here if sender is not a player
            commandSender.sendMessage(ChatColor.RED + "This command is only intended for players! Console running has no cooldown (intended) ");
            try {
                //OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
                long totalMemory = Runtime.getRuntime().maxMemory();
                long usedMemory =  Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                //long cpuUsage = Runtime.getRuntime()
                double convertedTotalMemory = (double) Math.round(((double) totalMemory/1000000000)*100) /100; // 2.dp. rounding
                double convertedUsedMemory = (double) Math.round(((double) usedMemory / 1000000000) * 100) /100;
                double ramUsagePercent = (double) Math.round(((double) usedMemory / totalMemory) * 100000)/1000; // 3d.p rounding albeit inefficinet
                Process process = Runtime.getRuntime().exec("sudo /usr/bin/vcgencmd measure_temp");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = reader.readLine();
                commandSender.sendMessage(ChatColor.GREEN + "RAM ALLOCATED: " + convertedUsedMemory+ "GB/" + convertedTotalMemory +"GB");
                commandSender.sendMessage(ChatColor.GREEN + "RAM USAGE: " + ramUsagePercent + "%");
                commandSender.sendMessage(ChatColor.GREEN + "CPU TEMP: " + line);
                //commandSender.sendMessage(ChatColor.GREEN + "CPU USAGE: " + cpuUsage);

                lastUseTime = System.currentTimeMillis();

            } catch (Exception e) {
                commandSender.sendMessage(ChatColor.RED + "Error getting system stats.");
            }
            return true;
        }

        // here if sender is a player

        final Player player = (Player) commandSender;
        //UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if ((currentTime - lastUseTime) < coolDown) {
            player.sendMessage(ChatColor.RED + "This command cannot be globally run for the next " + (coolDown-(currentTime - lastUseTime))/1000 + " seconds.");
            return true;
        }

        // player.setAllowFlight(true);
        // player.setFlying(true);

        //if (!player.hasPermission("tutorial.cpu")) {
        //    player.sendMessage(ChatColor.RED + "You don't have permission to run this command");
        //}

        player.sendMessage(ChatColor.GREEN + "Outputting CPU temperature and RAM usage... \n");
        try {
            //OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            long totalMemory = Runtime.getRuntime().maxMemory();
            long usedMemory =  Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            double convertedTotalMemory = (double) Math.round(((double) totalMemory/1000000000)*100) /100; // 2.dp. rounding
            double convertedUsedMemory = (double) Math.round(((double) usedMemory / 1000000000) * 100) /100;
            double ramUsagePercent = (double) Math.round(((double) usedMemory / totalMemory) * 100000)/1000; // 3d.p rounding albeit inefficinet
            Process process = Runtime.getRuntime().exec("sudo /usr/bin/vcgencmd measure_temp");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            player.sendMessage(ChatColor.GREEN + "RAM ALLOCATED: " + convertedUsedMemory+ "GB/" + convertedTotalMemory +"GB");
            player.sendMessage(ChatColor.GREEN + "RAM USAGE: " + ramUsagePercent + "%");
            player.sendMessage(ChatColor.GREEN + "CPU: " + line);

            lastUseTime = System.currentTimeMillis();

        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Error getting system stats.");
        }

        return true;
    }
}
