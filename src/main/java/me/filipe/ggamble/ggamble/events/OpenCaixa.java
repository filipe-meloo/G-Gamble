package me.filipe.ggamble.ggamble.events;

import de.tr7zw.nbtapi.NBTItem;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.filipe.ggamble.ggamble.GGamble;
import me.filipe.ggamble.ggamble.InitMethods;
import me.filipe.ggamble.ggamble.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class OpenCaixa implements Listener {

    private GGamble plugin;
    public OpenCaixa(GGamble plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChestPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        NBTItem nbti = new NBTItem(item);

        if (!nbti.hasKey("Caixa")) {
            event.setCancelled(true);
            return;
        }

        if (!InitMethods.getCaixas().contains(nbti.getString("Caixa"))) {
            event.setCancelled(true);
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(Utils.chat(plugin.getConfig().getString("caixas.messages.no-space-open")));
            return;
        }

        if (event.getHand() == EquipmentSlot.HAND) {
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        } else {
            player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
        }

        String config = "caixas." + nbti.getString("Caixa");

        //Obter recompensas
        Map<Integer, ItemStack> recompensas = new HashMap<>();
        Map<Integer, String> comandos = new HashMap<>();
        Map<Integer, Integer> chance = new HashMap<>();

        int size = plugin.getConfig().getConfigurationSection(config).getKeys(false).size();
        for(int i = 1; i < size; i++) {
            if (plugin.getConfig().getBoolean(config + ".rewards." + String.valueOf(i) + ".item")) {
                recompensas.put(i, Utils.createItem(
                        Material.getMaterial(plugin.getConfig().getString(config + ".rewards." + String.valueOf(i) + ".material")),
                        plugin.getConfig().getInt(config + ".rewards." + String.valueOf(i) + ".amount"),
                        plugin.getConfig().getString(config + ".rewards." + String.valueOf(i) + ".name"),
                        plugin.getConfig().getStringList(config + ".rewards." + String.valueOf(i) + ".lore")

                ));
            } else {
                recompensas.put(i, null);
            }

            comandos.put(i, plugin.getConfig().getString(config + ".rewards." + String.valueOf(i) + ".command"));
            chance.put(i, plugin.getConfig().getInt(config + ".rewards." + String.valueOf(i) + ".chance"));
        }

        //Calcular recompensa
        List<ItemStack> recompensa = new ArrayList<>();
        List<String> comando = new ArrayList<>();

        for (int i = 1; i < chance.size(); i++) {
            for (int j = recompensas.size() + 1; j < chance.get(i); j++) {
                recompensa.add(recompensas.get(i));
                comando.add(comandos.get(i));
            }
        }

        Random random = new Random();
        Integer rand = random.nextInt(recompensas.size()) + 1;

        ItemStack itemAdd = recompensa.get(rand);
        String comandoAdd = comando.get(rand);

        if (itemAdd != null) {
            player.getInventory().addItem(itemAdd);
        }
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), comandoAdd.replaceAll("%player%", player.getName()));
        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_1, 1f, 2f);

        //Colocar bau no chao e abrir
        Location location = event.getBlockPlaced().getLocation();
        location.getBlock().setType(Material.getMaterial(plugin.getConfig().getString(config + ".material")));

        UUID uuid = UUID.randomUUID();
        Hologram holo = DHAPI.createHologram(uuid.toString(), location.add(0.5, 1, 0.5));

        DHAPI.addHologramLine(holo, plugin.getConfig().getString("caixas.hologram.open").replaceAll("%reward%", "cu"));
        if (itemAdd != null) {
            DHAPI.addHologramLine(holo, itemAdd);
        }

        int tempo = 5;
        new BukkitRunnable() {
            @Override
            public void run() {
                location.getBlock().setType(Material.AIR);
                DHAPI.removeHologram(uuid.toString());
            }
        }.runTaskLater(plugin, tempo * 20);


    }

}
