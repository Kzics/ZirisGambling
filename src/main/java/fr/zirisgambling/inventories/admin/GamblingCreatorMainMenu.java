package fr.zirisgambling.inventories.admin;

import fr.zirisgambling.Main;
import fr.zirisgambling.utils.ColorsUtil;
import fr.zirisgambling.utils.GamblingUtils;
import fr.zirisgambling.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GamblingCreatorMainMenu {


    private final Main main;
    private final Inventory inv;
    public GamblingCreatorMainMenu(final Main main){
        this.main = main;
        this.inv = main.getServer().createInventory(null,45,"Create Gambling");
    }


    public void openInv(final Player player){
        GamblingUtils.addGlasses(inv);


        inv.setItem(20,new ItemUtils(Material.DIAMOND_SWORD,1)
                .setDisplayName(ColorsUtil.translate.apply("&aCreate Gambling"))
                .build());

        inv.setItem(24,new ItemUtils(Material.BOOK,1)
                .setDisplayName(ColorsUtil.translate.apply("&aManage gambling games"))
                .build());

        player.openInventory(inv);
    }
}
