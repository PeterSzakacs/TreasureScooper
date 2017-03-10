package szakacs.kpi.fei.tuke.arena;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.game.GameShop;
import szakacs.kpi.fei.tuke.arena.game.world.HorizontalTunnel;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldQueryable;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by developer on 5.12.2016.
 *
 * Used as a proxy object to make it impossible for the player to access
 * methods of the game gameInterface he/she has no authorization to access (registerActor(),
 * unregisterActor(), update()) by downcasting the reference to the game gameInterface
 * from PlayerGameInterface to ActorGameInterface. This is therefore
 * to prevent cheating on part of the player.
 */
public class PlayerGameProxy implements PlayerGameInterface {

    protected GameWorldQueryable worldProxy = new GameWorldQueryable() {
        public int getWidth() { return gameWorld.getWidth(); }
        public int getHeight() { return gameWorld.getHeight(); }
        public int getOffsetX() { return gameWorld.getOffsetX(); }
        public int getOffsetY() { return gameWorld.getOffsetY(); }
        public int getNuggetCount() { return gameWorld.getNuggetCount(); }
        public Map<String, TunnelCell> getEntrances() { return gameWorld.getEntrances(); }
        public Set<HorizontalTunnel> getTunnels() { return gameWorld.getTunnels(); }
    };

    protected GameLevelPrivileged game;
    protected GameWorldPrivileged gameWorld;
    protected ActorManagerPrivileged actorManager;
    protected PlayerManagerPrivileged playerManager;

    public PlayerGameProxy(GameLevelPrivileged game){
        this.game = game;
        this.gameWorld = game.getGameWorld();
        this.actorManager = game.getActorManager();
        this.playerManager = game.getPlayerManager();
    }

    @Override
    public Set<Actor> getActors() {
        return actorManager.getActors();
    }

    @Override
    public Set<Actor> getActorsBySearchCriteria(Predicate<Actor> predicate) {
        return actorManager.getActorsBySearchCriteria(predicate);
    }

    @Override
    public GameState getState() {
        return game.getState();
    }

    @Override
    public GameWorldQueryable getGameWorld() {
        return worldProxy;
    }

    @Override
    public Set<Pipe> getPipes() {
        return playerManager.getPipes();
    }

    @Override
    public Map<Player, Integer> getPlayersAndScores() {
        return playerManager.getPlayersAndScores();
    }

    @Override
    public GameShop getGameShop() {
        return playerManager.getGameShop();
    }
}
