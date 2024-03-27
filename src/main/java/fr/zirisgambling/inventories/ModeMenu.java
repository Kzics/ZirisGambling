package fr.zirisgambling.inventories;

import fr.zirisgambling.Main;
import fr.zirisgambling.config.FileConfig;
import fr.zirisgambling.utils.GamblingUtils;
import fr.zirisgambling.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ModeMenu {


    private final Main instance;
    private final Inventory inv;

    public ModeMenu(final Main instance){
        this.instance = instance;
        inv = Bukkit.createInventory(null,45,FileConfig.MODE_TITLE.get());
    }


    public void openInv(Player player){
        inv.setItem(21,new ItemUtils(Material.DIAMOND_HELMET,1)
                .setDisplayName(FileConfig.PVP_KIT_INV.get())
                .addLores(FileConfig.PVP_KIT_LORE.getList())
                .build());

        inv.setItem(22,new ItemUtils(Material.BOW,1)
                .setDisplayName(FileConfig.BOW_KIT_INV.get())
                .addLores(FileConfig.BOW_KIT_LORE.getList())
                .build());

        inv.setItem(23,new ItemUtils(Material.FISHING_ROD,1)
                .setDisplayName(FileConfig.ROD_KIT_INV.get())
                .addLores(FileConfig.ROD_KIT_LORE.getList())
                .build());


        inv.setItem(40,new ItemUtils(Material.ARROW,1)
                .setDisplayName(FileConfig.LEAVE_INV.get())
                .addLores(FileConfig.LEAVE_LORE.getList())
                .build());

        GamblingUtils.addGlasses(inv);


        player.openInventory(this.inv);

    }


    public static String getTitle(){
        return FileConfig.MODE_TITLE.get();
    }
}
