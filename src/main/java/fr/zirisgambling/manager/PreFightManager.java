package fr.zirisgambling.manager;

import fr.zirisgambling.GameModes;
import fr.zirisgambling.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Predicate;

public class PreFightManager {


    private final Main instance;
    private final ArrayList<UUID> playersChat;
    private final ArrayList<UUID> playerCreating;
    private final HashMap<UUID,String> playerResp;
    private final HashMap<UUID, GameModes> playerChoseMode;

    public PreFightManager(final Main instance){
        this.instance = instance;
        this.playersChat = new ArrayList<>();
        this.playerCreating = new ArrayList<>();
        this.playerResp = new HashMap<>();
        this.playerChoseMode = new HashMap<>();
    }

    public void addToChatState(Player player){
        this.getPlayersChat().add(player.getUniqueId());
    }

    public void removeToChatState(Player player){
        Predicate<UUID> containPlayer = (p)-> p.equals(player.getUniqueId());
        this.getPlayersChat().removeIf(containPlayer);

    }

    public void removePlayerResp(Player player){
        this.getPlayerResp().remove(player.getUniqueId());
    }

    public ArrayList<UUID> getPlayersChat() {
        return playersChat;
    }

    public HashMap<UUID, String> getPlayerResp() {
        return playerResp;
    }

    public HashMap<UUID, GameModes> getPlayerChoseMode() {
        return playerChoseMode;
    }

    public ArrayList<UUID> getPlayerCreating() {
        return playerCreating;
    }
}
