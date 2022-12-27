package top.ilhyc.customwarps.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import top.ilhyc.customwarps.CustomWarps;
import top.ilhyc.customwarps.PluginData;
import top.ilhyc.customwarps.PluginInitializer;
import top.ilhyc.customwarps.permissions.PermissionManager;

import java.io.File;

public class MiscCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.isOp()){
            if(s.equalsIgnoreCase("csync")){
                sync(commandSender);
            }
        }
        return false;
    }

    public static void sync(CommandSender sender){
        if(CustomWarps.playerdata.listFiles()!=null) {
            for (File f : CustomWarps.playerdata.listFiles()) {
                PluginData pd = new PluginData(f);
                int var1 = pd.getInt("number");
                if(var1==0){
                    continue;
                }
                try {
                    PermissionManager.setPermissionObject("customwarps.limit", Bukkit.getPlayer(PluginData.deleteYml(f)),var1 + "");
                    pd.set("number",null);
                    pd.save();
                }catch (Exception ex){
                    sender.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.failed-sync")));
                }
            }
        }
        sender.sendMessage(CustomWarps.Auto(PluginData.getConfig().getString("language.success-sync")));
    }
}
