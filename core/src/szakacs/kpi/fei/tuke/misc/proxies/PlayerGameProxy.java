package szakacs.kpi.fei.tuke.misc.proxies;

import szakacs.kpi.fei.tuke.arena.pipe.Pipe;
import szakacs.kpi.fei.tuke.game.world.HorizontalTunnel;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.game.GameShop;
import szakacs.kpi.fei.tuke.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.proxies.PlayerGameInterface;
import szakacs.kpi.fei.tuke.intrfc.game.GameWorld;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by developer on 5.12.2016.
 *
 * Used as a proxy object to make it impossible for the player to access
 * methods of the game world he/she has no authorization to access (registerActor(),
 * unregisterActor(), update()) by downcasting the reference to the game world
 * from PlayerGameInterface to ActorGameInterface. This is therefore
 * to prevent cheating on part of the player.
 */
public class PlayerGameProxy implements PlayerGameInterface {

    protected GameLevelPrivileged game;
    protected GameWorld gameWorld;
    protected ActorManagerPrivileged actorManager;

    public PlayerGameProxy(GameLevelPrivileged game){
        this.game = game;
        this.gameWorld = game.getGameWorld();
        this.actorManager = game.getActorManager();
    }

    @Override
    public List<Actor> getActors() {
        return Collections.unmodifiableList(actorManager.getActors());
    }

    @Override
    public List<Actor> getActorsBySearchCriteria(Predicate<Actor> predicate) {
        return actorManager.getActorsBySearchCriteria(predicate);
    }

    @Override
    public Pipe getPipe() {
        return actorManager.getPipe();
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
    public int getNuggetCount() {
        return gameWorld.getNuggetCount();
    }

    @Override
    public List<HorizontalTunnel> getTunnels() {
        return gameWorld.getTunnels();
    }

    @Override
    public Player getPlayer() {
        return game.getPlayer();
    }

    @Override
    public GameState getState() {
        return game.getState();
    }

    @Override
    public GameWorld getGameWorld() {
        return game.getGameWorld();
    }

    @Override
    public int getScore() {
        return game.getScore();
    }

    @Override
    public GameShop getGameShop() {
        return game.getGameShop();
    }
}
