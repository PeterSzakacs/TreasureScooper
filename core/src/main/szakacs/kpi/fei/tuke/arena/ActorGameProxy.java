package szakacs.kpi.fei.tuke.arena;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.HorizontalTunnelBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.HorizontalTunnelUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 28.1.2017.
 */
public class ActorGameProxy extends PlayerGameProxy implements ActorGameInterface {

    public ActorGameProxy(GameLevelPrivileged game){
        super(game);
    }

    @Override
    public GameWorldUpdatable getGameWorld() {
        return gameWorld;
    }

    @Override
    public void registerActor(ActorPrivileged actor, Runnable action) {
        actorManager.registerActor(actor, action);
    }

    @Override
    public void unregisterActor(ActorBasic actor) {
        actorManager.unregisterActor(actor);
    }

    @Override
    public Set<Pipe> getPipesUpdatable() {
        return playerManager.getPipesUpdatable();
    }

    @Override
    public MethodCallAuthenticator getAuthenticator() {
        return game.getAuthenticator();
    }
}
