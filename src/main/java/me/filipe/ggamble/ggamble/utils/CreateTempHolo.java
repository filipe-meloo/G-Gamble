package me.filipe.ggamble.ggamble.utils;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.filipe.ggamble.ggamble.GGamble;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class CreateTempHolo {

    public void createSimpleHolo(String name, List<String> texto, Player player, int tempo, GGamble plugin) {
        Location loc = player.getLocation();
        UUID rand = UUID.randomUUID();
        String nome = rand.toString();

        Hologram hologram = DHAPI.createHologram(nome, loc);

        for (String add : texto) {
            DHAPI.addHologramLine(hologram, add);
        }

        tempo = tempo * 20;
        new BukkitRunnable() {
            @Override
            public void run() {
                DHAPI.removeHologram(nome);
            }
        }.runTaskLater(plugin, tempo);

    }

    public void createItemHolo(String name, List<String> texto, ItemStack item, Player player, int tempo, GGamble plugin) {
        Location loc = player.getLocation();
        UUID rand = UUID.randomUUID();
        String nome = rand.toString();

        Hologram hologram = DHAPI.createHologram(nome, loc);

        for (String add : texto) {
            DHAPI.addHologramLine(hologram, add);
        }

        DHAPI.addHologramLine(hologram, item);

        tempo = tempo * 20;
        new BukkitRunnable() {
            @Override
            public void run() {
                DHAPI.removeHologram(nome);
            }
        }.runTaskLater(plugin, tempo);
    }

}
