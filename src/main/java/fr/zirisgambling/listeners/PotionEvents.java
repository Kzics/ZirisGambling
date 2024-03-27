package fr.zirisgambling.listeners;

import fr.zirisgambling.Gambling;
import fr.zirisgambling.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEvents implements Listener {

    private final Main instance;
    public PotionEvents(final Main instance){
        this.instance = instance;
    }


    @EventHandler
    public void onHeal(PotionSplashEvent e) {
        Player player = (Player) e.getEntity().getShooter();
        if(!(instance.getGamblingManager().noGames())) {

            Gambling currentGame = instance.getGamblingManager().getGameByUUID(player.getUniqueId());
            if (currentGame != null) {
                e.getAffectedEntities().forEach(entity -> {
                    if (entity instanceof Player) {
                        Player affectedPlayer = (Player) entity;
                        if (!(currentGame.getFighters().contains(affectedPlayer.getUniqueId()))) {
                            for (PotionEffect effect : e.getPotion().getEffects()) {
                                if (effect.getType().equals(PotionEffectType.HEAL)) {
                                    e.setIntensity(affectedPlayer, 0);
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        }
    }
}
