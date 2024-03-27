package fr.zirisgambling.listeners;

import fr.zirisgambling.Main;
import fr.zirisgambling.listeners.customevent.WinEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DeathEvents implements Listener {
    private final Main instance;
    public DeathEvents(final Main instance){
        this.instance = instance;

    }


    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player) {
            if(e.getDamager() instanceof Player || e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                if(instance.getGamblingManager().areFighting(e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) ?
                                (((Arrow)e.getDamager()).getShooter()) : (Player) e.getDamager()
                        ,(Player) e.getEntity())) {

                    Player player = (Player) e.getEntity();

                    if (player.getHealth() - e.getFinalDamage() <= 0) {
                        e.setCancelled(true);
                        instance.getGamblingManager().getStartedGames().forEach(game -> {

                            if (game.getFighters().contains(player.getUniqueId())) {
                                game.getFighters().remove(player.getUniqueId());

                                game.setWinner(Bukkit.getPlayer(game.getFighters().get(0)));

                                WinEvent ev = new WinEvent(game.getWinner(), player, game);

                                instance.getServer().getPluginManager().callEvent(ev);

                                game.giveInventories();
                                game.teleportBack();
                            }
                        });

                    }
                }
            }
        }
    }
}
