package fr.zirisgambling;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

public class GameMode {

    private final String name;
    private final YamlConfiguration configuration;
    private final List<ItemStack> armor;
    //private final List<ItemStack> stuff;

    public GameMode(File file){
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.name = configuration.getString("name");
        this.armor = (List<ItemStack>) configuration.getList("armor");
        //this.stuff = configuration.get;
    }


    public String getName() {
        return name;
    }

    public List<ItemStack> getArmor() {
        return armor;
    }

    /*public List<ItemStack> getStuff() {
        return stuff;
    }*/
}
