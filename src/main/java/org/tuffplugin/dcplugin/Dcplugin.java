package org.tuffplugin.dcplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.util.StringUtil;
import org.tuffplugin.dcplugin.commands.flyCommand;
import org.tuffplugin.dcplugin.commands.mobLoreCommand;
import org.tuffplugin.dcplugin.commands.servMeasureCommand;
import org.tuffplugin.dcplugin.commands.shutdownCommand;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public final class Dcplugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("dc plugin turnin on ");
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new myListener(), this);
        getCommand("measureServer").setExecutor(new servMeasureCommand());
        getCommand("fly").setExecutor(new flyCommand());
        getCommand("mobLore").setExecutor(new mobLoreCommand());
        getCommand("shutdown").setExecutor(new shutdownCommand(this));


    }

    @Override
    public void onDisable() {
        getLogger().info("dc plugin shutting down");
        // Plugin shutdown logic
    }
}
