package fr.zirisgambling.inventories.kits;

import fr.zirisgambling.Main;
import fr.zirisgambling.config.FileConfig;
import fr.zirisgambling.utils.ColorsUtil;
import fr.zirisgambling.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PvPKitMenu {


    private final Main instance;
    private final Inventory inv;

    public PvPKitMenu(final Main instance){
        this.instance = instance;
        inv = Bukkit.createInventory(null,45,FileConfig.PVP_KIT_TITLE.get());
    }


    public void openInv(Player player){

        this.instance.getGamblingManager().getPvPKit()
                .forEach(inv::addItem);

        inv.setItem(44,new ItemUtils(Material.SIGN,1)
                .setDisplayName(FileConfig.LEAVE_INV.get())
                .build());


        player.openInventory(inv);

    }

    public static String getTitle(){
        return FileConfig.PVP_KIT_TITLE.get();
    }
}
