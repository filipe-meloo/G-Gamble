package me.filipe.ggamble.ggamble.events;

import de.tr7zw.nbtapi.NBTItem;
import me.filipe.ggamble.ggamble.GGamble;
import me.filipe.ggamble.ggamble.InitMethods;
import me.filipe.ggamble.ggamble.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
        ItemStack item = null;
        if (event.getHand() == EquipmentSlot.HAND) {
            item = player.getInventory().getItemInMainHand();
        } else {
            item = player.getInventory().getItemInOffHand();
        }
        NBTItem nbti = new NBTItem(item);

        if (!nbti.hasKey("Caixa")) {
            return;
        }

        if (!InitMethods.getCaixas().contains(nbti.getString("Caixa"))) {
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(Utils.chat(plugin.getConfig().getString("caixas.messages.no-space-open")));
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);

        String config = "caixas." + nbti.getString("Caixa");

        //Calcular recompensa
        List<ItemStack> recompensas = InitMethods.getRecompensaItem(nbti.getString("Caixa"));
        List<String> comandos = InitMethods.getRecompensaComando(nbti.getString("Caixa"));
        List<Integer> chances = InitMethods.getRecompensaChance(nbti.getString("Caixa"));

        List<ItemStack> recompensa = new ArrayList<>();
        List<String> comando = new ArrayList<>();

        for (int i = 0; i < chances.size(); i++) {
            for (int j = 0; j < chances.get(i); j++) {
                recompensa.add(recompensas.get(i));
                comando.add(comandos.get(i));
            }
        }

        Random random = new Random();
        Integer rand = random.nextInt(recompensas.size());

        ItemStack itemAdd = recompensa.get(rand);
        String comandoAdd = comando.get(rand);

        if (itemAdd != null) {
            player.getInventory().addItem(itemAdd);
        }
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), comandoAdd.replaceAll("%player%", player.getName()));
        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_1, 1f, 2f);

        //Colocar bau no chao e abrir
        Location location = event.getBlock().getLocation();

        UUID uuid = UUID.randomUUID();
        ArmorStand hologram = Utils.createHologram(
                plugin.getConfig().getString("caixas.hologram.open").replaceAll("%reward%", Utils.chat(itemAdd.getItemMeta().getDisplayName())),
                location.add(0.5, -0.75, 0.5),
                player.getWorld()
        );

        if (itemAdd != null) {
            Item item1 = player.getWorld().dropItem(location.add(0.5, 0.75, 0.5), itemAdd);
            item1.setGravity(false);
            item1.setPickupDelay(99999);
            hologram.addPassenger(item1);

            int tempo = 5;
            new BukkitRunnable() {
                @Override
                public void run() {
                    hologram.removePassenger(item1);
                    item1.remove();
                }
            }.runTaskLater(plugin, (tempo * 20) - 1);

        }

        int tempo = 5;
        new BukkitRunnable() {
            @Override
            public void run() {
                hologram.remove();
            }
        }.runTaskLater(plugin, tempo * 20);


    }

}
