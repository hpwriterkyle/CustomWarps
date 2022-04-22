package top.ilhyc.Gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.ilhyc.GuiItemOperator;
import top.ilhyc.PluginData;
import top.ilhyc.WarpPoint;
import top.ilhyc.customwarps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveGui implements InventoryHolder {
    Player p;
    @Override
    public Inventory getInventory() {
        return null;
    }

    public RemoveGui(Player p){
        this.p = p;
    }

    public static Inventory getGui(Player p,boolean b){
        int inn = 54;
        Inventory in = Bukkit.createInventory(new RemoveGui(p),inn, customwarps.Auto(PluginData.getConfig().getString("gui.display")));
        PluginData pd = new PluginData(customwarps.playerdata,p.getName()+".yml");
        for(int i =  45;i<inn;i++){
            in.setItem(i,new ItemStack(Material.STAINED_GLASS_PANE,(short)1));
        }
        in.setItem(49,GuiItemOperator.createItem(Material.ANVIL,(short) 0,customwarps.map.get(p.getName()).size(), "&a"+customwarps.map.get(p.getName()).size()+"&7/&c"+(pd.getInt("warps")==0?PluginData.getConfig().getInt("default.warps"):pd.getInt("warps"))));
        if(customwarps.map.get(p.getName())!=null){
            int i=0;
            for(WarpPoint wp:customwarps.map.get(p.getName())){
                ItemStack is = GuiItemOperator.createItem(Material.getMaterial(PluginData.getConfig().getInt("gui.item.id")),(short)PluginData.getConfig().getInt("gui.item.data"),1,wp.getName(),replacedString(PluginData.getConfig().getStringList("language.item-description-remove"),wp));
                in.setItem(i,is);
                i++;
            }
        }
        return in;
    }

    public static List<String> replacedString(List<String> s, WarpPoint wp){
        return s.stream().map(a->a.replaceAll("%w",wp.location.getWorld().getName()).replaceAll("%x",wp.location.getBlockX()+"").replaceAll("%y",wp.location.getBlockY()+"").replace("%z",wp.location.getBlockZ()+"")).collect(Collectors.toList());
    }
}
