package top.ilhyc.customwarps.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import top.ilhyc.customwarps.CustomWarps;
import top.ilhyc.customwarps.GuiItemOperator;
import top.ilhyc.customwarps.PluginData;
import top.ilhyc.customwarps.WarpPoint;
import top.ilhyc.customwarps.permissions.PermissionManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveGui implements InventoryHolder,WarpGui{
    Player p;
    int page = 0;
    @Override
    public Inventory getInventory() {
        return null;
    }

    public RemoveGui(Player p){
        this.p = p;
    }

    public RemoveGui(Player p,int page){
        this.p = p;
        this.page = page;
    }

    public static Inventory getGui(Player p,boolean b){
        int inn = 54;
        Inventory in = Bukkit.createInventory(new RemoveGui(p),inn, CustomWarps.Auto(PluginData.getConfig().getString("gui.display")));
        int mp = -1;
        try{
            mp = PermissionManager.getPermissionObject("customwarps.limit",p,a->a.equals("*")?-2:Integer.parseInt(a));
        }catch (Exception ignored){
        }
        String amount = "∞";
        if(mp!=-2){
            amount = mp+"";
        }
        for(int i =  45;i<inn;i++){
            in.setItem(i,new ItemStack(Material.STAINED_GLASS_PANE,(short)1));
        }
        in.setItem(49,GuiItemOperator.createItem(Material.ANVIL,(short) 0, CustomWarps.map.get(p.getName()).size(), "&a"+ CustomWarps.map.get(p.getName()).size()+"&7/&c"+(mp==0? PluginData.getConfig().getInt("default.warps"):amount)));
        in.setItem(53,GuiItemOperator.createItem(Material.ARROW,(short) 0,1,"&f下一页",Arrays.asList("&c已是最后一页")));
        in.setItem(45,GuiItemOperator.createItem(Material.ARROW,(short) 0,1,"&f上一页",Arrays.asList("&c已是第一页")));
        if(CustomWarps.map.get(p.getName())!=null){
            int i=0;
            for(WarpPoint wp: CustomWarps.map.get(p.getName())){
                if(i<45) {
                    ItemStack is = GuiItemOperator.createItem(Material.getMaterial(PluginData.getConfig().getInt("gui.item.id")), (short) PluginData.getConfig().getInt("gui.item.data"), 1, wp.getName(), replacedString(PluginData.getConfig().getStringList("language.item-description-remove"), wp));
                    in.setItem(i, is);
                    i++;
                }//
            }
        }
        return in;
    }

    public static Inventory getGui(Player p,boolean b,int page){
        int inn = 54;
        Inventory in = Bukkit.createInventory(new RemoveGui(p,page),inn, CustomWarps.Auto(PluginData.getConfig().getString("gui.display")));
      //  PluginData pd = new PluginData(CustomWarps.playerdata,p.getName()+".yml");
        int mp = -1;
        try{
            mp = PermissionManager.getPermissionObject("customwarps.limit",p,a->a.equals("*")?-2:Integer.parseInt(a));
        }catch (Exception ignored){
        }
        String amount = "∞";
        if(mp!=-2){
            amount = mp+"";
        }
        for(int i =  45;i<inn;i++){
            in.setItem(i,new ItemStack(Material.STAINED_GLASS_PANE,(short)1));
        }
        in.setItem(49,GuiItemOperator.createItem(Material.ANVIL,(short) 0, CustomWarps.map.get(p.getName()).size(), "&a"+ CustomWarps.map.get(p.getName()).size()+"&7/&c"+(mp==-1? PluginData.getConfig().getInt("default.warps"):amount)));
        in.setItem(53,GuiItemOperator.createItem(Material.ARROW,(short) 0,1,"&f下一页",(page+1)*45> CustomWarps.map.get(p.getName()).size()?Arrays.asList("&c已是最后一页"):Arrays.asList("&7点击进入下一页")));
        in.setItem(45,GuiItemOperator.createItem(Material.ARROW,(short) 0,1,"&f上一页",page==0?Arrays.asList("&c已是第一页"):Arrays.asList("&7点击返回上一页")));
        if(CustomWarps.map.get(p.getName())!=null){
            int i=0;
            for(WarpPoint wp: CustomWarps.map.get(p.getName())){
                if(i>=page*45&&i<(page+1)*45) {
                    ItemStack is = GuiItemOperator.createItem(Material.getMaterial(PluginData.getConfig().getInt("gui.item.id")), (short) PluginData.getConfig().getInt("gui.item.data"), 1, wp.getName(), replacedString(PluginData.getConfig().getStringList("language.item-description-remove"), wp));
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

    public static List<String> replacedString(List<String> s, WarpPoint wp){
        return s.stream().map(a->a.replaceAll("%w",wp.getLocation().getWorld().getName()).replaceAll("%x",wp.getLocation().getBlockX()+"").replaceAll("%y",wp.getLocation().getBlockY()+"").replace("%z",wp.getLocation().getBlockZ()+"").replace("%m",CustomWarps.core==null?wp.getLocation().getWorld().getName():CustomWarps.core.getMVWorldManager().getMVWorld(wp.getLocation().getWorld().getName()).getAlias())).collect(Collectors.toList());
    }
}
