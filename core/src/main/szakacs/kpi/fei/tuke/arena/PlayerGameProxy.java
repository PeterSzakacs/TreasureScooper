package szakacs.kpi.fei.tuke.arena;

import szakacs.kpi.fei.tuke.arena.game.GameShop;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerInfo;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.HorizontalTunnelBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;

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

    protected GameWorldBasic worldProxy = new GameWorldBasic() {
        public int getWidth() { return gameWorld.getWidth(); }
        public int getHeight() { return gameWorld.getHeight(); }
        public int getOffsetX() { return gameWorld.getOffsetX(); }
        public int getOffsetY() { return gameWorld.getOffsetY(); }
        public int getNuggetCount() { return gameWorld.getNuggetCount(); }
        public Map<String, TunnelCellBasic> getEntrances() { return gameWorld.getEntrances(); }
        public Set<HorizontalTunnelBasic> getTunnels() { return gameWorld.getTunnels(); }
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
    public Set<ActorBasic> getActors() {
        return actorManager.getActors();
    }

    @Override
    public Set<ActorBasic> getActorsByType(ActorType type) {
        return actorManager.getActorsByType(type);
    }

    @Override
    public Map<TunnelCellBasic, Set<ActorBasic>> getPositionToActorsMap() {
        return actorManager.getPositionToActorsMap();
    }

    @Override
    public Set<ActorBasic> getActorsBySearchCriteria(Predicate<ActorBasic> predicate) {
        return actorManager.getActorsBySearchCriteria(predicate);
    }

    @Override
    public GameState getState() {
        return game.getState();
    }

    @Override
    public GameWorldBasic getGameWorld() {
        return worldProxy;
    }

    @Override
    public Set<PlayerInfo> getPlayerInfo() {
        return playerManager.getPlayerInfo();
    }

    @Override
    public GameShop getGameShop() {
        return playerManager.getGameShop();
    }
}
