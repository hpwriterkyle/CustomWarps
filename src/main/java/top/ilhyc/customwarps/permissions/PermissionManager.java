package top.ilhyc.customwarps.permissions;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import top.ilhyc.customwarps.CustomWarps;

import java.util.List;
import java.util.function.Function;

public class PermissionManager {
    private Player player;
    private PermissionAttachment permissionAttachment;

    public PermissionManager(Player p){
        this.player = p;
        this.permissionAttachment = p.addAttachment(CustomWarps.pis.plugin);
    }

    public <object> object getPermissionItem(String root,Function<String,object> translator){
        Permission<object> permission = new Permission<object>(root) {
            @Override
            public object translate(String s) {return translator.apply(getKey(s));}
        };
        List<object> list = permission.get(this.player);
        return list.isEmpty()?null:list.get(0);
    }

    public void removePermissionItem(String root,boolean exact){
        if(exact){
            this.permissionAttachment.unsetPermission(root);
            return;
        }
        for(PermissionAttachmentInfo pa:this.player.getEffectivePermissions()) {
            if(pa.getPermission().contains(root)){
                this.permissionAttachment.unsetPermission(pa.getPermission());
            }
        }
    }

    public String setPermissionItem(String root,String value){
        removePermissionItem(root,false);
        addPermissionItem(root+"."+value);
        return root+"."+value;
    }

    public PermissionAttachment addPermissionItem(String root){
        this.permissionAttachment.setPermission(root,true);
        return this.permissionAttachment;
    }

    public static <object> object getPermissionObject(String root, Player p, Function<String,object> supplier){
        PermissionManager pm = new PermissionManager(p);
        return pm.getPermissionItem(root,supplier);
    }

    public static String setPermissionObject(String root,Player p,String value){
        PermissionManager pm = new PermissionManager(p);
        return pm.setPermissionItem(root,value);
    }

    public static void removePermissionObject(String root,Player p,boolean exact){
        PermissionManager pm = new PermissionManager(p);
        pm.removePermissionItem(root,exact);
    }
}
