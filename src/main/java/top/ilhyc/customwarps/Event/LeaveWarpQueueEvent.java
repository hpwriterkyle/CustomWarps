package top.ilhyc.customwarps.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import top.ilhyc.customwarps.WarpPoint;

public class LeaveWarpQueueEvent extends PlayerEvent {
    public static HandlerList handlerList = new HandlerList();
    public WarpPoint wp;

    public LeaveWarpQueueEvent(Player who, WarpPoint wp) {
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

    public WarpPoint getWp() {
        return wp;
    }

    public void setWp(WarpPoint wp) {
        this.wp = wp;
    }
}
