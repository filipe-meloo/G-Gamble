package me.filipe.ggamble.ggamble.commands;

import me.filipe.ggamble.ggamble.GGamble;
import me.filipe.ggamble.ggamble.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GambleCommand implements CommandExecutor {

    private static GGamble plugin;
    public GambleCommand(GGamble plugin) {
        GambleCommand.plugin = plugin;
        plugin.getCommand("gamble").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfig().getString("ggamble.messages.player-only"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(Utils.chat(plugin.getConfig().getString("gamble.messages.insuf-args")));
            return true;
        }

        Integer valor = Integer.valueOf(args[0]);
        if (valor < plugin.getConfig().getInt("gamble.min-bet")) {
            player.sendMessage(Utils.chat(plugin.getConfig().getString("gamble.messages.min-bet").replaceAll("%qt%", String.valueOf(plugin.getConfig().getInt("gamble.min-bet")))));
            return true;
        } else if (valor > plugin.getConfig().getInt("gamble.max-bet")) {
            player.sendMessage(Utils.chat(plugin.getConfig().getString("gamble.messages.max-bet").replaceAll("%qt%", String.valueOf(plugin.getConfig().getInt("gamble.max-bet")))));
            return true;
        }

        if (plugin.eco.getBalance(player) < valor) {
            player.sendMessage(Utils.chat(plugin.getConfig().getString("gamble.messages.no-money")));
            return true;
        }

        Inventory inv = Bukkit.createInventory(player, InventoryType.HOPPER, Utils.chat(plugin.getConfig().getString("gamble.inventory.title").replaceAll("%value%", valor.toString())));

        inv.setItem(2, getMoeda(valor));

        ItemStack cancelar = Utils.createItem(Material.RED_STAINED_GLASS_PANE
                , 1
                , plugin.getConfig().getString("gamble.inventory.items.cancel.name")
                , null);

        ItemStack confirmar = Utils.createItem(Material.GREEN_STAINED_GLASS_PANE
                , 1
                , plugin.getConfig().getString("gamble.inventory.items.confirm.name")
                , null);

        inv.setItem(0, cancelar);
        inv.setItem(1, cancelar);

        inv.setItem(3, confirmar);
        inv.setItem(4, confirmar);

        player.openInventory(inv);

        return true;
    }

    public static ItemStack getMoeda(Integer valor) {
        //Converter da config para um array
        List<String> lore = plugin.getConfig().getStringList("gamble.inventory.items.coin.lore");
        for (int i = 0; i < lore.size(); i++) {
            String a = lore.get(i);

            lore.set(i, a.replaceAll("%value%", String.valueOf(valor)));
            lore.set(i, a.replaceAll("%value2%", String.valueOf(valor*2)));
        }

        return Utils.createItem(Material.SUNFLOWER
                , 1
                , plugin.getConfig().getString("gamble.inventory.items.coin.name")
                , lore);
    }
}
