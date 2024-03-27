package fr.zirisgambling.manager;

import fr.zirisgambling.Main;

import java.util.*;
import java.util.stream.Stream;

public class LeaderboardManager {
    private final Main instance;
    private final HashMap<String,Integer> winMap;

    public LeaderboardManager(final Main instance){
        this.instance = instance;
        this.winMap = new HashMap<>();
    }

    private void updateValues(){
        if(instance.getConfig().getConfigurationSection("Stats.") != null) {
            instance.getConfig().getConfigurationSection("Stats.")
                    .getKeys(false)
                    .forEach(name -> {
                        int playerWin = instance.getConfigManager().getPlayerWins(name);
                        winMap.put(name, playerWin);
                    });
        }
    }

    public Stream<Map.Entry<String,Integer>> getLeaderboard(){
        this.updateValues();

        return this.winMap.entrySet().stream()
                    .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()));


    }



}
