package fr.zirisgambling.listeners;

import fr.zirisgambling.Main;
import fr.zirisgambling.config.FileConfig;
import fr.zirisgambling.inventories.LaunchMenu;
import fr.zirisgambling.utils.GamblingUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvents implements Listener {


    private final Main instance;
    public ChatEvents(final Main instance){
        this.instance = instance;

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();

        if(instance.getPreFightManager().getPlayersChat().contains(player.getUniqueId())){
            e.setCancelled(true);
            if(this.IsCorrectAmount(e.getMessage())){
                if(GamblingUtils.getMoney(player) > Integer.parseInt(e.getMessage())) {
                    instance.getPreFightManager().getPlayerResp().put(player.getUniqueId(), e.getMessage());
                    instance.getPreFightManager().removeToChatState(player);

                    new LaunchMenu(instance).openInv(player);
                }else{
                    player.sendMessage(FileConfig.NOT_ENOUGH_MONEY.get());
                }
            }else{
                player.sendMessage(FileConfig.NOT_CORRECT_AMOUNT.get());
            }
        }

    }


    private boolean IsCorrectAmount(String message){
        int minBet = instance.getConfigManager().getMinBet();
        int maxBet = instance.getConfigManager().getMaxBet();
        return GamblingUtils.isInteger(message) && Integer.parseInt(message) >=minBet && Integer.parseInt(message)<=maxBet;
    }
}
