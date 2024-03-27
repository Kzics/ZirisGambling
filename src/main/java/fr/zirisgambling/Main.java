package fr.zirisgambling;

import fr.zirisgambling.commands.GamblingCommand;
import fr.zirisgambling.config.CustomFile;
import fr.zirisgambling.entity.HologramEntity;
import fr.zirisgambling.listeners.*;
import fr.zirisgambling.manager.*;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class Main extends JavaPlugin {

    private static Economy econ;
    private ConfigManager configManager;
    private PreFightManager preFightManager;
    private GamblingManager gamblingManager;
    private CustomFile langFile;
    private CustomFile hologramFile;
    private static Main instance;

    private HologramEntity currentHologram;
    @Override
    public void onEnable() {
        instance = this;
        langFile = new CustomFile(this,"lang");
        hologramFile = new CustomFile(this,"hologram");

        setupEconomy();

        langFile.createFile();
        hologramFile.createEmptyFile();
        this.createConfigFile();

        configManager = new ConfigManager(this);
        preFightManager = new PreFightManager(this);
        gamblingManager = new GamblingManager(this);

        getCommand("gambling").setExecutor(new GamblingCommand(this));

        HologramEntity hologramEntity = new HologramEntity(new LeaderboardManager(instance)
                .getLeaderboard()
                .collect(Collectors.toList()));

        this.registerEvents();
        if(instance.getHologramFile().getFileConfig().get("location") != null)
                hologramEntity.spawnHologram((Location) instance.getHologramFile().getFileConfig().get("location"));

        this.setCurrentHologram(hologramEntity);
    }

    @Override
    public void onDisable() {
        if(this.getCurrentHologram() != null) this.getCurrentHologram().getEntity().removeEntity();
    }

    private void registerEvents(){
        PluginManager pm = this.getServer().getPluginManager();

        pm.registerEvents(new InventoryEvents(this),this);
        pm.registerEvents(new ChatEvents(this),this);
        pm.registerEvents(new PlayerJoins(this),this);
        pm.registerEvents(new DeathEvents(this),this);
        pm.registerEvents(new GamblingEvents(this),this);
        pm.registerEvents(new PotionEvents(this),this);
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();
    }

    private void createConfigFile(){
        File config = new File(this.getDataFolder(),"config.yml");
        if(config.exists()) return;

        try {
            FileUtils.copyInputStreamToFile(instance.getResource("config.yml"),config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PreFightManager getPreFightManager() {
        return preFightManager;
    }

    public GamblingManager getGamblingManager() {
        return gamblingManager;
    }

    public static Economy getEcon(){
        return econ;
    }

    public static Main getInstance(){
        return instance;
    }

    public CustomFile getLangFile() {
        return langFile;
    }
    public CustomFile getHologramFile(){
        return hologramFile;
    }

    public HologramEntity getCurrentHologram() {
        return currentHologram;
    }
    public void setCurrentHologram(HologramEntity entity){
        this.currentHologram = entity;
    }


}
