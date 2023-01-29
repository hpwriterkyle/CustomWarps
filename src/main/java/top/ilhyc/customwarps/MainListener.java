package top.ilhyc.customwarps;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import top.ilhyc.customwarps.events.JoinWarpQueueEvent;
import top.ilhyc.customwarps.events.LeaveWarpQueueEvent;
import top.ilhyc.customwarps.events.PrelimitedEvent;
import top.ilhyc.customwarps.gui.MainGui;
import top.ilhyc.customwarps.gui.RemoveGui;


public class MainListener implements Listener {//

    @EventHandler
    public void clickWarp(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        if (inventory != null && inventory.getHolder() != null && inventory.getHolder() instanceof MainGui) {
            e.setCancelled(true);
            if (inventory.equals(e.getView().getTopInventory())) {
                MainGui mg = (MainGui) e.getView().getTopInventory().getHolder();
                int slot = mg.getPage() * 45 + e.getRawSlot();
                if (e.getSlotType() != InventoryType.SlotType.OUTSIDE) {
                    if (e.getRawSlot() < 45) {
                        if (CustomWarps.map.get(e.getWhoClicked().getName()).size() >= slot + 1) {
                            if (!CustomWarps.inCooldown(System.currentTimeMillis(), e.getWhoClicked().getName())) {
                                JoinWarpQueueEvent event = new JoinWarpQueueEvent((Player) e.getWhoClicked(), CustomWarps.map.get(e.getWhoClicked().getName()).get(slot));
                                //             if (customwarps.getEco().withdrawPlayer(e.getWhoClicked().getName(), PluginData.getConfig().getDouble("default.cost-teleport")).transactionSuccess()) {
                                Bukkit.getPluginManager().callEvent(event);
                                e.getWhoClicked().closeInventory();
                                if(event.isCancelled()){
                                    return;
                                }
                                event.work();
                                //             }
                                //     } else {
                                //         ((Player) e.getWhoClicked()).sendMessage(customwarps.Auto(PluginData.getConfig().getString("language.cost-teleport-failed")));
                                //     }
                            } else {
                                ((Player) e.getWhoClicked()).sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.cooldown")));
                            }
                        }
                    } else {
                        switch (e.getRawSlot()) {
                            case 53:
                                if (CustomWarps.map.get(e.getWhoClicked().getName()).size() > (mg.getPage() + 1) * 45) {
                                    e.getWhoClicked().openInventory(MainGui.getGui((Player) e.getWhoClicked(), false, mg.getPage() + 1));
                                }
                                break;
                            case 45:
                                if (mg.getPage() != 0) {
                                    e.getWhoClicked().openInventory(MainGui.getGui((Player) e.getWhoClicked(), false, mg.getPage() - 1));
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void removeWarp(InventoryClickEvent e){
        Inventory inventory = e.getInventory();
        if(inventory!=null&&inventory.getHolder()!=null&&inventory.getHolder() instanceof RemoveGui) {
            e.setCancelled(true);
            if(inventory.equals(e.getView().getTopInventory())) {
                RemoveGui rg = (RemoveGui) e.getClickedInventory().getHolder();
                int slot = rg.getPage() + e.getRawSlot();
                e.setCancelled(true);
                if (e.getSlotType() != InventoryType.SlotType.OUTSIDE) {
                    if (e.getRawSlot() < 45) {
                        if (CustomWarps.map.get(e.getWhoClicked().getName()).size() >= slot + 1) {
                            e.getWhoClicked().closeInventory();
                            CustomWarps.map.get(e.getWhoClicked().getName()).remove(slot);
                            WarpPoint.unstoreWarpPoint(slot, (Player) e.getWhoClicked());
                            ((Player) e.getWhoClicked()).sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.remove-success")));
                        }
                    } else {
                        switch (e.getRawSlot()) {
                            case 53:
                                if (CustomWarps.map.get(e.getWhoClicked().getName()).size() > (rg.getPage() + 1) * 45) {
                                    e.getWhoClicked().openInventory(RemoveGui.getGui((Player) e.getWhoClicked(), false, rg.getPage() + 1));
                                }
                                break;
                            case 45:
                                if (rg.getPage() != 0) {
                                    e.getWhoClicked().openInventory(RemoveGui.getGui((Player) e.getWhoClicked(), false, rg.getPage() - 1));
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void getMoved(PlayerMoveEvent e){
        if(e.getFrom().getBlockX()!=e.getTo().getBlockX()||e.getFrom().getBlockY()!=e.getTo().getBlockY()||e.getFrom().getBlockZ()!=e.getTo().getBlockZ()){
            if(CustomWarps.warpqueue.keySet().contains(e.getPlayer().getUniqueId())){
                CustomWarps.warpqueue.remove(e.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler
    public void getAttacked(EntityDamageEvent e){
        if(e.getEntityType()== EntityType.PLAYER) {
            Player p = (Player)e.getEntity();
                if (CustomWarps.warpqueue.keySet().contains(p.getUniqueId())) {
                    LeaveWarpQueueEvent event = new LeaveWarpQueueEvent(p,CustomWarps.warpqueue.get(p.getUniqueId()));
                    Bukkit.getPluginManager().callEvent(event);
                    if(event.isCancelled()){
                        return;
                    }
                    CustomWarps.warpqueue.remove(event.getPlayer().getUniqueId());
                }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            if(e.getItem()!=null) {
                if (e.getPlayer().isOp() && e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getItem().getType() == Material.FISHING_ROD) {
                    PrelimitedEvent event = new PrelimitedEvent(e.getPlayer(), e.getClickedBlock().getLocation(), e.getAction() == Action.LEFT_CLICK_BLOCK ? PrelimitedEvent.Type.Left : PrelimitedEvent.Type.Right);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if(event.isCancelled()){
                        return;
                    }
                    event.work();
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler //确保uuid正常
    public void onJoin(PlayerJoinEvent e){
        CustomWarps.checkInValid(new PluginData(CustomWarps.pis.playerdata,e.getPlayer().getName()+".yml")); //low efficiency
    }
}
