package me.filipe.ggamble.ggamble;

import me.filipe.ggamble.ggamble.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitMethods {

    private static List<String> caixas = new ArrayList<String>();
    private static Map<String, List<ItemStack>> recompensaItem = new HashMap<String, List<ItemStack>>();
    private static Map<String, List<String>> recompensaComando = new HashMap<String, List<String>>();
    private static Map<String, List<Integer>> recompensaChance = new HashMap<String, List<Integer>>();

    public static void loadCaixas(GGamble plugin) {

        caixas.clear();
        recompensaItem.clear();
        recompensaComando.clear();

        for(String s : plugin.getConfig().getConfigurationSection("caixas").getKeys(false)) {
            List<ItemStack> itensAdd = new ArrayList<>();
            List<String> comandos = new ArrayList<>();
            List<Integer> chance = new ArrayList<>();

            if (!s.equals("messages") && !s.equals("hologram")) {
                for (String a : plugin.getConfig().getConfigurationSection("caixas." + s + ".rewards").getKeys(false)) {
                    String config = "caixas." + s + ".rewards." + a + ".";

                    if (plugin.getConfig().getBoolean(config + "item")) {
                        ItemStack itemAdd = Utils.createItem (
                                Material.getMaterial(plugin.getConfig().getString(config + "material")),
                                plugin.getConfig().getInt(config + "quantity"),
                                plugin.getConfig().getString(config + "name"),
                                plugin.getConfig().getStringList(config + "lore")
                        );

                        itensAdd.add(itemAdd);
                    } else {
                        itensAdd.add(null);
                    }

                    String comando = plugin.getConfig().getString(config + "command");
                    comandos.add(comando);

                    Integer chancee = plugin.getConfig().getInt(config + "chance");
                    chance.add(chancee);
                }

                recompensaChance.put(s, chance);
                recompensaItem.put(s, itensAdd);
                recompensaComando.put(s, comandos);
                caixas.add(s);
            }
        }
    }

    public static List<Integer> getRecompensaChance(String s) {
        return recompensaChance.get(s);
    }

    public static List<ItemStack> getRecompensaItem(String s) {
        return recompensaItem.get(s);
    }

    public static List<String> getRecompensaComando(String s) {
        return recompensaComando.get(s);
    }

    public static List<String> getCaixas() {
        return caixas;
    }
}
