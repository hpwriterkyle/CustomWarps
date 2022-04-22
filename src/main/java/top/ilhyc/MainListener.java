package top.ilhyc;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiversePlugin;
import com.onarandombox.MultiverseCore.utils.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import top.ilhyc.Event.JoinWarpQueueEvent;
import top.ilhyc.Event.PrelimitedEvent;
import top.ilhyc.Gui.MainGui;
import top.ilhyc.Gui.RemoveGui;

import java.net.Proxy;
import java.util.HashMap;


public class MainListener implements Listener {
    @EventHandler
    public void clickWarp(InventoryClickEvent e){
        if(e.getInventory().getHolder() instanceof MainGui) {
            e.setCancelled(true);
            if (e.getSlotType() != InventoryType.SlotType.OUTSIDE) {
                if (e.getRawSlot() < 45) {
                    if (customwarps.map.get(e.getWhoClicked().getName()).size() >= e.getRawSlot() + 1) {
                        if (!customwarps.incooldown(System.currentTimeMillis(), e.getWhoClicked().getName())) {
                            //             if (customwarps.getEco().withdrawPlayer(e.getWhoClicked().getName(), PluginData.getConfig().getDouble("default.cost-teleport")).transactionSuccess()) {
                            Bukkit.getPluginManager().callEvent(new JoinWarpQueueEvent((Player) e.getWhoClicked(), customwarps.map.get(e.getWhoClicked().getName()).get(e.getRawSlot())));
                            e.getWhoClicked().closeInventory();
                            //             }
                            //     } else {
                            //         ((Player) e.getWhoClicked()).sendMessage(customwarps.Auto(PluginData.getConfig().getString("language.cost-teleport-failed")));
                            //     }
                        } else {
                            ((Player) e.getWhoClicked()).sendMessage(customwarps.Auto(PluginData.getConfig().getString("language.cooldown")));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void removeWarp(InventoryClickEvent e){
        if(e.getInventory().getHolder() instanceof RemoveGui) {
            e.setCancelled(true);
            if (e.getSlotType() != InventoryType.SlotType.OUTSIDE) {
                if (e.getRawSlot() < 45) {
                    if (customwarps.map.get(e.getWhoClicked().getName()).size() >= e.getRawSlot() + 1) {
                        e.getWhoClicked().closeInventory();
                        customwarps.map.get(e.getWhoClicked().getName()).remove(e.getRawSlot());
                        WarpPoint.unstoreWarpPoint(e.getRawSlot(), (Player) e.getWhoClicked());
                        ((Player) e.getWhoClicked()).sendMessage(customwarps.Auto(PluginData.getConfig().getString("language.remove-success")));
                    }
                }
            }
        }
    }

    @EventHandler
    public void joinQueue(JoinWarpQueueEvent e) {
        boolean allowed = customwarps.getEco() == null;
        if (allowed||customwarps.getEco().withdrawPlayer(e.getPlayer().getName(), PluginData.getConfig().getInt("default.cost-set")).transactionSuccess()) {
            (e.getPlayer()).sendMessage(customwarps.Auto(PluginData.getConfig().getString("language.start-teleport")));
            customwarps.warpqueue.add(e.getPlayer());
            BukkitTask bt = new BukkitRunnable() {
                int i = PluginData.getConfig().getInt("default.wait-time") + 1;

                @Override
                public void run() {
                    i--;
                    (e.getPlayer()).sendMessage(customwarps.Auto("&a") + i);
                    if (!customwarps.warpqueue.contains(e.getPlayer())) {
                        this.cancel();
                        (e.getPlayer()).sendMessage(customwarps.Auto(PluginData.getConfig().getString("language.block-teleport")));
                    }
                    if (i == 0) {
                        e.getPlayer().teleport(e.getWp().location);
                        customwarps.cooldown.put(e.getPlayer().getName(), System.currentTimeMillis());
                        (e.getPlayer()).sendMessage(customwarps.Auto(PluginData.getConfig().getString("language.success-teleport")));
                        this.cancel();
                    }
                }
            }.runTaskTimer(customwarps.getPlugin(top.ilhyc.customwarps.class), 0, 20);
        }else{
            e.getPlayer().sendMessage(customwarps.Auto(PluginData.getConfig().getString("language.cost-teleport-failed")));
        }
    }

    @EventHandler
    public void getMoved(PlayerMoveEvent e){
        if(e.getFrom().getBlockX()!=e.getTo().getBlockX()||e.getFrom().getBlockY()!=e.getTo().getBlockY()||e.getFrom().getBlockZ()!=e.getTo().getBlockZ()){
            if(customwarps.warpqueue.contains(e.getPlayer())){
                customwarps.warpqueue.remove(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void getAttacked(EntityDamageEvent e){
        if(e.getEntityType()== EntityType.PLAYER) {
            Player p = (Player)e.getEntity();
                if (customwarps.warpqueue.contains(p)) {
                    customwarps.warpqueue.remove(p);
                }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            if(e.getItem()!=null) {
                if (e.getPlayer().isOp() && e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getItem().getType() == Material.FISHING_ROD) {
                    Bukkit.getServer().getPluginManager().callEvent(new PrelimitedEvent(e.getPlayer(), e.getClickedBlock().getLocation(), e.getAction() == Action.LEFT_CLICK_BLOCK ? PrelimitedEvent.Type.Left : PrelimitedEvent.Type.Right));
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPrelimited(PrelimitedEvent e){
        if(!customwarps.limitedmap.containsKey(e.getPlayer())) {
            customwarps.limitedmap.put(e.getPlayer(), new Location[2]);
        }
            if (e.getType() == PrelimitedEvent.Type.Left) {
                customwarps.limitedmap.get(e.getPlayer())[0] = e.getClicked();
                e.getPlayer().sendMessage(customwarps.Auto("&a您当前已选中第一个坐标于&6" + e.getClicked().toString()));
            } else {
                customwarps.limitedmap.get(e.getPlayer())[1] = e.getClicked();
                e.getPlayer().sendMessage(customwarps.Auto("&a您当前已选中第二个坐标于&6" + e.getClicked().toString()));
            }
    }
}
