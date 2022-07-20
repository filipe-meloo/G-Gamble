package me.filipe.ggamble.ggamble.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static ItemStack createItem(Material material, int amount, String displayName, List<String> loreString) {

        ItemStack item;
        List<String> lore = new ArrayList();

        item = new ItemStack(material, amount);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.chat(displayName));

        if (loreString != null) {
            for (String s : loreString) {
                lore.add(Utils.chat(s));
            }
            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        return item;
    }
}

