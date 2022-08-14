package top.ilhyc.customwarps;

import org.bukkit.Bukkit;

import java.io.File;

public class PluginInitializer{
    public customwarps plugin;
    public File playerdata;
    public PluginInitializer(customwarps plugin){
        this.plugin = plugin;
    }

    public void logger(String s){
        Bukkit.getLogger().info(customwarps.Auto(s));
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
