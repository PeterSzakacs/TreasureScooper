package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.arena.ActorGameProxy;
import szakacs.kpi.fei.tuke.arena.PlayerGameProxy;
import szakacs.kpi.fei.tuke.arena.game.world.TreasureScooperWorld;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameStateTester;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.misc.ConfigProcessingException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.*;

/**
 * Created by developer on 25.1.2017.
 */
public class TreasureScooperLevel implements GameLevelPrivileged {

    private class GameProxies {
        private ActorGameInterface actorInterface;
        private PlayerGameInterface playerInterface;

        private GameProxies(){
            this.actorInterface = new ActorGameProxy(TreasureScooperLevel.this);
            this.playerInterface = new PlayerGameProxy(TreasureScooperLevel.this);
        }
    }

    private MethodCallAuthenticator authenticator = new MethodCallAuthenticator() {
        @Override
        public boolean authenticate(Object token) {
            return token != null && token.equals(this);
        }
    };

    private GameUpdater[] updaterInstances;
    private Set<GameUpdater> gameUpdaters;

    private GameWorldPrivileged gameWorld;
    private ActorManagerPrivileged actorManager;
    private PlayerManagerPrivileged playerManager;
    private GameStateTester stateTester;
    private GameState state;
    private GameProxies proxies;

    public TreasureScooperLevel(GameConfig config) throws ConfigProcessingException {
        this.state = GameState.INITIALIZING;
        this.gameWorld = new TreasureScooperWorld(authenticator);
        this.actorManager = new ActorManager(authenticator);
        this.playerManager = new PlayerManager(config, authenticator);
        this.setGameUpdaters(config);
        this.stateTester = new GameStateTesterBasic();
        this.proxies = new GameProxies();
    }

    private void setGameUpdaters(GameConfig config) throws ConfigProcessingException {
        Set<Class<? extends GameUpdater>> updaterClasses = config.getUpdaterClasses();
        int size = updaterClasses.size();
        this.updaterInstances = new GameUpdater[size];
        this.gameUpdaters = new HashSet<>(size);
        int idx = 0;
        for (Class<? extends GameUpdater> updaterCls : updaterClasses){
            try {
                updaterInstances[idx++] = updaterCls.newInstance();
            } catch (InstantiationException
                    | IllegalAccessException e) {
                throw new ConfigProcessingException("Failed to initialize game updater class: " + updaterCls, e);
            }
        }
    }

    // GameLevelQueryable methods (only queries)

    @Override
    public GameState getState() {
        return state;
    }

    // GameLevelUpdatable methods (access to the method call authenticator and game world)

    @Override
    public MethodCallAuthenticator getAuthenticator() {
        return authenticator;
    }

    // GameLevelPrivileged methods (access to all actor manager functions and updating the whole game).

    @Override
    public void startNewGame(DummyLevel level){
        gameWorld.startNewGame(this, level);
        actorManager.startNewGame(this, level);
        playerManager.startNewGame(this, level);
        gameUpdaters.clear();
        Set<Class<? extends GameUpdater>> updaterClasses = level.getGameUpdaterClasses();
        for (Class<? extends GameUpdater> updaterCls : updaterClasses){
            for (GameUpdater updater : updaterInstances){
                if (updater.getClass().equals(updaterCls)){
                    gameUpdaters.add(updater);
                    updater.startNewGame(this, level);
                    break;
                }
            }
        }
        stateTester.startNewGame(this, level);
        this.state = GameState.PLAYING;
    }

    @Override
    public void update() {
        playerManager.update();
        actorManager.update();
        for (GameUpdater updater : this.gameUpdaters)
            updater.update();
        if (state == GameState.PLAYING) {
            state = stateTester.testGameState();
            switch (state) {
                case PLAYING:
                case WON:
                    break;
                case LOST:
                    for (Player player : playerManager.getPlayersAndScores().keySet()) {
                        player.deallocate();
                    }
                    break;
            }
        }
    }

    @Override
    public GameWorldPrivileged getGameWorld() {
        return gameWorld;
    }

    @Override
    public ActorManagerPrivileged getActorManager() {
        return actorManager;
    }

    @Override
    public PlayerManagerPrivileged getPlayerManager() {
        return playerManager;
    }

    @Override
    public ActorGameInterface getActorInterface() {
        return proxies.actorInterface;
    }

    @Override
    public PlayerGameInterface getPlayerInterface() {
        return proxies.playerInterface;
    }
}
