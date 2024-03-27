package fr.zirisgambling.commands;

import fr.zirisgambling.Main;
import fr.zirisgambling.inventories.MainMenu;
import fr.zirisgambling.utils.ColorsUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamblingCommand implements CommandExecutor {

    private final Main instance;

    public GamblingCommand(final Main instance){
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return false;


        Player player = (Player) sender;

        if (args.length == 0) {
                instance.getConfigManager().createStats(player);
                new MainMenu(instance).openInv(player);
            return false;
        }else if(args.length == 1){
            if(player.hasPermission("gambling.admins")) {
                if (args[0].equalsIgnoreCase("hologram")) {
                    player.sendMessage(ColorsUtil.translate.apply("&&New Hologram location set !"));
                    instance.getHologramFile().getFileConfig().set("location", player.getLocation());
                    instance.getHologramFile().saveConfig();

                } else if (args[0].equalsIgnoreCase("reset")) {
                    player.sendMessage(ColorsUtil.translate.apply("&aUsers stats reset"));

                    instance.getConfig().set("Stats", null);
                    instance.getConfig().set("gameCreated", null);
                    instance.saveConfig();

                }
            }
        }
        return true;
    }



}
