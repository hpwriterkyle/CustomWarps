package top.ilhyc.customwarps.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import top.ilhyc.customwarps.CustomWarps;
import top.ilhyc.customwarps.PluginData;
import top.ilhyc.customwarps.WarpPoint;

public class JoinWarpQueueEvent extends PlayerEvent implements Cancellable {
    public static HandlerList handlerList = new HandlerList();
    public WarpPoint wp;

    private boolean cancealled = false;

    public JoinWarpQueueEvent(Player who, WarpPoint wp) {
        super(who);
        this.wp = wp;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public void setWp(WarpPoint wp) {
        this.wp = wp;
    }

    public WarpPoint getWp() {
        return wp;
    }

    public void work(){
        boolean allowed = CustomWarps.getEco() == null;
        if (allowed || CustomWarps.getEco().withdrawPlayer(getPlayer().getName(), PluginData.getConfig().getInt("default.cost-teleport")).transactionSuccess()) {//
            (getPlayer()).sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.start-teleport")));
            CustomWarps.warpqueue.put(getPlayer().getUniqueId(), getWp());
            BukkitTask bt = new BukkitRunnable() {
                int i = PluginData.getConfig().getInt("default.wait-time") + 1;

                @Override
                public void run() {
                    i--;
                    if (i == 0) {
                        getPlayer().teleport(getWp().getLocation());
                        CustomWarps.cooldown.put(getPlayer().getName(), System.currentTimeMillis());
                        (getPlayer()).sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.success-teleport")));
                        this.cancel();
                        return;
                    }
                    (getPlayer()).sendMessage(CustomWarps.Auto("&a") + i);
                    if (!CustomWarps.warpqueue.keySet().contains(getPlayer().getUniqueId())) {
                        this.cancel();
                        (getPlayer()).sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.block-teleport")));
                    }
                }
            }.runTaskTimer(CustomWarps.getPlugin(CustomWarps.class), 0, 20);
        }else{
            getPlayer().sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.cost-teleport-failed")));
        }
    }

    @Override
    public boolean isCancelled() {
        return this.cancealled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancealled = b;
    }//

}
