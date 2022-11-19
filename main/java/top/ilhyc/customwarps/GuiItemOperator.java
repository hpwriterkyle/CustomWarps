package top.ilhyc.customwarps;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiItemOperator {
    public static ItemStack createItem(Material material) {
        ItemStack is = new ItemStack(material);
        return is;
    }

    public static ItemStack createItem(Material material, short data) {
        ItemStack is = new ItemStack(material, 1, data);
        return is;
    }

    public static ItemStack createItem(Material material, short data, int amount) {
        ItemStack is = new ItemStack(material, amount, data);
        return is;
    }

    public static ItemStack createItem(Material material, short data, int amount, String display) {
        ItemStack is = new ItemStack(material, amount, data);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', display));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack createItem(Material material, short data, int amount, String display, List<String> lore) {
        ItemStack is = new ItemStack(material, amount, data);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', display));
        ArrayList<String> lores = new ArrayList<>();
        for(String s:lore){
            s = CustomWarps.Auto(s);
            lores.add(s);
        }
        im.setLore(lores);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack CreateItem(Material material, short data, int amount, String display, List<String> lore, String localizedname) {
        ItemStack is = new ItemStack(material, amount, data);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', display));
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    public static void SetItem(ItemStack is, Material m) {
        is.setType(m);
        ItemMeta im = is.getItemMeta();
        is.setItemMeta(im);
    }

    public static void SetItem(ItemStack is, Material m, String DisplayName) {
        is.setType(m);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(DisplayName);
        is.setItemMeta(im);
    }

    public static void SetItem(ItemStack is, Material m, String DisplayName, List<String> Lore) {
        is.setType(m);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(DisplayName);
        im.setLore(Lore);
        is.setItemMeta(im);
    }

    public static void SetItem(ItemStack is, Material m, String DisplayName, List<String> Lore, String LocalizedName) {
        is.setType(m);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(DisplayName);
        im.setLore(Lore);
        is.setItemMeta(im);
    }
}
