package me.filipe.ggamble.ggamble.commands;

import me.filipe.ggamble.ggamble.GGamble;
import me.filipe.ggamble.ggamble.InitMethods;
import me.filipe.ggamble.ggamble.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCaixas implements CommandExecutor {

    private GGamble plugin;
    public ListCaixas(GGamble plugin) {
        this.plugin = plugin;
        plugin.getCommand("listcaixas").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("listcaixas")) {
            sender.sendMessage(Utils.chat("&6&l&m==================================="));

            sender.sendMessage(Utils.chat("&a&lCaixas Disponíveis: "));

            List<String> caixas = InitMethods.getCaixas();
            for (String a : caixas) {
                sender.sendMessage(Utils.chat("   &1- &e" + a));
            }

            sender.sendMessage(Utils.chat("&6&l&m==================================="));

            return true;
        }
        return false;
    }
}
