package fr.zirisgambling.listeners.customevent;

import fr.zirisgambling.Gambling;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StartEvent extends Event implements Cancellable {

    public static HandlerList hList = new HandlerList();
    private final Player host;
    private final Player joined;
    private final Gambling game;

    public StartEvent(final Player player, final Player player2, Gambling game){
        this.host  = player;
        this.joined = player2;
        this.game = game;
    }

    public static HandlerList getHandlerList() {
        return hList;
    }
    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }

    @Override
    public HandlerList getHandlers() {
        return hList;
    }

    public Player getHost() {
        return this.host;
    }

    public Player getJoined(){
        return this.joined;
    }

    public Gambling getGame() {
        return game;
    }
}
