package me.filipe.ggamble.ggamble;

import java.util.ArrayList;
import java.util.List;

public class InitMethods {

    private static List<String> caixas = new ArrayList<String>();

    public static void loadCaixas(GGamble plugin) {
        caixas.addAll(plugin.getConfig().getConfigurationSection("caixas").getKeys(false));
    }

    public static List<String> getCaixas() {
        return caixas;
    }
}
