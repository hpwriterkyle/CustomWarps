package top.ilhyc.customwarps.Gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import top.ilhyc.customwarps.GuiItemOperator;
import top.ilhyc.customwarps.PluginData;
import top.ilhyc.customwarps.WarpPoint;
import top.ilhyc.customwarps.customwarps;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class MainGui implements Listener, InventoryHolder {
    Player p;
    int page = 0;
    @Override
    public Inventory getInventory() {
        return null;
    }

    public MainGui(Player p){
        this.p = p;
    }

    public MainGui(Player p,int page){
        this.p = p;
        this.page = page;
    }

    public static Inventory getGui(Player p,boolean b) {
            int inn = 54;
            PluginData pd = new PluginData(customwarps.playerdata, p.getName() + ".yml");
            Inventory in = Bukkit.createInventory(new MainGui(p), inn, customwarps.Auto(PluginData.getConfig().getString("gui.display")));
            for (int i = 45; i <54; i++) {
                in.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, (short) 1));
            }
            in.setItem(49, GuiItemOperator.createItem(Material.ANVIL, (short) 0, customwarps.map.get(p.getName()).size(), "&a" + customwarps.map.get(p.getName()).size() + "&7/&c" + (pd.getInt("number") == 0 ? PluginData.getConfig().getInt("default.warps") : pd.getInt("number"))));
            in.setItem(53,GuiItemOperator.createItem(Material.ARROW,(short) 0,1,"&f下一页",Arrays.asList("&c已是最后一页")));
            in.setItem(45,GuiItemOperator.createItem(Material.ARROW,(short) 0,1,"&f上一页",Arrays.asList("&c已是第一页")));
            if (customwarps.map.get(p.getName()) != null) {
                int i = 0;
                for (WarpPoint wp : customwarps.map.get(p.getName())) {
                    if(i<45) {
                        ItemStack is = GuiItemOperator.createItem(Material.getMaterial(PluginData.getConfig().getInt("gui.item.id")), (short) PluginData.getConfig().getInt("gui.item.data"), 1, wp.getName(), RemoveGui.replacedString(PluginData.getConfig().getStringList("language.item-description-normal"), wp));
                        in.setItem(i, is);
                        i++;
                    }else{
                        break;
                    }
                }
            }
            return in;
    }

    public static Inventory getGui(Player p,boolean b,int page) {
        int inn = 54;
        PluginData pd = new PluginData(customwarps.playerdata, p.getName() + ".yml");
        Inventory in = Bukkit.createInventory(new MainGui(p,page), inn, customwarps.Auto(PluginData.getConfig().getString("gui.display")));
        for (int i = 45; i <54; i++) {
            in.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, (short) 1));
        }
        in.setItem(49, GuiItemOperator.createItem(Material.ANVIL, (short) 0, customwarps.map.get(p.getName()).size(), "&a" + customwarps.map.get(p.getName()).size() + "&7/&c" + (pd.getInt("number") == 0 ? PluginData.getConfig().getInt("default.warps") : pd.getInt("number"))));
        in.setItem(53,GuiItemOperator.createItem(Material.ARROW,(short) 0,1,"&f下一页",((page+1)*45)>customwarps.map.get(p.getName()).size()?Arrays.asList("&c已是最后一页"):Arrays.asList("&7点击进入下一页")));
        in.setItem(45,GuiItemOperator.createItem(Material.ARROW,(short) 0,1,"&f上一页",page==0?Arrays.asList("&c已是第一页"):Arrays.asList("&7点击返回上一页")));
        if (customwarps.map.get(p.getName()) != null) {
            int i = 0;
            for (WarpPoint wp : customwarps.map.get(p.getName())) {
                if(i>=page*45&&i<(page+1)*45) {
                    ItemStack is = GuiItemOperator.createItem(Material.getMaterial(PluginData.getConfig().getInt("gui.item.id")), (short) PluginData.getConfig().getInt("gui.item.data"), 1, wp.getName(), RemoveGui.replacedString(PluginData.getConfig().getStringList("language.item-description-normal"), wp));
                    in.setItem(i-page*45, is);
                }
                i++;
            }
        }
        return in;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
