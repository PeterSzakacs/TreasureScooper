package com.szakacs.kpi.fei.tuke.game.arena.game;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.GameState;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.ManipulableGameInterface;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.QueryableGameInterface;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by developer on 5.12.2016.
 *
 * Used as a proxy object to make it impossible for the player to access
 * methods of the game world he has no authorization to access (registerActor(),
 * unregisterActor(), update()) by downcasting the reference to the game world
 * from QueryableGameInterface to ManipulableGameInterface. This is therefore
 * to prevent cheating on part of the player.
 */
public class GameProxy implements QueryableGameInterface{

    private ManipulableGameInterface gameWorld;

    public GameProxy(ManipulableGameInterface gameWorld){
        this.gameWorld = gameWorld;
    }

    @Override
    public List<Actor> getActors() {
        return gameWorld.getActors();
    }

    @Override
    public List<Actor> getActorsBySearchCriteria(Predicate<Actor> predicate) {
        return gameWorld.getActorsBySearchCriteria(predicate);
    }

    @Override
    public boolean intersects(Actor actorA, Actor actorB) {
        return gameWorld.intersects(actorA, actorB);
    }

    @Override
    public Pipe getPipe() {
        return gameWorld.getPipe();
    }

    @Override
    public TunnelCell getRootCell() {
        return gameWorld.getRootCell();
    }

    @Override
    public int getWidth() {
        return gameWorld.getWidth();
    }

    @Override
    public int getHeight() {
        return gameWorld.getHeight();
    }

    @Override
    public int getOffsetX() {
        return gameWorld.getOffsetX();
    }

    @Override
    public int getOffsetY() {
        return gameWorld.getOffsetY();
    }

    @Override
    public int getRemainingNuggetsCount() {
        return gameWorld.getRemainingNuggetsCount();
    }

    @Override
    public Collection<HorizontalTunnel> getTunnels() {
        return gameWorld.getTunnels();
    }

    @Override
    public GameState getState() {
        return gameWorld.getState();
    }
}
