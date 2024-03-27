package fr.zirisgambling.manager;

import fr.zirisgambling.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ConfigManager {


    private final Main instance;
    public ConfigManager(final Main instance){
        this.instance = instance;
    }


    public int getPlayerWins(Player player){
        return instance.getConfig().getInt("Stats." + player.getUniqueId()
                + ".win");
    }

    public int getPlayerWins(String player){
        return instance.getConfig().getInt("Stats." + player
                + ".win") == -1 ? 0 :instance.getConfig().getInt("Stats." + player
                + ".win") ;
    }

    public int getPlayerLoses(String player){
        return instance.getConfig().getInt("Stats." + player
                + ".lose")== -1 ? 0 :instance.getConfig().getInt("Stats." + player
                + ".lose") ;
    }

    public int getPlayerGames(String player){
        return instance.getConfig().getInt("Stats." + player
                + ".games")== -1 ? 0 :instance.getConfig().getInt("Stats." + player
                + ".games") ;
    }

    public void addPlayerGame(String player){
        instance.getConfig().set("Stats." + player
                + ".games",this.getPlayerGames(player) + 1);

        instance.saveConfig();

    }

    public void addPlayerWin(String player){
        instance.getConfig().set("Stats." + player
                + ".win",this.getPlayerWins(player) + 1);

        instance.saveConfig();
    }

    public void addPlayerLose(String player){
        instance.getConfig().set("Stats." + player
                + ".lose",this.getPlayerLoses(player) + 1);

        instance.saveConfig();
    }

    public void createStats(Player player){
        if(!this.hasPlayerStats(player.getName())){
            if(instance.getConfig().get("gameCreated") == null) instance.getConfig().set("gameCreated",0);
            instance.getConfig().set("Stats." + player.getName() + ".win",0);
            instance.getConfig().set("Stats." + player.getName() + ".lose",0);
            instance.getConfig().set("Stats." + player.getName() + ".games",0);


            instance.saveConfig();
        }
    }

    public int getCurrentMaxID(){
        return instance.getConfig().getInt("gameCreated");
    }
    public void incrementMaxGame(){
        instance.getConfig().set("gameCreated",this.getCurrentMaxID() + 1);

        instance.saveConfig();
    }

    private boolean hasPlayerStats(String player){
        return instance.getConfig().getConfigurationSection("Stats." + player) != null;
    }

    public Location getSpawnLocation(String key){
        String world = instance.getConfig().getString(key + ".world");
        int x = instance.getConfig().getInt(key +".x");
        int y = instance.getConfig().getInt(key + ".y");
        int z = instance.getConfig().getInt(key + ".z");
        return new Location(instance.getServer().getWorld(world),x,y,z);
    }

    public int getMinBet(){
        return instance.getConfig().getInt("min-bet");
    }
    public int getMaxBet(){
        return instance.getConfig().getInt("max-bet");
    }

    public int getTimeToAnswer() {
        return instance.getConfig().getInt("time-to-answer");
    }
}
