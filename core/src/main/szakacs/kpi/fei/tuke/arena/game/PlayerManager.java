package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnScoreEventCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldQueryable;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.misc.ConfigProcessingException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.*;

/**
 * Created by developer on 7.3.2017.
 */
public class PlayerManager implements PlayerManagerPrivileged {

    private OnScoreEventCallback scoreChangeCallback = new OnScoreEventCallback() {
        @Override
        public void onScoreEvent(int newScore, Player affectedPlayer) {
            if (newScore >= 0) {
                scores.put(affectedPlayer, newScore);
            }
        }
    };

    private Player[] playerInstances;
    private List<Pipe> pipes;
    private Map<Player, Integer> scores;
    private GameShop gameShop;
    private MethodCallAuthenticator authenticator;

    public PlayerManager(GameConfig config, MethodCallAuthenticator authenticator) throws ConfigProcessingException {
        this.authenticator = authenticator;
        Set<Class<? extends Player>> playerClasses = config.getPlayerClasses();
        int size = playerClasses.size();
        this.playerInstances = new Player[size];
        this.pipes = new ArrayList<>(size);
        this.scores = new HashMap<>(size);
        int idx = 0;
        for (Class<? extends Player> playerCls : playerClasses) {
            try {
                playerInstances[idx++] = playerCls.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                throw new ConfigProcessingException("Failed to initialize player: " + playerCls, e);
            }
        }
    }

    @Override
    public List<Pipe> getPipes() {
        return Collections.unmodifiableList(pipes);
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
    public void update() {
        for (Pipe pipe : pipes){
            if (pipe.getHealth() > 0) {
                pipe.getController().act();
                pipe.allowMovement(authenticator);
            }
        }
    }

    @Override
    public OnScoreEventCallback getScoreChangeCallback() {
        return scoreChangeCallback;
    }

    @Override
    public void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level){
        pipes.clear();
        scores.clear();
        this.gameShop = new GameShop(gameLevel, scoreChangeCallback);
        Map<DummyEntrance, Class<? extends Player>> entranceToPlayerMap = level.getEntranceToPlayerMap();
        GameWorldQueryable gameWorld = gameLevel.getGameWorld();
        for (DummyEntrance de : entranceToPlayerMap.keySet()) {
            for (Player player : playerInstances){
                if (player.getClass().equals(entranceToPlayerMap.get(de))){
                    Pipe pipe = new Pipe(
                            gameLevel.getActorInterface(),
                            gameWorld.getEntrances().get(de.getId()),
                            player
                    );
                    pipes.add(pipe);
                    scores.put(player, 0);
                    player.initialize(gameLevel.getPlayerInterface(), pipe);
                    break;
                }
            }
        }
    }
}
