package top.ilhyc;

import org.bukkit.Location;

public class LimitField{
    Location[] limited ={null,null};

    public LimitField(Location lo,Location lt){
        if(lo.getWorld().getName().equalsIgnoreCase(lt.getWorld().getName())){
            limited[0] = new Location(lo.getWorld(), Math.min(lo.getBlockX(), lt.getBlockX()),Math.min(lo.getBlockY(),lt.getBlockY()),Math.min(lo.getBlockZ(),lt.getBlockZ()));
            limited[1] = new Location(lt.getWorld(),Math.max(lo.getBlockX(),lt.getBlockX()),Math.max(lo.getBlockY(),lt.getBlockY()),Math.max(lo.getBlockZ(),lt.getBlockZ()));
        }
    }

    public Location getOneSide(){
        return limited[0];
    }

    public Location getTwoSide(){
        return limited[1];
    }

    public boolean inLimited(Location l){
        if(l.getBlockX()>=limited[0].getBlockX()&&l.getBlockY()>=limited[0].getBlockY()&&l.getBlockZ()>=limited[0].getBlockZ()){
            return l.getBlockX()<=limited[1].getBlockX()&&l.getBlockY()<=limited[1].getBlockZ()&&l.getBlockZ()<=limited[1].getBlockZ();
        }
        return false;
    }

    public int getV(){
        return (getTwoSide().getBlockX()-getOneSide().getBlockX()+1)*(getTwoSide().getBlockY()-getOneSide().getBlockY()+1)*(getTwoSide().getBlockZ()-getOneSide().getBlockZ()+1);
    }
}
