package top.ilhyc.customwarps.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.ilhyc.customwarps.CustomWarps;
import top.ilhyc.customwarps.PluginData;
import top.ilhyc.customwarps.WarpPoint;

import java.util.ArrayList;
import java.util.List;

public class SetCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            PluginData pd = new PluginData(CustomWarps.playerdata, p.getName() + ".yml");
            if (strings.length > 0) {
                if (!PluginData.getConfig().getString("bannedworld").contains(p.getWorld().getName())&& CustomWarps.limitfields.values().stream().noneMatch(a->a.inLimited(p.getLocation()))) {
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
                    wp.name = CustomWarps.Auto(strings[0]);
                    lwp.add(wp);
                    CustomWarps.map.put(p.getName(), lwp);
                    WarpPoint.storeWarpPoint(wp, p);
                    p.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.add-success")));
                    //            }else{
                    //                  p.sendMessage(customwarps.Auto(PluginData.getConfig().getString("language.cost-set-failed")));
                    //        }
                } else {
                    p.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.world-failed")));
                }
            }
        }
            return false;
        }
}
