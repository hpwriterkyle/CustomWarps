package top.ilhyc.Gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftItem;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import top.ilhyc.GuiItemOperator;
import top.ilhyc.PluginData;
import top.ilhyc.WarpPoint;
import top.ilhyc.customwarps;

import java.beans.Customizer;
import java.io.File;

public class MainGui implements Listener, InventoryHolder {
    Player p;
    @Override
    public Inventory getInventory() {
        return null;
    }

    public MainGui(Player p){
        this.p = p;
    }

    public static Inventory getGui(Player p,boolean b) {
            int inn = 54;
            PluginData pd = new PluginData(customwarps.playerdata, p.getName() + ".yml");
            Inventory in = Bukkit.createInventory(new MainGui(p), inn, customwarps.Auto(PluginData.getConfig().getString("gui.display")));
            for (int i = 45; i <54; i++) {
                in.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, (short) 1));
            }
            in.setItem(49, GuiItemOperator.createItem(Material.ANVIL, (short) 0, customwarps.map.get(p.getName()).size(), "&a" + customwarps.map.get(p.getName()).size() + "&7/&c" + (pd.getInt("number") == 0 ? PluginData.getConfig().getInt("default.warps") : pd.getInt("number"))));
            if (customwarps.map.get(p.getName()) != null) {
                int i = 0;
                for (WarpPoint wp : customwarps.map.get(p.getName())) {
                    ItemStack is = GuiItemOperator.createItem(Material.getMaterial(PluginData.getConfig().getInt("gui.item.id")), (short) PluginData.getConfig().getInt("gui.item.data"), 1, wp.getName(), RemoveGui.replacedString(PluginData.getConfig().getStringList("language.item-description-normal"), wp));
                    in.setItem(i, is);
                    i++;
                }
            }
            return in;
    }
}
