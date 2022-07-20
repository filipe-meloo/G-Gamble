package me.filipe.ggamble.ggamble.commands;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.filipe.ggamble.ggamble.GGamble;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CreateTempHolo implements CommandExecutor {

    private GGamble plugin;
    public CreateTempHolo(GGamble plugin) {
        this.plugin = plugin;
        plugin.getCommand("createholo").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        Location loc = player.getLocation();

        Hologram holo = DHAPI.createHologram("name", loc);
        DHAPI.addHologramLine(holo, "ola");

        new BukkitRunnable() {
            @Override
            public void run() {
                DHAPI.removeHologram("name");
            }
        }.runTaskLater(plugin, 100);


        return false;
    }
}
