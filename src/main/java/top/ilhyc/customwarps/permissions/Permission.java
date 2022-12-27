package top.ilhyc.customwarps.permissions;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Permission<T> {
    private T t;
    private String root;

    public Permission(String s){
        this.root = s;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public List<T> get(Player p){
        return p.getEffectivePermissions().stream().filter(a->a.getPermission().contains(this.root)&&a.getValue()).map(a->translate(a.getPermission())).collect(Collectors.toList());
    }

    public abstract T translate(String s);

    public String getKey(String s){
        String[] roots = s.split("\\.");
        return roots[roots.length-1];
    }
}
