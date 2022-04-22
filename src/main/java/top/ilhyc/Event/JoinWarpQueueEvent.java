package top.ilhyc.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import top.ilhyc.WarpPoint;

public class JoinWarpQueueEvent extends PlayerEvent {
    public static HandlerList handlerList = new HandlerList();
    public WarpPoint wp;

    public JoinWarpQueueEvent(Player who,WarpPoint wp) {
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
}
