package fr.zirisgambling;

import fr.zirisgambling.utils.GamblingUtils;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Locale;

public enum GameModes {

    PVP(Material.DIAMOND_HELMET),
    BOW(Material.BOW),
    ROD(Material.FISHING_ROD),
    CUSTOM(Material.BOOKSHELF);


    private Material displayMat;
    GameModes(Material displayMat){
        this.displayMat = displayMat;

    }

    public String toName(){
        return GamblingUtils.capitalizeFirst(name().toLowerCase(Locale.ROOT));
    }

    public Material getDisplayMat() {
        return displayMat;
    }

    public static GameModes getModeByMaterial(Material mat){
        return Arrays.stream(values()).filter(mode-> mode.getDisplayMat().equals(mat)).findFirst().get();
    }

    public void setDisplayMat(Material newMaterial){
        displayMat = newMaterial;

    }
}
