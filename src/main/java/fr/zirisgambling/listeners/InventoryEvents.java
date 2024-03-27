package fr.zirisgambling.listeners;

import fr.zirisgambling.Gambling;
import fr.zirisgambling.GameModes;
import fr.zirisgambling.Main;
import fr.zirisgambling.config.FileConfig;
import fr.zirisgambling.inventories.*;
import fr.zirisgambling.inventories.kits.BowKitMenu;
import fr.zirisgambling.inventories.kits.PvPKitMenu;
import fr.zirisgambling.inventories.kits.RodKitMenu;
import fr.zirisgambling.listeners.customevent.CreateGamblingEvent;
import fr.zirisgambling.listeners.customevent.StartEvent;
import fr.zirisgambling.utils.ColorsUtil;
import fr.zirisgambling.utils.GamblingUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.List;


public class InventoryEvents implements Listener {

    private final Main instance;
    private final List<String> kitInv;

    public InventoryEvents(final Main instance) {
        this.instance = instance;
        kitInv = new ArrayList<>();
        kitInv.add(BowKitMenu.getTitle());
        kitInv.add(PvPKitMenu.getTitle());
        kitInv.add(RodKitMenu.getTitle());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {

        if (e.getInventory().getTitle().equals(LaunchMenu.getTitle())) {
            if(instance.getPreFightManager().getPlayerCreating().contains(e.getPlayer().getUniqueId())) {
                instance.getPreFightManager().getPlayerCreating().remove(e.getPlayer().getUniqueId());
                e.getPlayer().sendMessage(FileConfig.CANCEL_GAMBLING.get());
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
            return;
        }

        if(FileConfig.getInventoryTitles().contains(player.getOpenInventory().getTopInventory().getTitle())){
            if (e.getClickedInventory().equals(player.getOpenInventory().getBottomInventory())){
                e.setCancelled(true);
                return;
            }
        }

        if(kitInv.contains(e.getClickedInventory().getTitle())) e.setCancelled(true);


        if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(FileConfig.LEAVE_INV.get())) {
                new MainMenu(instance).openInv(player);
            }

            if (e.getClickedInventory().getTitle().equals(MainMenu.getTitle())) {
                if (!e.getCurrentItem().hasItemMeta()) return;

                e.setCancelled(true);

                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(FileConfig.CREATE_GAMBLING_INV.get())) {
                    if (!(instance.getGamblingManager().hasAlreadyGambling(player))) {
                        new ModeMenu(instance).openInv(player);

                    } else {
                        player.playSound(player.getLocation(), Sound.ANVIL_USE, 5f, 5f);
                        player.sendMessage(FileConfig.ALREADY_GAMBLING_LAUNCHED.get());

                    }
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(FileConfig.WAITING_GAMBLING_INV.get())) {
                    new WaitingGamblingMenu(instance).openInv(player);
                } else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ColorsUtil.translate.apply("&cQuitter"))){
                    player.closeInventory();
                }
            } else if (e.getClickedInventory().getTitle().equals(ModeMenu.getTitle())) {
                e.setCancelled(true);
                if (e.getClick().isLeftClick()) {
                    if (e.getCurrentItem().getItemMeta().hasDisplayName()) {
                        switch (e.getCurrentItem().getType()) {
                            case DIAMOND_HELMET:
                            case BOW:

                            case FISHING_ROD:
                                int minBet = instance.getConfigManager().getMinBet();
                                int maxBet = instance.getConfigManager().getMaxBet();

                                player.closeInventory();
                                instance.getPreFightManager().getPlayerCreating().add(player.getUniqueId());
                                instance.getPreFightManager().addToChatState(player);
                                instance.getPreFightManager().getPlayerChoseMode().put(player.getUniqueId(), GameModes.getModeByMaterial(e.getCurrentItem().getType()));
                                player.sendMessage(ColorsUtil.translate.apply(FileConfig.MIN_MAX_MESSAGE.get()
                                        .replace("{min}",String.valueOf(minBet))
                                        .replace("{max}",String.valueOf(maxBet))));

                                instance.getServer().getScheduler().runTaskLater(instance, () -> {
                                    instance.getPreFightManager().removeToChatState(player);

                                    if (!instance.getPreFightManager().getPlayerResp().containsKey(player.getUniqueId())) {
                                        player.sendMessage(FileConfig.LIMIT_TIME.get());
                                        instance.getPreFightManager().getPlayerCreating().remove(player.getUniqueId());

                                    }
                                }, instance.getConfigManager().getTimeToAnswer()* 20L);

                                break;
                        }
                    }
                }else{

                    switch (e.getCurrentItem().getType()){
                        case DIAMOND_HELMET:
                            new PvPKitMenu(instance).openInv(player);
                            break;
                        case BOW:
                            new BowKitMenu(instance).openInv(player);
                            break;
                        case FISHING_ROD:
                            new RodKitMenu(instance).openInv(player);
                            break;

                    }
                }
            } else if (e.getClickedInventory().getTitle().equals(LaunchMenu.getTitle())) {
                e.setCancelled(true);
                if (e.getCurrentItem().hasItemMeta()) {
                    if (e.getCurrentItem().getType().equals(Material.REDSTONE_TORCH_ON)) {
                        instance.getPreFightManager().getPlayerCreating().remove(player.getUniqueId());
                        player.closeInventory();

                        player.sendMessage(FileConfig.GAMBLING_CREATED.get());

                        Gambling createdGame = new Gambling(instance,instance.getPreFightManager().getPlayerChoseMode().get(player.getUniqueId()), player,
                                Integer.parseInt(instance.getPreFightManager().getPlayerResp().get(player.getUniqueId())), instance.getConfigManager().getCurrentMaxID());

                        CreateGamblingEvent gamblingEvent = new CreateGamblingEvent(createdGame);

                        instance.getServer().getPluginManager().callEvent(gamblingEvent);
                    }
                }
            } else if (e.getClickedInventory().getTitle().equals(WaitingGamblingMenu.getTitle())) {
                e.setCancelled(true);

                if(e.getCurrentItem().getItemMeta().hasLore() && !this.isCorrectItem(e.getCurrentItem().getItemMeta().getDisplayName()) ) {
                    Gambling gambling = instance.getGamblingManager().getGameByName(e.getCurrentItem().getItemMeta().getDisplayName()
                            .substring(4));

                    if(gambling.getHost().getUniqueId() != player.getUniqueId() && GamblingUtils.getMoney(player) > gambling.getMoneyBet()) {
                        StartEvent ev = new StartEvent(gambling.getHost(), player, gambling);

                        instance.getServer().getPluginManager().callEvent(ev);
                    }

                }else{
                    if(e.getCurrentItem().getItemMeta().getDisplayName().equals(FileConfig.STARTED_GAME_INV.get())){
                        new StartedGamblingMenu(instance).openInv(player);
                    }
                }

            }else if(e.getClickedInventory().getTitle().equals(StartedGamblingMenu.getTitle())){
                e.setCancelled(true);
            }else if(e.getClickedInventory().getTitle().equals(PvPKitMenu.getTitle())){
                e.setCancelled(true);
            }
        }
    }

    private boolean isCorrectItem(String displayName){
        return displayName.equals(FileConfig.LEAVE_INV.get())
                || displayName.equals(FileConfig.STARTED_GAME_INV.get());
    }
}
