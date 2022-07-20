package me.filipe.ggamble.ggamble.events;

import de.tr7zw.nbtapi.NBTItem;
import me.filipe.ggamble.ggamble.GGamble;
import me.filipe.ggamble.ggamble.commands.GambleCommand;
import me.filipe.ggamble.ggamble.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {

    private GGamble plugin;
    public InventoryClick(GGamble plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getCurrentItem() == null) return;
        if (event.getView().getBottomInventory() == event.getClickedInventory()) return;

        if (event.getClickedInventory().getType().equals(InventoryType.HOPPER)) {
            if (event.getView().getTitle().startsWith(plugin.getConfig().getString("gamble.inventory.title").replaceAll("%value%", ""))) {

                Player player = (Player) event.getWhoClicked();
                if (event.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE)) {
                    player.sendMessage(Utils.chat(plugin.getConfig().getString("gamble.messages.cancel")));
                    event.setCancelled(true);
                    player.closeInventory();
                    return;
                }

                if (event.getCurrentItem().getType().equals(Material.SUNFLOWER)) {
                    event.setCancelled(true);
                    return;
                }

                String valor = event.getView().getTitle();
                String titleDef = plugin.getConfig().getString("gamble.inventory.title");
                titleDef = titleDef.replaceAll("%value%", "");

                valor = valor.replaceAll(titleDef, "");
                Integer value = Integer.valueOf(valor);

                ItemStack moeda = GambleCommand.getMoeda(value);
                NBTItem nbti = new NBTItem(moeda);

                nbti.setInteger("Valor", value);

                player.getInventory().addItem(nbti.getItem());
                event.setCancelled(true);
                player.closeInventory();
                player.sendMessage(Utils.chat(plugin.getConfig().getString("gamble.messages.confirm").replaceAll("%value%", String.valueOf(value))));
                plugin.eco.withdrawPlayer(player, value);
            }
        }
    }

}
