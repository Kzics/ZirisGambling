package fr.zirisgambling.listeners.customevent;

import fr.zirisgambling.Gambling;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CreateGamblingEvent extends Event implements Cancellable {

    public static HandlerList hList = new HandlerList();

    private final Gambling game;

    public CreateGamblingEvent(final Gambling game){
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

    public Gambling getGame() {
        return game;
    }
}
