package top.ilhyc.customwarps.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import top.ilhyc.customwarps.CustomWarps;

public class PrelimitedEvent extends PlayerEvent implements Cancellable {
    public static HandlerList handlerList = new HandlerList();
    private Location clicked;
    private Type type;

    private boolean cancelled = false;
    public PrelimitedEvent(Player who,Location l,Type type) {
        super(who);
        this.clicked = l;
        this.type = type;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public enum Type{
        Right,
        Left,
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Location getClicked() {
        return clicked;
    }

    public Type getType() {
        return type;
    }

    public void setClicked(Location clicked) {
        this.clicked = clicked;
    }

    public void work(){
        if(!CustomWarps.limitedmap.containsKey(getPlayer())) {
            CustomWarps.limitedmap.put(getPlayer(), new Location[2]);
        }
        if (getType() == PrelimitedEvent.Type.Left) {
            CustomWarps.limitedmap.get(getPlayer())[0] = getClicked();
            getPlayer().sendMessage(CustomWarps.Auto("&a您当前已选中第一个坐标于&6" + getClicked().toString()));
        } else {
            CustomWarps.limitedmap.get(getPlayer())[1] = getClicked();
            getPlayer().sendMessage(CustomWarps.Auto("&a您当前已选中第二个坐标于&6" + getClicked().toString()));
        }
    }
}
