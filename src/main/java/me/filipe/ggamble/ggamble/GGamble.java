package me.filipe.ggamble.ggamble;

import me.filipe.ggamble.ggamble.commands.GambleCommand;
import me.filipe.ggamble.ggamble.commands.GiveCaixa;
import me.filipe.ggamble.ggamble.commands.ListCaixas;
import me.filipe.ggamble.ggamble.events.GiraRC;
import me.filipe.ggamble.ggamble.events.InventoryClick;
import me.filipe.ggamble.ggamble.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class GGamble extends JavaPlugin {

    public Economy eco;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        Bukkit.getConsoleSender().sendMessage("G-Gamble On.");

        if (!setupEconomy()) {
            System.out.println(Utils.chat("&cVault em falta."));
            getServer().getPluginManager().disablePlugin(this);
        }

        InitMethods.loadCaixas(this);

        new GambleCommand(this);
        new InventoryClick(this);
        new GiraRC(this);

        new GiveCaixa(this);
        new ListCaixas(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage("G-Gamble Off.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ggamble")) {
            if (!sender.hasPermission(getConfig().getString("ggamble.permission"))) {
                sender.sendMessage(Utils.chat(getConfig().getString("ggamble.messages.no-perms")));
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage(Utils.chat(getConfig().getString("ggamble.messages.invalid-message")));
                return true;
            }

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                    sender.sendMessage(Utils.chat(this.getConfig().getString("ggamble.messages.reload-message")));
                    InitMethods.loadCaixas(this);
                    this.reloadConfig();
                }
            }

            return true;
        }
        return false;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economy = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economy != null)
            eco = economy.getProvider();
        return (eco != null);
    }
}
