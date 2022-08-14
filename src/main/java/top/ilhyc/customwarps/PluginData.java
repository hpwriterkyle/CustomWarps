package top.ilhyc.customwarps;

import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PluginData {
    //此类灵感来源于Forge插件 并对其源码进行部分修改操作而成(因为关乎甚小 因作者要求勿扰且直接开源 未曾打扰原作者以获取授权)
    //仅以此处表示敬意 若有要求 请联系本插件作者以删除
    public File f;
    public FileConfiguration fc;

    public PluginData(File f){
        this.f = f;
        createit(this.f);
        this.fc = YamlConfiguration.loadConfiguration(f);
        save();
    }

    public PluginData(){
        this.fc = getConfig();
        save();
    }

    public PluginData(File f, String name){
        this.f = new File(f,name);
        createit(f);
        this.fc = YamlConfiguration.loadConfiguration(this.f);
        save();
    }

    public PluginData(String s){
        this.f = new File(customwarps.data,s);
        createit(f);
        this.fc = YamlConfiguration.loadConfiguration(this.f);
        save();
    }


    public void save() {
        try {
            this.fc.save(this.f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collection<String> getKeys(String path) {
        if (path == null || path.equals("")) {
            return fc.getKeys(false);
        } else {
            return fc.getConfigurationSection(path)==null?null:fc.getConfigurationSection(path).getKeys(false);
        }
    }

    public void set(String path, Object object) {
        fc.set(path, object);
    }

    public Object get(String path) {
        return fc.get(path);
    }

    public String getString(String path) {
        return customwarps.Auto(this.fc.getString(path));
    }

    public int getInt(String path) {
        return fc.getInt(path);
    }

    public double getDouble(String path){
        return fc.getDouble(path);
    }

    public List<?> getList(String path){
        return fc.getList(path);
    }

    public List<String> getStringList(String path){
        return fc.getStringList(path);
    }

    public static FileConfiguration getConfig(){
        customwarps plugin = top.ilhyc.customwarps.customwarps.getPlugin(top.ilhyc.customwarps.customwarps.class);
        return plugin.getConfig();
    }

    public static List<String> lore(String...list){
        List<String> lores = new ArrayList<>();
        for(String s:list) {
            lores.add(customwarps.Auto(s));
        }
        return lores;
    }

    public static List<String> lore(List<String> list){
        List<String> lores = new ArrayList<>();
        for(String s:list){
            if(s!=null) {
                lores.add(customwarps.Auto(s));
            }
        }
        return lores;
    }

    public File getF() {
        return f;
    }

    public FileConfiguration getFc() {
        return fc;
    }

    public void create(){
        if(!this.f.exists()){
            try {
                this.f.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void createit(File f){
        if(!f.exists()){
            try {
                f.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public File[] getDatas(){
        return this.f.listFiles();
    }

    public List<FileConfiguration> getinDatas(){
        ArrayList<FileConfiguration> afc = new ArrayList<>();
        File[] fs = getDatas();
        for(int i=0;i<fs.length;i++){
            afc.add(YamlConfiguration.loadConfiguration(fs[i]));
        }
        return afc;
    }

    public String deleteYml() {
        if(this.f.getName().contains(".yml")) {
            String newname = "";
            for (int i = 0; i < f.getName().length(); i++) {
                if (i < this.f.getName().length() - 4) {
                    newname = newname + this.f.getName().charAt(i);
                }
            }
            return newname;
        }
        return f.getName();
    }

    public static String deleteYml(File f) {
        if (f.getName().contains(".yml")) {
            String newname = "";
            for (int i = 0; i < f.getName().length(); i++) {
                if (i < f.getName().length() - 4) {
                    newname = newname + f.getName().charAt(i);
                }
            }
            return newname;
        }
        return f.getName();
    }

    public boolean getBoolean(String s){
        return fc.getBoolean(s);
    }

    public HashMap<String,?> getMap(String root,String key) {
        HashMap<String, Object> map = new HashMap<>();
        if (fc.get(root + "." + key) != null) {
            ConfigurationSection cs = fc.getConfigurationSection(root + "." + key);
            for (String s : cs.getKeys(false)) {
                if (s != null) {
                    map.put(s, fc.get(root + "." + key + "." + s));
                }
            }
        }
        return map;
    }

    public void setLocation(String path, Location p){
        fc.set(path+".world",p.getWorld().getName());
        fc.set(path+".x",p.getBlockX());
        fc.set(path+".y",p.getBlockY());
        fc.set(path+".z",p.getBlockZ());
    }

    public Location getLocation(String path){
        World w = Bukkit.getWorld(fc.getString(path+".world"));
        if(w==null){
            MultiverseWorld ww = customwarps.core.getMVWorldManager().getMVWorld(path);
            w = ww.getCBWorld();
        }
        if(w==null){
            customwarps.pis.logger("&c"+fc.getString(path+".world")+"&c世界不存在于所有被服务端核心或多世界加载的世界列表中");
            customwarps.pis.logger("&e现已自动将此次世界传送点世界设置为world");
            w = Bukkit.getWorld("world");
        }
        return new Location(/*Bukkit.getWorld(fc.getString(path+".world")!=null?fc.getString(path+".world"):"world")*/w,fc.getDouble(path+".x"),fc.getDouble(path+".y"),fc.getDouble(path+".z"));
    }
}
