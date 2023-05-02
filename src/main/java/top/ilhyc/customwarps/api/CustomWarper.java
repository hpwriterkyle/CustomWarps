package top.ilhyc.customwarps.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import top.ilhyc.customwarps.CustomWarps;
import top.ilhyc.customwarps.LimitField;
import top.ilhyc.customwarps.PluginData;
import top.ilhyc.customwarps.WarpPoint;
import top.ilhyc.customwarps.gui.MainGui;
import top.ilhyc.customwarps.gui.RemoveGui;

import java.util.List;
import java.util.UUID;

public class CustomWarper {
    public static List<String> bannedWorlds;
    public CustomWarper(){
        bannedWorlds = PluginData.getConfig().getStringList("bannedworld");
    }

    public List<WarpPoint> getWarps(UUID uuid){
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        return CustomWarps.map.get(player.getName());
    }

    public List<WarpPoint> getWarps(String name){
        return CustomWarps.map.get(name);
    }

    public boolean isLimited(Location l){
        return CustomWarps.limitedmap.values().stream().anyMatch(a->new LimitField(a[0],a[1]).inLimited(l));
    }

    /*
    @param uuid -> player uuid
    @param page -> gui page [start at zero]
     */
    public void openTeleportMenu(UUID uuid,int page){
        Player p = Bukkit.getPlayer(uuid);
        if(p!=null){
            p.openInventory(MainGui.getGui(p,true,0));
        }
    }

    /*
    @param uuid -> player uuid
    @param page -> gui page [start at zero]
     */
    public void openRemoveMenu(UUID uuid,int page){
        Player p = Bukkit.getPlayer(uuid);
        if(p!=null){
            p.openInventory(RemoveGui.getGui(p,true,0));
        }
    }

    /*
    including CREATE and EDIT
    @param name -> player name
    @param location_1 -> the first location
    @param location_2 -> the second location
    */
    public void setLimit(String name,Location location_1,Location location_2){
        LimitField lf;
        CustomWarps.limitfields.put(name,lf = new LimitField(location_1,location_2));
        PluginData pd = new PluginData(CustomWarps.data,"data.yml");
        pd.setLocation("limitedfields."+name+".first",lf.getOneSide());
        pd.setLocation("limitedfields."+name+".second",lf.getOneSide());
        pd.save();
    }

    public void removeLimit(String name){
        CustomWarps.limitfields.remove(name);
        PluginData pd = new PluginData(CustomWarps.data,"data.yml");
        pd.set("limitedfields."+name,null);
        pd.save();
    }

    public boolean isBanned(World world){
        for(String s:bannedWorlds){
            if(s.equals(world.getName())){
                return true;
            }
            if(!s.contains(":")) {
                continue;
            }
                String[] ss = s.split(":");
                switch (ss[1]){
                    case "prefix":
                        if(world.getName().startsWith(ss[0])){
                            return true;
                        }
                        break;
                    case "suffix":
                        if(world.getName().endsWith(ss[0])){
                            return true;
                        }
                        break;
                    case "contains":
                        if(world.getName().contains(ss[0])){
                            return true;
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("其匹配模式填写有误!");
                }
        }
        return false;
    }
}
