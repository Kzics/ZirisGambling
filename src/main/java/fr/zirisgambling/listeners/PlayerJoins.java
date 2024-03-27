package fr.zirisgambling.listeners;


import fr.zirisgambling.Gambling;
import fr.zirisgambling.Main;
import fr.zirisgambling.inventories.WaitingGamblingMenu;
import fr.zirisgambling.listeners.customevent.WinEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
public class PlayerJoins implements Listener  {

    private final Main instance;
    public PlayerJoins(final Main instance){
        this.instance = instance;
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        instance.getPreFightManager().removeToChatState(e.getPlayer());
        instance.getPreFightManager().removePlayerResp(e.getPlayer());

        if(instance.getGamblingManager().hasAlreadyGambling(e.getPlayer())){
            instance.getGamblingManager()
                    .removeGamblingGame(instance.getGamblingManager()
                            .getGameByHost(e.getPlayer().getUniqueId())
                            .getGameId());

            instance.getServer().getOnlinePlayers()
                    .forEach(player -> {
                        if(player.getOpenInventory().getTopInventory().getTitle().equals(WaitingGamblingMenu.getTitle())){
                            new WaitingGamblingMenu(instance).openInv(player);
                        }
                    });
        }

        if(this.hasCombatLog(e.getPlayer())){
            Gambling gambling = instance.getGamblingManager().getGameByUUID(e.getPlayer().getUniqueId());
            if(gambling != null){
                gambling.setWinner(Bukkit.getPlayer(gambling.getRemainingOne(e.getPlayer())));

                WinEvent winEvent = new WinEvent(gambling.getWinner(),e.getPlayer(),gambling);
                gambling.giveInventories();
                gambling.teleportBack();
                instance.getServer().getPluginManager().callEvent(winEvent);

            }

        }
    }

    public boolean hasCombatLog(Player player){
    return instance.getGamblingManager()
                .getPlayersFighting()
                .stream()
                .anyMatch(fighter->fighter.equals(player.getUniqueId()));
    }
}


