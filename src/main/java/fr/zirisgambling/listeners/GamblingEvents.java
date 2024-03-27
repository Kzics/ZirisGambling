package fr.zirisgambling.listeners;

import fr.zirisgambling.Gambling;
import fr.zirisgambling.Main;
import fr.zirisgambling.config.FileConfig;
import fr.zirisgambling.inventories.WaitingGamblingMenu;
import fr.zirisgambling.listeners.customevent.CreateGamblingEvent;
import fr.zirisgambling.listeners.customevent.StartEvent;
import fr.zirisgambling.listeners.customevent.WinEvent;
import fr.zirisgambling.entity.HologramEntity;
import fr.zirisgambling.manager.LeaderboardManager;
import fr.zirisgambling.utils.ColorsUtil;
import fr.zirisgambling.utils.GamblingUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.stream.Collectors;

public class GamblingEvents implements Listener {

    private final Main instance;
    public GamblingEvents(final Main instance){
        this.instance = instance;

    }

    @EventHandler
    public void onWin(WinEvent e){
        Gambling game = e.getGame();
        instance.getConfigManager().addPlayerGame(e.getWinner().getName());
        instance.getConfigManager().addPlayerWin(e.getWinner().getName());

        instance.getConfigManager().addPlayerGame(e.getLoser().getName());
        instance.getConfigManager().addPlayerLose(e.getLoser().getName());

        e.getWinner().sendMessage(FileConfig.WON_MESSAGE.get());
        e.getWinner().sendMessage("\n\n" + FileConfig.EARNED_MESSAGE.get()
                .replace("{money}",Main.getEcon().format(e.getGame().getMoneyBet())));

        GamblingUtils.addMoneyTo(e.getWinner(),e.getGame().getMoneyBet());

        if(e.getLoser() != null){
            e.getLoser().sendMessage(FileConfig.LOST_MESSAGE.get());
            e.getLoser().sendMessage(ColorsUtil.translate.apply("\n\n" + FileConfig.LOST_MONEY_MESSAGE.get()
                    .replace("{money}",Main.getEcon().format(e.getGame().getMoneyBet()))));

            GamblingUtils.removeMoneyTo(e.getLoser(),e.getGame().getMoneyBet());
        }
        e.getGame().showPlayers();

        if(instance.getCurrentHologram() != null) instance.getCurrentHologram().getEntity().removeEntity();
        HologramEntity hologram = new HologramEntity(new LeaderboardManager(instance)
                .getLeaderboard()
                .collect(Collectors.toList()));

        instance.setCurrentHologram(hologram);

        FileConfiguration hologramConfig = instance.getHologramFile().getFileConfig();

        hologram.spawnHologram(hologramConfig.get("location") != null ? (Location) hologramConfig.get("location") :
                new Location(e.getWinner().getWorld(),19,66,-67));


        instance.getGamblingManager().removeGamblingGame(game.getGameId());
    }

    @EventHandler
    public void onStart(StartEvent e){
        e.getGame().startGambling(e.getJoined());

        instance.getGamblingManager().getPlayersFighting().add(e.getGame().getHost().getUniqueId());
        instance.getGamblingManager().getPlayersFighting().add(e.getJoined().getUniqueId());

        for (UUID player : instance.getGamblingManager().getPlayersFighting()) {
            if (instance.getGamblingManager().getPlayersFighting().contains(player)) {
                Gambling gambling = instance.getGamblingManager().getGameByUUID(player);
                if (gambling == null || gambling.getGameId() != e.getGame().getGameId()) {
                    try {
                        Object craftPlayer = Bukkit.getPlayer(player).getClass().getMethod("getHandle").invoke(Bukkit.getPlayer(player));
                        Object connection = craftPlayer.getClass().getField("playerConnection").get(craftPlayer);
                        Object networkManager = connection.getClass().getField("networkManager").get(connection);
                        Channel channel = (Channel) networkManager.getClass().getField("channel").get(networkManager);
                        channel.pipeline().addBefore("packet_handler", instance.getServer().getPlayer(player).getName(), new ChannelDuplexHandler() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                Class<?> packetClass = Class.forName("net.minecraft.server." + getVersion() + ".Packet");
                                if (packetClass.isInstance(msg)) {
                                    if (msg.getClass().getSimpleName().equals("PacketPlayOutNamedSoundEffect")) {
                                        Field nameField = msg.getClass().getDeclaredField("a");
                                        nameField.setAccessible(true);
                                        String soundName = (String) nameField.get(msg);
                                        if (soundName.equals("random.explode")) return;
                                    }

                                    if (msg.getClass().getSimpleName().equals("PacketPlayOutWorldEvent") || msg.getClass().getSimpleName().equals("PacketPlayOutSpawnEntity")) {
                                        if (e.getGame().getWinner() != null) {
                                            ctx.pipeline().close();
                                        }
                                        return;
                                    }
                                }
                                super.write(ctx, msg, promise);
                            }
                        });
                    } catch (Exception ev) {
                        ev.printStackTrace();
                    }
                }
            }
        }


        instance.getServer().getOnlinePlayers()
                .forEach(player -> {
                    if(player.getOpenInventory().getTopInventory().getTitle().equals(WaitingGamblingMenu.getTitle())){
                        new WaitingGamblingMenu(instance).openInv(player);
                    }
                });
    }


    @EventHandler
    public void onCreate(CreateGamblingEvent e){
        instance.getConfigManager().incrementMaxGame();
        instance.getGamblingManager().addGamblingGame(e.getGame());

        instance.getServer().getOnlinePlayers()
                .forEach(player -> {
                    if(player.getOpenInventory().getTopInventory().getTitle().equals(WaitingGamblingMenu.getTitle())){
                        new WaitingGamblingMenu(instance).openInv(player);
                    }
                });
    }

    private String getVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }
}
