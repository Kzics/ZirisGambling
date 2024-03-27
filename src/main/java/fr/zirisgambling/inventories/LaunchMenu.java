package fr.zirisgambling.inventories;

import fr.zirisgambling.Main;
import fr.zirisgambling.config.FileConfig;
import fr.zirisgambling.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class LaunchMenu {


    private final Main instance;
    private final Inventory inv;

    public LaunchMenu(final Main instance){
        this.instance = instance;
        this.inv = Bukkit.createInventory(null,45,FileConfig.LAUNCH_TITLE.get());
    }

    public void openInv(Player player){

        inv.setItem(44,new ItemUtils(Material.REDSTONE_TORCH_ON,1)
                        .setDisplayName(FileConfig.LAUNCH_INV.get())
                        .addLores(FileConfig.LAUNCH_LORE.getList())
                .build());


        player.openInventory(this.inv);
    }

    public static String getTitle(){
        return FileConfig.LAUNCH_TITLE.get();
    }
}
