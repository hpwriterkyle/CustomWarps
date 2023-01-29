package top.ilhyc.customwarps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import top.ilhyc.customwarps.events.JoinWarpQueueEvent;

import java.util.List;

public class WarpPoint {
    private String name;
    private Location location;
    private int order;

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /*

        @param p -> warped player
        @param queue -> if join to the queue for warping
        @param cooldown -> if with cooldown
        @return -> return different WarpResults depending on the situation
     */
    public WarpResult warp(Player p,boolean queue,boolean cooldown){
        if (cooldown&&CustomWarps.inCooldown(System.currentTimeMillis(),p.getName())) {
            return WarpResult.WITHIN_COOLDOWN;
        }
        if(!queue){
            p.teleport(p.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            return WarpResult.FORCED_WARPED;
        }
        JoinWarpQueueEvent event = new JoinWarpQueueEvent(p, this);
        //             if (customwarps.getEco().withdrawPlayer(e.getWhoClicked().getName(), PluginData.getConfig().getDouble("default.cost-teleport")).transactionSuccess()) {
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return WarpResult.CANCELLED;
        }
        event.work();
        return WarpResult.JOIN_QUEUE;
    }

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

    public enum WarpResult{
        WITHIN_COOLDOWN,
        CANCELLED,
        JOIN_QUEUE,
        FORCED_WARPED
    }
}
