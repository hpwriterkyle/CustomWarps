package top.ilhyc.customwarps.Event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PrelimitedEvent extends PlayerEvent {
    public static HandlerList handlerList = new HandlerList();
    private Location clicked;
    private Type type;
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
}
