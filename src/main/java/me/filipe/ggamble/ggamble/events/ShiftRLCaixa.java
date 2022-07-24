package me.filipe.ggamble.ggamble.events;

import de.tr7zw.nbtapi.NBTItem;
import me.filipe.ggamble.ggamble.GGamble;
import me.filipe.ggamble.ggamble.InitMethods;
import me.filipe.ggamble.ggamble.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShiftRLCaixa implements Listener {

    private GGamble plugin;
    public ShiftRLCaixa(GGamble plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = (Player) event.getPlayer();
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

        event.setCancelled(true);
        String config = "caixas." + nbti.getString("Caixa") + ".rewards";
        String configgui = "caixas.gui-rewards.";

        List<ItemStack> recompensas = InitMethods.getRecompensaItem(nbti.getString("Caixa"));
        List<String> comandos = InitMethods.getRecompensaComando(nbti.getString("Caixa"));

        Inventory inv = Bukkit.createInventory(player, plugin.getConfig().getInt(configgui + "size"), plugin.getConfig().getString(configgui + "title"));
        ItemStack fechar = Utils.createItem(
                Material.getMaterial(plugin.getConfig().getString(configgui + "close.material")),
                1,
                plugin.getConfig().getString(configgui + "close.name"),
                plugin.getConfig().getStringList(configgui + "close.lore")
        );

        inv.setItem((plugin.getConfig().getInt(configgui + "size") * 9 - 4), fechar);

        int j = 10;
        for (ItemStack i : recompensas) {
            inv.setItem(j, i);
            j++;
            if (j == 16 || j == 25 || j == 34) {
                j += 3;
            }
        }

        if (j == 16 || j == 25 || j == 34) {
            j += 3;
        }
        for (String s : comandos) {
            ItemStack i = Utils.createItem(Material.NAME_TAG, 1, "/" + s, null);
            inv.setItem(j, i);
            j++;
            if (j == 16 || j == 25 || j == 34) {
                j += 3;
            }
        }

        player.openInventory(inv);
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent event) {

    }

}
