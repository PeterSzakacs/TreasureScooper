package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnScoreEventCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.misc.GameLevelInitializationException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.*;

/**
 * Created by developer on 7.3.2017.
 */
public class PlayerManager implements PlayerManagerPrivileged {

    private class PlayerTokenImpl implements PlayerToken {
        @Override
        public boolean validate(PlayerToken token) {
            return this.equals(token);
        }
    }

    private OnScoreEventCallback scoreChangeCallback = new OnScoreEventCallback() {
        @Override
        public void onScoreEvent(int newScore, Player affectedPlayer) {
            if (newScore >= 0) {
                scores.put(affectedPlayer, newScore);
            }
        }
    };

    private Set<Pipe> pipes;
    private Map<Player, Integer> scores;
    private Map<Player, PlayerTokenImpl> playerTokenMap;
    private GameShop gameShop;
    private MethodCallAuthenticator authenticator;

    public PlayerManager(GameConfig config, MethodCallAuthenticator authenticator) {
        this.authenticator = authenticator;
        Set<Class<? extends Player>> playerClasses = config.getPlayerClasses();
        int size = playerClasses.size();
        this.pipes = new HashSet<>(size);
        this.scores = new HashMap<>(size);
        this.playerTokenMap = new HashMap<>(size);
    }

    @Override
    public Set<PipeBasic> getPipes() {
        return Collections.unmodifiableSet(pipes);
    }

    @Override
    public Map<Player, Integer> getPlayersAndScores() {
        return Collections.unmodifiableMap(scores);
    }

    @Override
    public GameShop getGameShop() {
        return gameShop;
    }

    @Override
    public Set<Pipe> getPipesUpdatable() {
        return Collections.unmodifiableSet(pipes);
    }

    @Override
    public void update() {
        for (Pipe pipe : pipes){
            if (pipe.getHealth() > 0) {
                Player controller = pipe.getController();
                controller.act(playerTokenMap.get(controller));
                pipe.allowMovement(authenticator);
            }
        }
    }

    @Override
    public OnScoreEventCallback getScoreChangeCallback() {
        return scoreChangeCallback;
    }

    @Override
    public void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level) throws GameLevelInitializationException {
        pipes.clear();
        scores.clear();
        playerTokenMap.clear();
        this.gameShop = new GameShop(gameLevel, scoreChangeCallback);
        Map<DummyEntrance, Class<? extends Player>> entranceToPlayerMap = level.getEntranceToPlayerMap();
        GameWorldBasic gameWorld = gameLevel.getGameWorld();
        for (DummyEntrance de : entranceToPlayerMap.keySet()) {
            Class<? extends Player> playerCls = entranceToPlayerMap.get(de);
            Player player;
            try {
                player = entranceToPlayerMap.get(de).newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                throw new GameLevelInitializationException("Failed to initialize player: " + playerCls, e);
            }
            PlayerTokenImpl token = new PlayerTokenImpl();
            player.setPlayerToken(token);
            Pipe pipe = new Pipe(
                    gameLevel.getActorInterface(),
                    gameWorld.getEntrances().get(de.getId()),
                    player
            );
            pipes.add(pipe);
            scores.put(player, 0);
            playerTokenMap.put(player, token);
            player.initialize(gameLevel.getPlayerInterface(), pipe, token);
        }
    }
}
