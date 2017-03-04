package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.game.world.TreasureScooperWorld;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnItemBoughtCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnNuggetCollectedCallback;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;
import szakacs.kpi.fei.tuke.arena.PlayerGameProxy;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorld;
import szakacs.kpi.fei.tuke.misc.ConfigProcessingException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by developer on 25.1.2017.
 */
public class TreasureScooperLevel implements GameLevelPrivileged {

    private OnNuggetCollectedCallback gameCallback = new OnNuggetCollectedCallback() {
        @Override
        public void onNuggetCollected(int nuggetValue) {
            TreasureScooperLevel.this.score += nuggetValue;
        }
    };

    private OnItemBoughtCallback scoreChangeCallback = new OnItemBoughtCallback() {
        @Override
        public void onItemBought(int price) {
            TreasureScooperLevel.this.score -= price;
            if (score < 0) {
                score = 0;
            }
        }
    };

    private GameWorld gameWorld;
    private ActorManagerPrivileged actorManager;
    private GameShop gameShop;

    private Set<GameUpdater> gameUpdaters;
    private List<Player> players;
    private GameState state;
    private int score;

    public TreasureScooperLevel(DummyLevel level) throws ConfigProcessingException {
        this.gameWorld = new TreasureScooperWorld(level.getGameWorldPrototype(), this.gameCallback);
        this.actorManager = new ActorManager(this);
        this.score = 0;
        this.gameShop = new GameShop(actorManager.getActorGameProxy(), scoreChangeCallback);
        this.setPlayers(level);
        this.setGameUpdaters(level);
        this.state = GameState.PLAYING;
    }

    private void setGameUpdaters(DummyLevel level) throws ConfigProcessingException {
        this.gameUpdaters = new HashSet<>(3);
        for (Class<? extends GameUpdater> updaterCls : level.getGameUpdaterClasses()){
            try {
                gameUpdaters.add(updaterCls.getConstructor(GameLevelPrivileged.class).newInstance(this));
            } catch (NoSuchMethodException
                    | InvocationTargetException
                    | InstantiationException
                    | IllegalAccessException e) {
                throw new ConfigProcessingException("Failed to initialize game", e);
            }
        }
    }

    private void setPlayers(DummyLevel level) throws ConfigProcessingException {
        Map<DummyEntrance, Class<? extends Player>> entranceToPlayerMap = level.getEntranceToPlayerMap();
        this.players = new ArrayList<>(entranceToPlayerMap.size());
        for (DummyEntrance de : entranceToPlayerMap.keySet()) {
            try {
                Player player = entranceToPlayerMap.get(de).newInstance();
                players.add(player);
                actorManager.getPlayerToPipeMap().put(
                        player,
                        new Pipe(
                                actorManager.getActorGameProxy(),
                                gameWorld.getEntrances().get(de.getId()),
                                player
                        )
                );
            } catch (IllegalAccessException | InstantiationException e) {
                throw new ConfigProcessingException("Failed to initialize game", e);
            }
        }
        PlayerGameInterface playerInterface = new PlayerGameProxy(this);
        for (Player player : players) {
            player.initialize(playerInterface);
        }
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public GameState getState() {
        return this.state;
    }

    @Override
    public ActorManagerPrivileged getActorManager() {
        return this.actorManager;
    }

    @Override
    public GameWorld getGameWorld() {
        return this.gameWorld;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public GameShop getGameShop() {
        return this.gameShop;
    }

    @Override
    public void update() {
        for (Player player : players) {
            if (actorManager.getPipeOfPlayer(player).getHealth() > 0) {
                player.act();
            }
        }
        actorManager.update();
        for (GameUpdater updater : this.gameUpdaters)
            updater.update();
        if (gameWorld.getNuggetCount() == 0)
            state = GameState.WON;
        int healthCount = 0;
        for (Pipe pipe : actorManager.getPlayerToPipeMap().values()) {
            healthCount += pipe.getHealth();
        }
        if (healthCount <= 0) {
            state = GameState.LOST;
            for (Player player : players){
                player.deallocate();
            }
        }
    }
}
