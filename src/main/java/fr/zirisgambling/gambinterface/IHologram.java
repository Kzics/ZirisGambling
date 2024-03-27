package fr.zirisgambling.gambinterface;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

public interface IHologram {



    boolean spawnHologram(Location loc);

    void addToWorld(Location loc, ArmorStand stand);
}
