package me.filipe.ggamble.ggamble.commands;

import de.tr7zw.nbtapi.NBTItem;
import me.filipe.ggamble.ggamble.GGamble;
import me.filipe.ggamble.ggamble.InitMethods;
import me.filipe.ggamble.ggamble.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCaixa implements CommandExecutor {

    private GGamble plugin;
    public GiveCaixa(GGamble plugin) {
        this.plugin = plugin;
        plugin.getCommand("givecaixa").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("givecaixa")) {
            if (args.length != 3) {
                sender.sendMessage(Utils.chat(plugin.getConfig().getString("caixas.messages.invalid-give")));
                return true;
            }

            if (!InitMethods.getCaixas().contains(args[0])) {
                sender.sendMessage(Utils.chat(plugin.getConfig().getString("caixas.messages.no-exist")));
                return true;
            }

            Player player = Bukkit.getPlayer(args[1]);
            if (player == null || !player.isOnline()) {
                sender.sendMessage(Utils.chat(plugin.getConfig().getString("caixas.messages.invalid-player")));
                return true;
            }

            if (player.getInventory().firstEmpty() == -1) {
                sender.sendMessage(Utils.chat(plugin.getConfig().getString("caixas.messages.no-space")));
                return true;
            }

            Integer qt = Integer.parseInt(args[2]);
            if (qt <= 0 || qt > 64) {
                sender.sendMessage(Utils.chat(plugin.getConfig().getString("caixas.messages.invalid-qt")));
                return true;
            }

            //Tudo pronto para a execução
            String path = "caixas." + args[0] + ".";
            ItemStack item = Utils.createItem(Material.getMaterial(plugin.getConfig().getString(path + "material")),
                    Integer.parseInt(args[2]),
                    plugin.getConfig().getString(path + "name"),
                    plugin.getConfig().getStringList(path + "lore"));

            NBTItem nbti = new NBTItem(item);
            nbti.setString("Caixa", args[0]);

            player.getInventory().addItem(nbti.getItem());
            sender.sendMessage(Utils.chat(plugin.getConfig().getString("caixas.messages.success-give")
                    .replaceAll("%qt%", args[2]).replaceAll("%caixa%", args[0]).replaceAll("%player%", player.getDisplayName())));

            player.sendMessage(Utils.chat(plugin.getConfig().getString("caixas.messages.received")
                    .replaceAll("%qt%", args[2]).replaceAll("%caixa%", args[0])));

            return true;
        }
        return false;
    }
}
