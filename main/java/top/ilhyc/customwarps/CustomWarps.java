package top.ilhyc.customwarps;

import com.onarandombox.MultiverseCore.MultiverseCore;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import top.ilhyc.customwarps.commands.LimitCommand;
import top.ilhyc.customwarps.commands.MainCommand;
import top.ilhyc.customwarps.commands.SetCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class CustomWarps extends JavaPlugin {
    public static File playerdata;
    public static PluginInitializer pis;
    public static File data;
    public static HashMap<String, List<WarpPoint>> map = new HashMap<>();
    public static HashMap<String,Long> cooldown = new HashMap<>();
    public static HashMap<UUID,WarpPoint> warpqueue = new HashMap<>();//
    public static HashMap<String, LimitField> limitfields = new HashMap<>();
    public static File save;
    private static Economy eco = null;
    public static HashMap<Player, Location[]> limitedmap = new HashMap<>();
    public static MultiverseCore core;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults();
        if(!this.getDataFolder().exists()){
            try {
                this.getDataFolder().mkdirs();
                saveDefaultConfig();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        data = this.getDataFolder();
        PluginInitializer pi = new PluginInitializer(this);
        pis = pi;
        pis.logger("[CustomWarps]此插件已启用");
        playerdata = pi.registerPlayerData();
        core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        getServer().getPluginManager().registerEvents(new MainListener(),this);
        getCommand("customwarps").setExecutor(new MainCommand());
        getCommand("cset").setExecutor(new SetCommand());
        getCommand("climit").setExecutor(new LimitCommand());
        save = new File(data,"data.yml");
        if(!setupEconomy()){
            pis.logger("[CustomWarps]应当暂时发现了一个前置的漏洞!");
            pis.logger(">>Vault插件未安装!");
        }else{
            pis.logger("[CustomWarps]应当暂时已经成功安装Vault插件!");
        }
        loadConfig();
        pis.logger("插件已加载了"+map.keySet().size()+"个传送点");
        pis.logger("插件已加载了"+limitfields.keySet().size()+"个限制区");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        pis.logger("[CustomWarps]此插件已关闭");
//        if(customwarps.playerdata.listFiles()!=null) {
//            for (Player op: Bukkit.getOnlinePlayers()) {
//                WarpPoint.restoreWarpPoint(customwarps.map.get(op.getName()),op);
//            }
//    }
    }

    public static String Auto(String s){
        return ChatColor.translateAlternateColorCodes('&',s);
    }

    public static void loadConfig(){
        map.clear();
        if(playerdata.listFiles()!=null) {
            for (File f : playerdata.listFiles()) {
                if (f != null) {
                    int n = 0;
                    PluginData pd = new PluginData(f);
                    List<WarpPoint> lwp = new ArrayList<>();
                    if (pd.getKeys("warps") != null) {
                        for (String s : pd.getKeys("warps")) {
                            WarpPoint wp = new WarpPoint();
                            wp.location = pd.getLocation("warps" + "." + s + ".location");
                            wp.name = pd.getString("warps." + s + ".name");
                            wp.ser = n;
                            lwp.add(wp);
                            n++;
                        }
                    }
                    map.put(PluginData.deleteYml(f), lwp);
                }
            }
        }
        if(!save.exists()){
            try {
                save.createNewFile();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            PluginData pd = new PluginData(save);
            if(pd.getKeys("limitedfields")!=null){
                for(String s:pd.getKeys("limitedfields")){
                    LimitField l = new LimitField(pd.getLocation("limitedfields."+s+".first"),pd.getLocation("limitedfields."+s+".second"));
                    limitfields.put(s,l);
                }
            }
        }
    }

    private boolean setupEconomy() {//经济api 配置
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                return false;
            } else {
                eco = rsp.getProvider();
                return eco != null;
            }
        }
    }

    public static Economy getEco() {
        return eco;
    }

    public static boolean incooldown(Long t,String p){
        if(cooldown.get(p)!=null) {
            return t <= cooldown.get(p) + PluginData.getConfig().getInt("default.cooldown") * 1000L;
        }
        return false;
    }
}
