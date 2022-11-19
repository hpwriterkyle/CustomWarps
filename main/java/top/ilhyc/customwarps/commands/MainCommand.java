package top.ilhyc.customwarps.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.ilhyc.customwarps.gui.MainGui;
import top.ilhyc.customwarps.gui.RemoveGui;
import top.ilhyc.customwarps.PluginData;
import top.ilhyc.customwarps.WarpPoint;
import top.ilhyc.customwarps.CustomWarps;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            PluginData pd = new PluginData(CustomWarps.playerdata, p.getName() + ".yml");
            if (strings.length > 0) {
                if (p.isOp()) {
                    if (strings[0].equalsIgnoreCase("reload")) {
                        if (CustomWarps.playerdata.listFiles() != null) {
                            for (Player op : Bukkit.getOnlinePlayers()) {
                                WarpPoint.restoreWarpPoint(CustomWarps.map.get(op.getName()), op);
                            }
                            p.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.reload-success")));
                        }
                    }
                }
                if (strings[0].equalsIgnoreCase("warps")) {
                    if (!CustomWarps.map.containsKey(p.getName())) {
                        ArrayList<WarpPoint> awp = new ArrayList<>();
                        CustomWarps.map.put(p.getName(), awp);
                    }
                    p.openInventory(MainGui.getGui(p, p.hasPermission("customwarps.playingon.be"),0));
                } else if (strings[0].equalsIgnoreCase("removewarp")) {
                    p.openInventory(RemoveGui.getGui(p, p.hasPermission("customwarps.playingon.be"),0));
                } else if (strings.length > 1) {
                    if (strings[0].equalsIgnoreCase("setwarp")) {
                        if (!PluginData.getConfig().getString("bannedworld").contains(p.getWorld().getName()) && CustomWarps.limitfields.values().stream().noneMatch(a -> a.inLimited(p.getLocation()))) {
                            boolean allowed = CustomWarps.getEco() == null;
                            if (allowed|| CustomWarps.getEco().withdrawPlayer(commandSender.getName(), PluginData.getConfig().getInt("default.cost-set")).transactionSuccess()) {
                                if (CustomWarps.map.get(p.getName()) == null) {
                                    ArrayList<WarpPoint> awp = new ArrayList<>();
                                    CustomWarps.map.put(p.getName(), awp);
                                }
                                if (pd.getInt("number") != 0) {
                                    if (pd.getInt("number") <= CustomWarps.map.get(p.getName()).size()) {
                                        p.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.add-failed")));
                                        return true;
                                    }
                                } else {
                                    if (PluginData.getConfig().getInt("default.warps") <= CustomWarps.map.get(p.getName()).size()) {
                                        p.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.add-failed")));
                                        return true;
                                    }
                                }
                                //                        if (customwarps.getEco().withdrawPlayer(p, PluginData.getConfig().getDouble("cost-set")).transactionSuccess()) {
                                WarpPoint wp = new WarpPoint();
                                List<WarpPoint> lwp = new ArrayList<>();
                                if (CustomWarps.map.get(p.getName()) != null) {
                                    lwp = CustomWarps.map.get(p.getName());
                                }
                                wp.ser = lwp.size();
                                wp.location = p.getLocation();
                                wp.name = strings[1];
                                lwp.add(wp);
                                CustomWarps.map.put(p.getName(), lwp);
                                WarpPoint.storeWarpPoint(wp, p);
                                p.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.add-success")));
                                //            }else{
                                //                  p.sendMessage(customwarps.Auto(PluginData.getConfig().getString("language.cost-set-failed")));
                                //        }
                            } else {
                                p.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.cost-set-failed")));
                            }
                        } else {
                            p.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.world-failed")));
                        }
                    }
                } else if (strings.length > 2) {
                    Player target = Bukkit.getPlayer(strings[1]);
                    if (strings[0].equalsIgnoreCase("setlimit")) {
                        if (p.isOp()) {
                            PluginData pds = new PluginData(CustomWarps.playerdata, replacedplayer(strings[1], p) + ".yml");
                            try {
                                pds.set("number", Integer.parseInt(strings[2]));
                            } catch (NumberFormatException ex) {
                                p.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.error-number")));
                            }
                            p.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.success-set")));
                            WarpPoint.restoreWarpPoint(CustomWarps.map.get(p.getName()), p);
                            pds.save();
                        }
                    } else if (strings[0].equalsIgnoreCase("addlimit")) {
                        PluginData pds = new PluginData(CustomWarps.playerdata, replacedplayer(strings[1], p) + ".yml");
                        if (p.isOp()) {
                            try {
                                int n = PluginData.getConfig().getInt("default.warps");
                                if (pds.getInt("number") != 0) {
                                    n = pds.getInt("number");
                                }
                                pds.set("number", n + Integer.parseInt(strings[2]));
                            } catch (NumberFormatException ex) {
                                p.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.error-number")));
                            }
                            p.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.success-set")));
                            WarpPoint.restoreWarpPoint(CustomWarps.map.get(p.getName()), p);
                            pds.save();
                        }
                    } else if (strings[0].equalsIgnoreCase("removelimit")) {
                        if (p.isOp()) {
                            PluginData pds = new PluginData(CustomWarps.playerdata, replacedplayer(strings[1], p) + ".yml");
                            try {
                                int n = PluginData.getConfig().getInt("default.warps");
                                if (pds.getInt("number") != 0) {
                                    n = pds.getInt("number");
                                }
                                pds.set("number", n - Integer.parseInt(strings[2]));
                            } catch (NumberFormatException ex) {
                                p.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.error-number")));
                            }
                            p.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.success-set")));
                            pds.save();
                            WarpPoint.restoreWarpPoint(CustomWarps.map.get(p.getName()), p);
                        }
                    }
            } else {
                PluginData.getConfig().getStringList("language.help").stream().forEach(a -> p.sendMessage(CustomWarps.Auto(a)));
            }
        } else {
            PluginData.getConfig().getStringList("language.help").stream().forEach(a -> p.sendMessage(CustomWarps.Auto(a)));
        }
        }else{
            if(strings.length>0) {
                if (strings[0].equalsIgnoreCase("reload")) {
                    if (CustomWarps.playerdata.listFiles() != null) {
                        for (Player op : Bukkit.getOnlinePlayers()) {
                            WarpPoint.restoreWarpPoint(CustomWarps.map.get(op.getName()), op);
                        }
                        commandSender.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.reload-success")));
                    }
                }
                if (strings.length > 2) {
                    Player target = Bukkit.getPlayer(strings[1]);
                    if (strings[0].equalsIgnoreCase("setlimit")) {
                        PluginData pds = new PluginData(CustomWarps.playerdata, strings[1] + ".yml");
                        try {
                            pds.set("number", Integer.parseInt(strings[2]));
                        } catch (NumberFormatException ex) {
                            Bukkit.getLogger().info(CustomWarps.Auto(PluginData.getConfig().getString("language.error-number")));
                        }
                        Bukkit.getLogger().info(CustomWarps.Auto(PluginData.getConfig().getString("language.success-set")));
                        pds.save();
                    } else if (strings[0].equalsIgnoreCase("addlimit")) {
                        PluginData pds = new PluginData(CustomWarps.playerdata, strings[1] + ".yml");
                        try {
                            pds.set("number", pds.getInt("number") + Integer.parseInt(strings[2]));
                        } catch (NumberFormatException ex) {
                            Bukkit.getLogger().info(CustomWarps.Auto(PluginData.getConfig().getString("language.error-number")));
                        }
                        Bukkit.getLogger().info(CustomWarps.Auto(PluginData.getConfig().getString("language.success-set")));
                        pds.save();
                    } else if (strings[0].equalsIgnoreCase("removelimit")) {
                        PluginData pds = new PluginData(CustomWarps.playerdata, strings[1] + ".yml");
                        try {
                            pds.set("number", pds.getInt("number") - Integer.parseInt(strings[2]));
                        } catch (NumberFormatException ex) {
                            Bukkit.getLogger().info(CustomWarps.Auto(PluginData.getConfig().getString("language.error-number")));
                        }
                        Bukkit.getLogger().info(CustomWarps.Auto(PluginData.getConfig().getString("language.success-set")));
                        pds.save();
                    }
                }
            }
        }
        return false;
    }

    private static String replacedplayer(String s,Player p){
        return s.contains("%player%")?s.replace("%player%",p.getName()):s;
    }

    private static String replacedplayer(String s,String name){
        return s.contains("%player%")?s.replace("%player%",name):s;
    }
}
