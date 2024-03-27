package fr.zirisgambling.manager;

import fr.zirisgambling.GameMode;
import fr.zirisgambling.Main;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class KitsManager {


    private final Set<GameMode> availableModes;
    private final Main main;
    public KitsManager(final Main main){
        this.availableModes = new HashSet<>();
        this.main = main;
    }

    public Set<GameMode> getAvailableModes() {
        return availableModes;
    }

    public void loadModes(){
        File kitsFolder = new File(main.getDataFolder() , "kits");
        File[] kits = kitsFolder.listFiles();
        if(kits == null) return;

        for(File f : kits){
            this.availableModes.add(new GameMode(f));
        }
    }
}
