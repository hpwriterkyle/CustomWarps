package top.ilhyc.customwarps.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.ilhyc.customwarps.CustomWarps;
import top.ilhyc.customwarps.LimitField;
import top.ilhyc.customwarps.PluginData;

public class LimitCommand implements CommandExecutor {
    //
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                if (commandSender.isOp() && commandSender instanceof Player) {
                    if (strings.length > 1) {
                        Player p = (Player) commandSender;
                        PluginData pd = new PluginData(CustomWarps.data,"data.yml");
                        if (strings[0].equalsIgnoreCase("add")) {
                            if (CustomWarps.limitedmap.containsKey(p) && CustomWarps.limitedmap.get(p)[0] != null && CustomWarps.limitedmap.get(p)[1] != null) {
                                LimitField lf = new LimitField(CustomWarps.limitedmap.get(p)[0], CustomWarps.limitedmap.get(p)[1]);
                                if(!CustomWarps.limitfields.containsKey(strings[1])) {
                                    CustomWarps.limitfields.put(strings[1], lf);
                                    pd.setLocation("limitedfields."+strings[1]+".first",lf.getOneSide());
                                    pd.setLocation("limitedfields."+strings[1]+".second",lf.getOneSide());
                                    pd.save();
                                    p.sendMessage(CustomWarps.Auto("&a你创建了一个体积为&6"+lf.getV()+"&a的名为"+strings[1]+"传送保护区"));
                                }else{
                                    p.sendMessage(CustomWarps.Auto("&c已存在此区域!"));
                                }
                            }
                        } else if (strings[0].equalsIgnoreCase("remove")) {
                            if(CustomWarps.limitfields.containsKey(strings[1])){
                                CustomWarps.limitfields.remove(strings[1]);
                                pd.set("limitedfields."+strings[1],null);
                                pd.save();
                                p.sendMessage(CustomWarps.Auto("&c你删除了一个名为"+strings[1]+"传送保护区"));
                            }else{
                                p.sendMessage(CustomWarps.Auto("&c不存在此区域!"));
                            }
                            Location l;
                        }else if(strings[0].equalsIgnoreCase("list")){
                            if(!CustomWarps.limitfields.keySet().isEmpty()) {
                                CustomWarps.limitfields.keySet().forEach(a -> p.sendMessage(CustomWarps.Auto("&f- ") + a));
                            }else{
                                p.sendMessage(CustomWarps.Auto("&7不存在任何限制区域"));
                            }
                        }
                    }
                    }else {
                    commandSender.sendMessage(CustomWarps.Auto("&c非游戏中的管理员不可使用此指令!"));
                }
        return false;
    }
}
