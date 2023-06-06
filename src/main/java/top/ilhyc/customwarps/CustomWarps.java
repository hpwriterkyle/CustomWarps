package top.ilhyc.customwarps;

import com.onarandombox.MultiverseCore.MultiverseCore;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import top.ilhyc.customwarps.api.CustomWarper;
import top.ilhyc.customwarps.commands.LimitCommand;
import top.ilhyc.customwarps.commands.MainCommand;
import top.ilhyc.customwarps.commands.MiscCommand;
import top.ilhyc.customwarps.commands.SetCommand;
import top.ilhyc.customwarps.gui.MainGui;
import top.ilhyc.customwarps.gui.WarpGui;

import java.io.File;
import java.util.*;

public final class CustomWarps extends JavaPlugin {
    public static File playerdata;
    public static PluginInitializer pis;
    public static File data;
    public static Map<String, List<WarpPoint>> map = new HashMap<>();
    public static Map<String,Long> cooldown = new HashMap<>();
    public static Map<UUID,WarpPoint> warpqueue = new HashMap<>();
    public static Map<String, LimitField> limitfields = new HashMap<>();
    public static File save;
    private static Economy eco = null;
    public static Map<Player, Location[]> limitedmap = new HashMap<>();
    public static MultiverseCore core;
    private static CustomWarper customWarper;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults();
        if(!this.getDataFolder().exists()){
            try {
                this.getDataFolder().mkdirs();
                saveDefaultConfig();
            } catch (Exception e) {//
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
        getCommand("cmisc").setExecutor(new MiscCommand());
        save = new File(data,"data.yml");
        if(!setupEconomy()){
            pis.logger("[CustomWarps]应当暂时发现了一个前置的漏洞!");
            pis.logger(">>Vault插件未安装!");
        }else{
            pis.logger("[CustomWarps]应当暂时已经成功安装Vault插件!");
        }
        loadConfig();
        customWarper = new CustomWarper();
        pis.logger("插件已加载了"+map.keySet().size()+"个玩家的传送点组");
        pis.logger("插件已加载了"+limitfields.keySet().size()+"个限制区");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        pis.logger("[CustomWarps]此插件已关闭");
        String s = CustomWarps.Auto(getConfig().getString("language.reload-force"));
        Bukkit.getOnlinePlayers().stream().filter(a->a.getOpenInventory().getTopInventory()!=null).filter(a->a.getOpenInventory().getTopInventory().getHolder()!=null)
                .filter(a->a.getOpenInventory().getTopInventory().getHolder() instanceof WarpGui)
                .forEach(a->{
                    a.sendMessage(s);
                    a.closeInventory();
                });
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
     //   MiscCommand.sync(Bukkit.getConsoleSender());
        if(playerdata.listFiles()!=null) {
            for (File f : playerdata.listFiles()) {
                if (f != null) {
                    int n = 0;
                    PluginData pd = new PluginData(f);
                    List<WarpPoint> lwp = new ArrayList<>();
                    if (pd.getKeys("warps") != null) {
                        for (String s : pd.getKeys("warps")) {
                            WarpPoint wp = new WarpPoint();
                            wp.setLocation(pd.getLocation("warps" + "." + s + ".location"));
                            wp.setName(pd.getString("warps." + s + ".name"));
                            wp.setOrder(n);
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

    public static boolean inCooldown(Long t, String p){
        if(cooldown.get(p)!=null) {
            return t <= cooldown.get(p) + PluginData.getConfig().getInt("default.cooldown") * 1000L;
        }
        return false;
    }

    public static void checkInValid(PluginData pd){
        OfflinePlayer p = Bukkit.getOfflinePlayer(PluginData.deleteYml(pd.f));
        String uuid = "player.uuid";
        UUID id;
        try{
            id = p.getUniqueId();
        }catch (NoSuchMethodError e){
            id = p.getPlayer().getUniqueId();
        }
        if(pd.getString(uuid)==null){
            pd.set(uuid,id.toString());
            pd.save();
            return;
        }
        if(id.toString().equals(pd.getString(uuid))){
            return;
        }
        p = Bukkit.getPlayer(UUID.fromString(pd.getString(uuid)));
        if(!pd.f.renameTo(new File(CustomWarps.pis.playerdata,p.getName()+".yml"))){
            PluginData pds = new PluginData(CustomWarps.pis.playerdata,p.getName()+".yml");
            checkInValid(pds);
        }
        pd.set(uuid,p.getUniqueId().toString());
        pd.save();
    }

    public static CustomWarper getApi(){
        return customWarper;
    }

}
