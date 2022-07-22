package me.filipe.ggamble.ggamble;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class InitMethods {

    private static List<String> caixas = new ArrayList<String>();

    public static void loadCaixas(GGamble plugin) {

        caixas.clear();
        for(String s : plugin.getConfig().getConfigurationSection("caixas").getKeys(false)){
            if (!s.equals("messages") && !s.equals("hologram")) {
                caixas.add(s);
            }
        }

    }

    public static List<String> getCaixas() {
        return caixas;
    }
}
