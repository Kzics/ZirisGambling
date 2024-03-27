package fr.zirisgambling.config;

import fr.zirisgambling.Main;
import fr.zirisgambling.utils.ColorsUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum FileConfig {
    WAITING_GAMBLING_INV,
    WAITING_GAMBLING_LORE,
    CREATE_GAMBLING_INV,
    CREATE_GAMBLING_LORE,
    LEAVE_INV,
    QUIT_INV,
    QUIT_LORE,
    LEAVE_LORE,
    PVP_KIT_INV,
    PVP_KIT_LORE,
    ROD_KIT_INV,
    ROD_KIT_LORE,
    BOW_KIT_INV,
    BOW_KIT_LORE,
    LAUNCH_INV,
    LAUNCH_LORE,
    STARTED_GAME_INV,
    STARTED_GAME_LORE,
    NOT_ENOUGH_MONEY,
    INCORRECT_NUMBER,
    WON_MESSAGE,
    EARNED_MESSAGE,
    LOST_MESSAGE,
    LOST_MONEY_MESSAGE,
    ALREADY_GAMBLING_LAUNCHED,
    CANCEL_GAMBLING,
    LIMIT_TIME,
    GAMBLING_CREATED,
    LINE_FORMAT,
    WAITING_MODE_LORE,
    WAITING_BET_LORE,
    WAITING_FIGHT_LORE,
    STARTED_MODE_LORE,
    STARTED_BET_LORE,
    STARTED_FIGHT_LORE,
    STARTED_LAST_LORE,
    ENEMY_MESSAGE,
    FOOT_MESSAGE,
    LEADERBOARD_GAMBLING_INV,
    LEADERBOARD_GAMBLING_LORE,
    MAIN_GAMBLING_TITLE,
    WAITING_GAMBLING_TITLE,
    STARTED_GAMBLING_TITLE,
    MODE_TITLE,
    LAUNCH_TITLE,
    PVP_KIT_TITLE,
    BOW_KIT_TITLE,
    ROD_KIT_TITLE,
    WINS_MESSAGE,
    LOSES_MESSAGE,
    GAMES_MESSAGE,
    INFO_MESSAGE,
    MIN_MAX_MESSAGE,
    NOT_CORRECT_AMOUNT;


    public String get(){
        String value = name()
                .replace("_","-")
                .toLowerCase();

        FileConfiguration langConfig = Main.getInstance()
                .getLangFile()
                .getFileConfig();

        String configValue = langConfig
                .getString(value);

        return ColorsUtil.translate.apply(configValue);
    }

    public List<String> getList(){
        String value = name()
                .replace("_","-")
                .toLowerCase();

        FileConfiguration langConfig = Main.getInstance()
                .getLangFile()
                .getFileConfig();

        List<String> configValue = langConfig.getStringList(value);
        configValue.forEach(val-> val = ColorsUtil.translate.apply(val));

        return configValue;
    }

    public static List<String> getInventoryTitles(){
        return Arrays.stream(values())
                 .filter(v-> v.name().endsWith("TITLE"))
                 .collect(Collectors.toList())
                 .stream()
                 .map(FileConfig::get)
                 .collect(Collectors.toList());
    }

}
