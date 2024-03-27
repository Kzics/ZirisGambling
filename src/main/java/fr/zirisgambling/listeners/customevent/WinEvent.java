package fr.zirisgambling.listeners.customevent;

import fr.zirisgambling.Gambling;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WinEvent extends Event implements Cancellable {

    public static HandlerList hList = new HandlerList();

    private final Player winner;
    private final Player loser;
    private final Gambling game;

    public WinEvent(final Player player, final Player player2, Gambling game){
        this.winner  = player;
        this.loser = player2;
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

    public Player getWinner() {
        return this.winner;
    }

    public Player getLoser(){
        return this.loser;
    }

    public Gambling getGame() {
        return game;
    }
}
