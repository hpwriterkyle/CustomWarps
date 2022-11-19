package top.ilhyc.customwarps;

import org.bukkit.Bukkit;

import java.io.File;

public class PluginInitializer{
    public CustomWarps plugin;//
    public File playerdata;
    public PluginInitializer(CustomWarps plugin){
        this.plugin = plugin;
    }

    public void logger(String s){
        Bukkit.getLogger().info(CustomWarps.Auto(s));
    }

    public File registerPlayerData(){
        File playerdata = new File(plugin.getDataFolder(),"playerdata");
        if(!playerdata.exists()){
            try {
                playerdata.mkdirs();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        this.playerdata = playerdata;
        return playerdata;
    }
}
