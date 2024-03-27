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

public class MainMenu {


    private final Main instance;
    private final Inventory inv;

    public MainMenu(final Main instance){
        this.instance = instance;
        inv = Bukkit.createInventory(null,45,FileConfig.MAIN_GAMBLING_TITLE.get());
    }

    public void openInv(Player player){

        this.inv.setItem(4, new ItemUtils(new ItemStack(Material.SKULL_ITEM,1,(short) 3))
                        .setDisplayName(ColorsUtil.translate.apply(FileConfig.INFO_MESSAGE.get()))
                        .addLores(ColorsUtil.translate.apply(FileConfig.WINS_MESSAGE.get() + instance.getConfigManager().getPlayerWins(player.getName())),
                                ColorsUtil.translate.apply(FileConfig.LOSES_MESSAGE.get() + instance.getConfigManager().getPlayerLoses(player.getName())),
                                ColorsUtil.translate.apply(FileConfig.GAMES_MESSAGE.get() + instance.getConfigManager().getPlayerGames(player.getName())))
                .toPlayerHead(player.getName()));


        this.inv.setItem(21,new ItemUtils(Material.FEATHER,1)
                        .setDisplayName(FileConfig.WAITING_GAMBLING_INV.get())
                        .addLores(FileConfig.WAITING_GAMBLING_LORE.getList())
                .build());

        this.inv.setItem(23,new ItemUtils(Material.IRON_SWORD,1)
                .setDisplayName(FileConfig.CREATE_GAMBLING_INV.get())
                .addLores(FileConfig.CREATE_GAMBLING_LORE.getList())
                .build());

        this.inv.setItem(40,new ItemUtils(Material.BARRIER,1)
                        .setDisplayName(ColorsUtil.translate.apply("&cQuitter"))
                .build());

        GamblingUtils.addGlasses(inv);
        player.openInventory(this.inv);

    }


    public static String getTitle(){
        return FileConfig.MAIN_GAMBLING_TITLE.get();
    }
}
