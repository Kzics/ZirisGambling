package fr.zirisgambling.inventories;

import fr.zirisgambling.Main;
import fr.zirisgambling.config.FileConfig;
import fr.zirisgambling.utils.ColorsUtil;
import fr.zirisgambling.utils.GamblingUtils;
import fr.zirisgambling.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WaitingGamblingMenu {
    private final Main instance;
    private final Inventory inv;

    public WaitingGamblingMenu(final Main instance){
        this.instance = instance;
        this.inv = Bukkit.createInventory(null,45,FileConfig.WAITING_GAMBLING_TITLE.get());
    }

    public void openInv(Player player){

        GamblingUtils.addAroundGlasses(inv);

        instance.getGamblingManager().getWaitingGames()
                .forEach(game->{
                    ItemStack it = new ItemUtils(new ItemStack(Material.SKULL_ITEM,1,(short) 3))
                            .setDisplayName(ColorsUtil.translate.apply("&e&l" + game.getHost().getDisplayName()))
                            .addLores(" ", FileConfig.WAITING_MODE_LORE.get()
                                            .replace("{mode}",game.getMode().toName())
                            ,FileConfig.WAITING_BET_LORE.get()
                                            .replace("{money}",String.valueOf(game.getMoneyBet())), " "," ",
                             FileConfig.WAITING_FIGHT_LORE.get()
                                     .replace("{host}",game.getHost().getName()))
                            .toPlayerHead(player.getName());

                    inv.addItem(it);
        });



        inv.setItem(40,new ItemUtils(Material.ENDER_CHEST,1)
                .setDisplayName(FileConfig.STARTED_GAME_INV.get())
                        .addLores(FileConfig.STARTED_GAME_LORE.getList())
                .build());

        inv.setItem(44,new ItemUtils(Material.ARROW,1)
                        .setDisplayName(FileConfig.LEAVE_INV.get())
                        .addLores(FileConfig.LEAVE_LORE.getList())
                .build());


        player.openInventory(this.inv);
    }

    public static String getTitle(){
        return FileConfig.WAITING_GAMBLING_TITLE.get();
    }
}
