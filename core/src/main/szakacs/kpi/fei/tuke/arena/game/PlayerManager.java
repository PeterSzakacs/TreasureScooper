package szakacs.kpi.fei.tuke.arena.game;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.player.BasePlayer;
import szakacs.kpi.fei.tuke.intrfc.player.Player;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnScoreEventCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerInfo;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldUpdatable;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.misc.GameLevelInitializationException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.*;
import java.util.function.Supplier;

/**
 * Created by developer on 7.3.2017.
 */
public class PlayerManager implements PlayerManagerPrivileged {

    private class PlayerTokenImpl implements PlayerToken {
        public boolean validate(PlayerToken token) { return this.equals(token); }
    }

    private class PlayerInfoImpl implements PlayerInfo {
        private Player player;
        private Pipe pipe;
        private int score = 0;

        private PlayerInfoImpl(Pipe pipe, Player player){
            this.pipe = pipe; this.player = player;
        }

        public int getScore() { return score; }
        public PipeBasic getPipe() { return pipe; }
        public BasePlayer getPlayer() { return player; }
    }



    private OnScoreEventCallback scoreChangeCallback = new OnScoreEventCallback() {
        @Override
        public void onScoreEvent(int newScore, PlayerToken token) {
            if (newScore >= 0) {
                players.get(token).score = newScore;
            }
        }
    };

    private Supplier<GameState> stateGetter;
    private BiMap<PlayerToken, PlayerInfoImpl> players;
    private BiMap<PlayerToken, Pipe> pipes;
    private GameShop gameShop;
    private MethodCallAuthenticator authenticator;
    private boolean stateChanged;



    public PlayerManager(GameConfig config, MethodCallAuthenticator authenticator) {
        this.authenticator = authenticator;
        Set<Class<? extends Player>> playerClasses = config.getPlayerClasses();
        int size = playerClasses.size();
        this.players = HashBiMap.create(size);
        this.pipes = HashBiMap.create(size);
    }



    // PlayerManagerBasic methods



    @Override
    public Set<PlayerInfo> getPlayerInfo() {
        return Collections.unmodifiableSet(players.values());
    }

    @Override
    public GameShop getGameShop() {
        return gameShop;
    }



    // PlayerManagerUpdatable methods



    @Override
    public Map<PlayerToken, Pipe> getPipesUpdatable() {
        return Collections.unmodifiableMap(pipes);
    }

    @Override
    public Map<PlayerToken, PlayerInfo> getPlayerTokenMap() {
        return Collections.unmodifiableMap(players);
    }



    // PlayerManagerPrivileged methods



    @Override
    public void update() {
        switch (stateGetter.get()) {
            case PLAYING:
                for (PlayerToken token : players.keySet()) {
                    PlayerInfoImpl info = players.get(token);
                    if (info.pipe.getHealth() > 0) {
                        info.player.act(token);
                        info.pipe.allowMovement(authenticator);
                    }
                }
                break;
            case WON:
            case LOST:
                if (stateChanged) {
                    stateChanged = false;
                    for (PlayerInfoImpl info : players.values()) {
                        info.player.deallocate();
                    }
                }
                break;
        }
    }

    @Override
    public OnScoreEventCallback getScoreChangeCallback() {
        return scoreChangeCallback;
    }



    // ResettableGameClass methods



    @Override
    public void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level) throws GameLevelInitializationException {
        pipes.clear(); players.clear();
        stateGetter = gameLevel::getState; stateChanged = true;
        Map<DummyEntrance, Class<? extends Player>> entranceToPlayerMap = level.getEntranceToPlayerMap();
        GameWorldUpdatable gameWorld = gameLevel.getGameWorld();
        for (DummyEntrance de : entranceToPlayerMap.keySet()) {
            Class<? extends Player> playerCls = entranceToPlayerMap.get(de);
            Player player;
            try {
                player = playerCls.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                throw new GameLevelInitializationException("Failed to initialize player: " + playerCls, e);
            }
            PlayerTokenImpl token = new PlayerTokenImpl();
            player.setPlayerToken(token);
            Pipe pipe = new Pipe(gameLevel.getActorInterface(),
                    gameWorld.getEntrancesUpdatable().get(de.getId()),
                    token
            );
            pipes.put(token, pipe);
            players.put(token, new PlayerInfoImpl(pipe, player));
        }
        this.gameShop = new GameShop(gameLevel, scoreChangeCallback);
        for (PlayerToken token : players.keySet()){
            PlayerInfoImpl playerInfo = players.get(token);
            playerInfo.player.initialize(gameLevel.getPlayerInterface(),
                    playerInfo.pipe, token
            );
        }
    }
}
