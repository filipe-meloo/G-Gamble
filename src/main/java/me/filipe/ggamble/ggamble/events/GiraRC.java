package me.filipe.ggamble.ggamble.events;

import de.tr7zw.nbtapi.NBTItem;
import me.filipe.ggamble.ggamble.GGamble;
import me.filipe.ggamble.ggamble.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class GiraRC implements Listener {

    private GGamble plugin;
    public GiraRC(GGamble plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onGiraRightClick(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.SUNFLOWER) return;

        boolean hand = event.getHand() != EquipmentSlot.OFF_HAND;

        if (event.getItem().getItemMeta().getDisplayName().equals(Utils.chat(plugin.getConfig().getString("gamble.inventory.items.coin.name")))) {
            List<String> lore = event.getItem().getItemMeta().getLore();
            Player player = (Player) event.getPlayer();
            if (lore == null) return;

            ItemStack itemNaMao = event.getItem();
            NBTItem nbti = new NBTItem(itemNaMao);
            Integer value = nbti.getInteger("Valor");

            Random random = new Random();
            final int num = 100;
            Integer tentou = random.nextInt(num) + 1;
            int a = plugin.getConfig().getInt("gamble.win-chance");

            Bukkit.getConsoleSender().sendMessage(String.valueOf(num));
            Bukkit.getConsoleSender().sendMessage(String.valueOf(num * (a / 100)));

            if (tentou < a) {
                player.sendMessage(Utils.chat(plugin.getConfig().getString("gamble.messages.win").replaceAll("%value%", String.valueOf(value*2))));
                player.playSound(player, Sound.ITEM_GOAT_HORN_SOUND_0, 1F, 2F);
                plugin.eco.depositPlayer(player, value*2);
            } else {
                player.sendMessage(Utils.chat(plugin.getConfig().getString("gamble.messages.lose").replaceAll("%value%", String.valueOf(value))));
                player.playSound(player, Sound.BLOCK_CHEST_LOCKED, 1F, 0F);
            }

            if (hand) {
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
            } else {
                player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
            }
            event.setCancelled(true);
        }
    }
}
