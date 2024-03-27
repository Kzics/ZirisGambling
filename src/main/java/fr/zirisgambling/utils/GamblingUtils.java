package fr.zirisgambling.utils;

import fr.zirisgambling.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Method;
import java.util.*;

public class GamblingUtils {



    public static ItemStack getHeadOf(String player){
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        meta.setOwner(player);

        skull.setItemMeta(meta);

        return skull;
    }

    public static boolean isInteger(String num){
        try{

            Integer.parseInt(num);

            return true;
        }catch (Exception e){
            return false;
        }

    }

    public static String capitalizeFirst(String str){
        return str.substring(0,1).toUpperCase(Locale.ROOT)
                + str.substring(1);
    }

    public static boolean isArmor(ItemStack item) {
        try {
            Class<?> craftItemStackClass = Class.forName("org.bukkit.craftbukkit." + getVersion() + ".inventory.CraftItemStack");
            Method asNMSCopyMethod = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);

            Object nmsItemStack = asNMSCopyMethod.invoke(null, item);
            Class<?> nmsItemClass = nmsItemStack.getClass();
            Method getItemMethod = nmsItemClass.getMethod("getItem");

            Object nmsItem = getItemMethod.invoke(nmsItemStack);
            Class<?> itemArmorClass = Class.forName("net.minecraft.server." + getVersion() + ".ItemArmor");

            return itemArmorClass.isAssignableFrom(nmsItem.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }
    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        Set<T> keys = new HashSet<T>();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    public static void addMoneyTo(OfflinePlayer player, int amount){
        Main.getEcon().depositPlayer(player,amount);
    }
    public static void removeMoneyTo(OfflinePlayer player, int amount){
        Main.getEcon().withdrawPlayer(player,amount);
    }
    public static double getMoney(OfflinePlayer player){
        return Main.getEcon().getBalance(player);
    }


    private static void addInventoryGlass(Inventory inv,int slot1,int slot2,int slot3){
        inv.setItem(slot1,new ItemUtils(Material.STAINED_GLASS_PANE,1)
                .setDurability((short) 1)
                .setDisplayName(" ")
                .build());
        inv.setItem(slot2,new ItemUtils(Material.STAINED_GLASS_PANE,1)
                .setDurability((short) 1)
                .setDisplayName(" ")
                .build());
        inv.setItem(slot3,new ItemUtils(Material.STAINED_GLASS_PANE,1)
                .setDurability((short) 1)
                .setDisplayName(" ")
                .build());
    }

    public static void addGlasses(Inventory inv){
        addInventoryGlass(inv,0,1,9);
        addInventoryGlass(inv,7,8,17);
        addInventoryGlass(inv,27,36,37);
        addInventoryGlass(inv,35,43,44);
    }

    public static void addAroundGlasses(Inventory inv){
        for(int i = 0;i<9;i++){
            inv.setItem(i,new ItemUtils(Material.STAINED_GLASS_PANE,1)
                    .setDurability((short) 1)
                    .setDisplayName(" ")
                    .build());
        }

        for(int i = 1;i<5;i++){
            inv.setItem(9*i,new ItemUtils(Material.STAINED_GLASS_PANE,1)
                    .setDurability((short) 1)
                    .setDisplayName(" ")
                    .build());
            inv.setItem((9*i) + 8,new ItemUtils(Material.STAINED_GLASS_PANE,1)
                    .setDurability((short) 1)
                    .setDisplayName(" ")
                    .build());
            if(i == 4){
                addLineGlasses(inv);

            }
        }
    }

    private static void addLineGlasses(Inventory inv){
        for(int i = 36;i<45;i++){
            if(i == 39 || i == 40 || i == 41){
                continue;
            }

            inv.setItem(i,new ItemUtils(Material.STAINED_GLASS_PANE,1)
                    .setDurability((short) 1)
                    .setDisplayName(" ")
                    .build());

        }
    }



}
