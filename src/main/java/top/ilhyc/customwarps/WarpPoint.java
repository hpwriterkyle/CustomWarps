package top.ilhyc.customwarps;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class WarpPoint {
    public String name;
    public Location location;
    public int ser;

    public static void storeWarpPoint(WarpPoint wp, Player p){
        PluginData pd = new PluginData(CustomWarps.playerdata,p.getName()+".yml");
        pd.set("warps."+ CustomWarps.map.get(p.getName()).size()+".name",wp.name);
        pd.setLocation("warps."+ CustomWarps.map.get(p.getName()).size()+".location",wp.location);
        pd.save();
    }

    public static void unstoreWarpPoint(int i, Player p){
        PluginData pd = new PluginData(CustomWarps.playerdata,p.getName()+".yml");
        for(int n = 0;n<=i;n++){
            if(i==n){
                pd.set("warps",null);
                pd.save();
                restoreWarpPoint(CustomWarps.map.get(p.getName()),p);
                break;
            }
        }
    }

    public static void restoreWarpPoint(List<WarpPoint> lwp, Player p){
        PluginData pd = new PluginData(CustomWarps.playerdata,p.getName()+".yml");
        int i =0;
        if(lwp!=null) {
            for (WarpPoint wp : lwp) {
                pd.set("warps." + i + ".name", wp.name);
                pd.setLocation("warps." + i + ".location", wp.location);
                i++;
            }
            pd.save();
        }
    }

    public String getName(){
        return CustomWarps.Auto(name);
    }
}
