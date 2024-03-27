package fr.zirisgambling;

import fr.zirisgambling.config.FileConfig;
import fr.zirisgambling.utils.GamblingUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Gambling {


    private final GameModes mode;
    private final Player host;
    private final int moneyBet;
    private final int gameId;
    private boolean started;
    private Player joined;
    private final Main instance;
    private final ArrayList<UUID> fighters = new ArrayList<>();
    private Player winner;
    private final HashMap<UUID,ItemStack[]> savedInventory;
    private final HashMap<UUID,ItemStack[]> savedArmor;
    private final HashMap<UUID,Location> savedLocation;

    public Gambling(final Main instance,final GameModes mode,final Player host,final int moneyBet,int gameId){
        this.mode = mode;
        this.host = host;
        this.moneyBet = moneyBet;
        this.gameId = gameId;
        this.savedInventory = new HashMap<>();
        this.savedArmor = new HashMap<>();
        this.savedLocation = new HashMap<>();
        this.fighters.add(this.host.getUniqueId());

        this.winner = null;

        this.started = false;

        this.instance = instance;
    }


    public void startGambling(Player joined){
        this.setStarted(true);

        this.joined = joined;

        getJoined().sendMessage(FileConfig.ENEMY_MESSAGE.get()
                .replace("{player}",getHost().getDisplayName()));

        getHost().sendMessage(FileConfig.ENEMY_MESSAGE.get()
                .replace("{player}",getJoined().getDisplayName()));

        this.saveInventories();
        this.saveLocation();

        Location joinedLocation = instance.getConfigManager().getSpawnLocation("playerSpawn");
        Location hostLocation = instance.getConfigManager().getSpawnLocation("playerFrontSpawn");

        joinedLocation.setDirection(joinedLocation.getDirection().multiply(-1));

        joined.teleport(joinedLocation);
        getHost().teleport(hostLocation);


        getFighters().add(joined.getUniqueId());
        getFighters().add(host.getUniqueId());

        this.giveStuff(joined);
        this.giveStuff(this.getHost());
        this.hidePlayers();
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }


    private void saveInventories(){
        savedInventory.put(this.getHost().getUniqueId(),this.getHost().getInventory().getContents());
        savedInventory.put(this.getJoined().getUniqueId(),this.getJoined().getInventory().getContents());

        savedArmor.put(this.getHost().getUniqueId(),this.getHost().getInventory().getArmorContents());
        savedArmor.put(this.getJoined().getUniqueId(),this.getJoined().getInventory().getArmorContents());

        this.getHost().getInventory().clear();
        this.getJoined().getInventory().clear();

        this.getHost().getInventory().setArmorContents(null);
        this.getJoined().getInventory().setArmorContents(null);
    }
    public void giveInventories(){
        this.getHost().getInventory().clear();
        this.getJoined().getInventory().clear();

        this.getHost().getInventory().setContents(savedInventory.get(getHost().getUniqueId()));
        this.getJoined().getInventory().setContents(savedInventory.get(getJoined().getUniqueId()));

        this.getHost().getInventory().setArmorContents(savedArmor.get(getHost().getUniqueId()));
        this.getJoined().getInventory().setArmorContents(savedArmor.get(getJoined().getUniqueId()));

        this.getHost().setHealth(this.getHost().getMaxHealth());
        this.getJoined().setHealth(this.getJoined().getMaxHealth());


    }

    private void saveLocation(){
        savedLocation.put(getHost().getUniqueId(),getHost().getLocation());
        savedLocation.put(getJoined().getUniqueId(),getJoined().getLocation());
    }

    public void teleportBack(){
        getHost().teleport(savedLocation.get(getHost().getUniqueId()));
        getJoined().teleport(savedLocation.get(getJoined().getUniqueId()));
    }

    public GameModes getMode() {
        return mode;
    }

    public Player getHost(){
        return host;
    }

    public int getMoneyBet(){
        return moneyBet;
    }

    public int getGameId() {
        return gameId;
    }

    public void setWinner(Player player){
        this.winner = player;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getJoined(){
        return joined;
    }

    public ArrayList<UUID> getFighters() {
        return fighters;
    }


    private void hidePlayers(){
        instance.getGamblingManager()
                .getStartedGames()
                .forEach(g->{
                    if(g.getGameId() != this.getGameId()) {
                        g.getFighters()
                                .forEach(fighter -> {
                                    this.getHost().hidePlayer(Bukkit.getPlayer(fighter));
                                    this.getJoined().hidePlayer(Bukkit.getPlayer(fighter));

                                    Bukkit.getPlayer(fighter).hidePlayer(this.getHost());
                                    Bukkit.getPlayer(fighter).hidePlayer(this.getJoined());

                                });
                    }
                });
    }

    public void showPlayers(){
        instance.getGamblingManager()
                .getStartedGames()
                .forEach(g->{
                    if(g.getGameId() != this.getGameId()) {
                        g.getFighters()
                                .forEach(other -> {
                                    this.getHost().showPlayer(Bukkit.getPlayer(other));
                                    this.getJoined().showPlayer(Bukkit.getPlayer(other));

                                    Bukkit.getPlayer(other).showPlayer(this.getHost());
                                    Bukkit.getPlayer(other).showPlayer(this.getJoined());

                                });
                    }
                });

    }

    public void giveStuff(Player p){
        List<ItemStack> armorContents = new ArrayList<>();

        if(mode.equals(GameModes.PVP)) {
            instance.getGamblingManager().getPvPKit()
                    .forEach(it->{
                        if(GamblingUtils.isArmor(it)){
                            armorContents.add(it);
                        }else{
                            p.getInventory().addItem(it);
                        }

                    });

            Collections.reverse(armorContents);
            p.getInventory().setArmorContents(armorContents.toArray(new ItemStack[0]));

        }else if (mode.equals(GameModes.BOW)){
            instance.getGamblingManager().getBowKit()
                    .forEach(it->{
                        if(GamblingUtils.isArmor(it)){
                            armorContents.add(it);
                        }else{
                            p.getInventory().addItem(it);
                        }

                    });

            Collections.reverse(armorContents);
            p.getInventory().setArmorContents(armorContents.toArray(new ItemStack[0]));

        }else if (mode.equals(GameModes.ROD)){
            instance.getGamblingManager().getRodKit()
                    .forEach(it->{
                        if(GamblingUtils.isArmor(it)){
                            armorContents.add(it);
                        }else{
                            p.getInventory().addItem(it);
                        }

                    });

            Collections.reverse(armorContents);
            p.getInventory().setArmorContents(armorContents.toArray(new ItemStack[0]));
        }
    }

    public UUID getRemainingOne(Player player){
        return this.getFighters().stream()
                .filter(p->!(p.equals(player.getUniqueId())))
                .findFirst()
                .get();

    }
}
