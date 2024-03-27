package fr.zirisgambling.manager;

import fr.zirisgambling.Gambling;
import fr.zirisgambling.Main;
import fr.zirisgambling.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.*;
import java.util.stream.Collectors;

public class GamblingManager {




    private final Main instance;
    private final Deque<Gambling> games;
    private final ArrayList<UUID> playersFight;
    public GamblingManager(final Main instance){
        this.instance = instance;
        this.games = new LinkedList<>();
        this.playersFight = new ArrayList<>();
    }

    public void addGamblingGame(Gambling game){
        games.addFirst(game);
    }

    public void removeGamblingGame(int gameId){
        games.removeIf(game-> game.getGameId() == gameId);
    }

    public Deque<Gambling> getGamblingGames(){
        return games;
    }

    public Deque<Gambling> getWaitingGames(){
        return games.stream()
                .filter(game-> !game.isStarted())
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    public Deque<Gambling> getStartedGames() {
        return games.stream()
                .filter(Gambling::isStarted)
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    public Gambling getGameByID(int id){
        return this.getGamblingGames().stream()
                .filter(game-> game.getGameId() == id)
                .findFirst().get();

    }
    public Gambling getGameByHost(UUID host){
        return this.getGamblingGames().stream()
                .filter(game-> game.getHost().getUniqueId().equals(host))
                .findFirst().get();
    }

    public Gambling getGameByName(String playerName){
        return this.getGamblingGames().stream()
                .filter(game-> game.getHost().getDisplayName().equals(playerName))
                .findFirst().get();
    }

    public Gambling getGameByUUID(UUID playerUUID){
        return this.getGamblingGames()
                .stream()
                .filter(game-> game.getFighters().contains(playerUUID))
                .findFirst()
                .orElse(null);
    }

    public ArrayList<UUID> getPlayersFighting(){
        return playersFight;
    }

    public boolean areFighting(ProjectileSource player1, Player player2){
        if(!(player1 instanceof Player)) return false;
        return this.getPlayersFighting().contains(((Player) player1).getUniqueId()) && this.getPlayersFighting()
                .contains( player2.getUniqueId());

    }

    public boolean hasAlreadyGambling(Player player){
        return this.getGamblingGames()
                .stream()
                .anyMatch(games->games.getHost().equals(player));
    }

    public List<ItemStack> getPvPKit(){
        List<ItemStack> pvpKit = new ArrayList<>();
        pvpKit.add(new ItemUtils(Material.DIAMOND_HELMET,1)
                        .addEnchants(Enchantment.DURABILITY,4)
                .build());
        pvpKit.add(new ItemUtils(Material.DIAMOND_CHESTPLATE,1)
                .addEnchants(Enchantment.DURABILITY,4)
                .build());
        pvpKit.add(new ItemUtils(Material.DIAMOND_LEGGINGS,1)
                .addEnchants(Enchantment.DURABILITY,4)
                .build());
        pvpKit.add(new ItemUtils(Material.DIAMOND_BOOTS,1)
                .addEnchants(Enchantment.DURABILITY,4)
                .build());
        pvpKit.add(new ItemUtils(Material.DIAMOND_SWORD,1)
                .addEnchants(Enchantment.DAMAGE_ALL,5)
                .build());
        pvpKit.add(new ItemUtils(Material.GOLDEN_APPLE,3)
                .build());
        ItemStack stack = new ItemUtils(Material.POTION,1).build();

        Potion pot = new Potion(PotionType.INSTANT_HEAL,2);
        pot.setSplash(true);
        pot.apply(stack);

        for(int i=0;i<10;i++){
            pvpKit.add(stack);
        }

        return pvpKit;
    }

    public List<ItemStack> getBowKit(){
        List<ItemStack> bowKit = new ArrayList<>();
        bowKit.add(new ItemUtils(Material.BOW,1)
                        .addEnchants(Enchantment.ARROW_INFINITE,1)
                .build());

        bowKit.add(new ItemUtils(Material.ARROW,1)
                .build());

        return bowKit;
    }

    public List<ItemStack> getRodKit(){
        List<ItemStack> rodKit = new ArrayList<>(this.getPvPKit());

        rodKit.set(5,new ItemUtils(Material.FISHING_ROD,1).build());

        return rodKit;
    }

    public boolean noGames(){
        return this.getPlayersFighting().isEmpty();
    }



}
