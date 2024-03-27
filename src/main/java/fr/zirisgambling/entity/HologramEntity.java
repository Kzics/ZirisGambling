package fr.zirisgambling.entity;

import fr.zirisgambling.config.FileConfig;
import fr.zirisgambling.gambinterface.IHologram;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;

import java.util.*;

public class HologramEntity implements IHologram {

    private List<Map.Entry<String, Integer>> lines;
    private List<ArmorStand> armorStands;
    public HologramEntity(List<Map.Entry<String,Integer>> line){
        this.lines = new ArrayList<>();
        this.armorStands = new ArrayList<>();

        lines.addAll(line);
    }

    @Override
    public boolean spawnHologram(Location loc) {
        CraftWorld world = (CraftWorld) loc.getWorld();
        Location footLoc = new Location(loc.getWorld(),loc.getX(),loc.getY()-2,loc.getZ());
        ArmorStand footMessage = world.spawn(footLoc,ArmorStand.class);

        footMessage.setCustomNameVisible(true);
        footMessage.setCustomName(FileConfig.FOOT_MESSAGE.get());
        footMessage.setVisible(true);
        footMessage.setGravity(false);
        footMessage.setVisible(false);

        armorStands.add(footMessage);
        //TODO
        // faire un objet qui creer tout seul des lignes sans faire tout ça
        Location tempLoc = new Location(loc.getWorld(),loc.getX(),loc.getY(),loc.getZ());

        lines.forEach(line->{
            tempLoc.setY(tempLoc.getY()-0.25);
            ArmorStand stand = world.spawn(tempLoc,ArmorStand.class);

            stand.setCustomNameVisible(true);
            stand.setCustomName(FileConfig.LINE_FORMAT.get()
                    .replace("{name}",line.getKey())
                    .replace("{wins}",String.valueOf(line.getValue())));
            stand.setVisible(false);

            stand.setGravity(false);

            armorStands.add(stand);
        });

        return true;
    }

    public ArmorStandEntity getEntity() {
        return new ArmorStandEntity(this.armorStands);
    }

    @Override
    public void addToWorld(Location loc, ArmorStand stand) {

    }
}
