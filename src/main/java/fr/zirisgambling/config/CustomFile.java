package fr.zirisgambling.config;

import fr.zirisgambling.Main;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class CustomFile {

    private final Main instance;
    private final String fileName;
    private final File dataFolder;
    private FileConfiguration fileConfig;
    private  File langFile;

    public CustomFile(final Main instance, final String fileName){
        this.instance = instance;
        this.fileName = fileName;
        this.dataFolder = instance.getDataFolder();

    }

    public File getFile(){
        return new File(instance.getDataFolder(),this.fileName + ".yml");
    }


    public void createFile(){
        langFile = new File(instance.getDataFolder(),this.fileName + ".yml");

        InputStream langIn = instance.getResource("lang.yml");

        if(!langFile.exists()) {

            try {
                FileUtils.copyInputStreamToFile(langIn, langFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Fichier créer ! ");
        }
        fileConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    public void createEmptyFile(){
        langFile = new File(instance.getDataFolder(),this.fileName + ".yml");

        if(!langFile.exists()){
            try {
                langFile.createNewFile();
                System.out.println(this.fileName + " a été créer ! ");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        fileConfig = YamlConfiguration.loadConfiguration(langFile);

    }

    public FileConfiguration getFileConfig() {
        return fileConfig;
    }

    public String getName(){
        return this.fileName;
    }

    public void saveConfig(){
        try{

            this.getFileConfig().save(getFile());

        }catch (Exception e){
            Bukkit.getConsoleSender().sendMessage("Sauvegarde du fichier " + this.fileName + " est impossible");
        }
    }

}
