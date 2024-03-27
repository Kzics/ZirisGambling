package fr.zirisgambling.entity;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.List;

public class ArmorStandEntity {

    private final List<ArmorStand> armorStands;
    public ArmorStandEntity(List<ArmorStand> armorStands){
        this.armorStands = armorStands;
    }



    public void removeEntity(){
        armorStands
                .forEach(Entity::remove);
    }
}
