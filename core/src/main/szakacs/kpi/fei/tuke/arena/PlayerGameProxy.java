package szakacs.kpi.fei.tuke.arena;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.game.world.HorizontalTunnel;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.arena.game.GameShop;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorld;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    protected GameLevelPrivileged game;
    protected ActorManagerPrivileged actorManager;

    public PlayerGameProxy(GameLevelPrivileged game, ActorManagerPrivileged actorManager){
        this.game = game;
        this.actorManager = actorManager;
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
    public Map<Player, Pipe> getPlayerToPipeMap() {
        return actorManager.getPlayerToPipeMap();
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
