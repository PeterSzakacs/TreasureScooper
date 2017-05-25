package szakacs.kpi.fei.tuke.arena.game;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeHead;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeSegment;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnPipeMovedCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnStackUpdatedCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.HorizontalTunnelBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic;
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
import szakacs.kpi.fei.tuke.misc.PlayerException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.*;
import java.util.function.Supplier;

/**
 * The managing class for all actors and their pipes.
 */
public class PlayerManager implements PlayerManagerPrivileged {

    private class PlayerTokenImpl implements PlayerToken {
        public boolean validate(PlayerToken token) { return this.equals(token); }
    }

    private class PlayerInfoImpl implements PlayerInfo {
        private Player player;
        private Pipe pipe;
        private int score = 0;
        private Throwable crashReason = null;

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

    private OnPipeMovedCallback onPipeMovedCallback = new OnPipeMovedCallback() {
        public void onPush(PipeHead head, PipeSegment pushed) {
            Set<ActorBasic> formerHeadPostion = positionPipesMap.get(
                    pushed.getCurrentPosition()
            );
            formerHeadPostion.remove(head);
            formerHeadPostion.add(pushed);
            positionPipesMap.get(head.getCurrentPosition()).add(head);
        }
        public void onPop(PipeHead head, PipeSegment popped) {
            Set<ActorBasic> newHeadPostion = positionPipesMap.get(
                    head.getCurrentPosition()
            );
            newHeadPostion.remove(popped);
            newHeadPostion.add(head);
            TunnelCellBasic formerHeadPosition = popped.getCurrentPosition()
                    .getCellAtDirection(popped.getDirection());
            positionPipesMap.get(formerHeadPosition).remove(head);
        }
    };



    private Supplier<GameState> stateGetter;
    private BiMap<PlayerToken, PlayerInfoImpl> players;
    private BiMap<PlayerToken, Pipe> pipes;
    private Map<PlayerToken, PlayerInfoImpl> unregisteredPlayers;
    private Map<TunnelCellBasic, Set<ActorBasic>> positionPipesMap;
    private GameShop gameShop;
    private MethodCallAuthenticator authenticator;
    private boolean stateChanged;



    public PlayerManager(GameConfig config, MethodCallAuthenticator authenticator) {
        this.authenticator = authenticator;
        Set<Class<? extends Player>> playerClasses = config.getPlayerClasses();
        int size = playerClasses.size();
        this.players = HashBiMap.create(size);
        this.unregisteredPlayers = new HashMap<>(size);
        this.pipes = HashBiMap.create(size);
        this.positionPipesMap = new HashMap<>();
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

    @Override
    public Map<TunnelCellBasic, Set<ActorBasic>> getPositionsToPipesMap() {
        return Collections.unmodifiableMap(positionPipesMap);
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
                handlePlaying();
                break;
            case WON:
            case LOST:
                handleEndOfLevel();
                break;
        }
    }

    @Override
    public OnScoreEventCallback getScoreChangeCallback() {
        return scoreChangeCallback;
    }

    @Override
    public Map<PlayerToken, PlayerInfo> getUnregisteredPlayers() {
        return Collections.unmodifiableMap(unregisteredPlayers);
    }

    @Override
    public Throwable getCrashReason(PlayerToken token) {
        if (unregisteredPlayers.containsKey(token)){
            return unregisteredPlayers.get(token).crashReason;
        } else {
            return null;
        }
    }


    // ResettableGameClass methods



    @Override
    public void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level) throws GameLevelInitializationException {
        players.clear(); unregisteredPlayers.clear();
        pipes.clear(); positionPipesMap.clear();
        stateGetter = gameLevel::getState;
        stateChanged = true;
        createPlayersAndPipes(gameLevel, level);
        initializePositionPipesMap(gameLevel.getGameWorld());
        this.gameShop = new GameShop(gameLevel, scoreChangeCallback);
        for (PlayerToken token : players.keySet()) {
            PlayerInfoImpl playerInfo = players.get(token);
            playerInfo.player.initialize(
                    gameLevel.getPlayerInterface(),
                    playerInfo.pipe,
                    token
            );
        }
    }



    // helper methods



    private void createPlayersAndPipes(GameLevelPrivileged gameLevel, DummyLevel level) throws GameLevelInitializationException {
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
                    onPipeMovedCallback,
                    token
            );
            pipes.put(token, pipe);
            players.put(token, new PlayerInfoImpl(pipe, player));
        }
    }

    private void initializePositionPipesMap(GameWorldBasic world){
        positionPipesMap.clear();
        Set<TunnelCellBasic> cellsSet = world.getCells();
        for (TunnelCellBasic cell : cellsSet) {
            positionPipesMap.put(cell, new HashSet<>(3));
        }
        for (Pipe pipe : pipes.values()){
            PipeHead head = pipe.getHead();
            positionPipesMap.get(head.getCurrentPosition()).add(head);
        }
    }

    private void handlePlaying(){
        Iterator<PlayerToken> it = players.keySet().iterator();
        PlayerToken token; PlayerInfoImpl info;
        while (it.hasNext()) {
            token = it.next(); info = players.get(token);
            if (info.pipe.getHealth() <= 0) {
                removePlayer(token, it);
            }
        }
        it = players.keySet().iterator();
        while (it.hasNext()) {
            token = it.next(); info = players.get(token);
            try {
                info.player.act(token);
            } catch (Exception e) {
                removePlayer(token, it);
                info.crashReason = e;
            }
            info.pipe.allowMovement(authenticator);
        }
    }

    private void handleEndOfLevel(){
        if (stateChanged) {
            stateChanged = false;
            for (PlayerToken token : pipes.keySet()) {
                PlayerInfoImpl info = players.get(token);
                info.player.deallocate();
            }
            if (stateGetter.get() == GameState.LOST) {
                unregisteredPlayers.putAll(players);
                players.clear();
                pipes.clear();
            }
        }
    }

    private void removePlayer(PlayerToken token, Iterator<PlayerToken> iterator){
        PlayerInfoImpl info = players.get(token);
        pipes.remove(token);
        positionPipesMap.get(info.pipe.getHead().getCurrentPosition())
                .remove(info.pipe.getHead());
        for (PipeSegment segment : info.pipe.getSegmentStack(token)){
            positionPipesMap.get(segment.getCurrentPosition())
                    .remove(segment);
        }
        unregisteredPlayers.put(token, info);
        iterator.remove();
    }
}
