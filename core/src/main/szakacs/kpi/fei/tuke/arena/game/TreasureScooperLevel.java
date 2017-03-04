package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.arena.game.world.TreasureScooperWorld;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnItemBoughtCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnNuggetCollectedCallback;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameStateTester;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorld;
import szakacs.kpi.fei.tuke.misc.ConfigProcessingException;
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
    private GameStateTester stateTester;
    private GameState state;
    private int score;

    public TreasureScooperLevel(DummyLevel level) throws ConfigProcessingException {
        this.gameWorld = new TreasureScooperWorld(level.getGameWorldPrototype(), this.gameCallback);
        this.actorManager = new ActorManager(this, level);
        this.score = 0;
        this.gameShop = new GameShop(actorManager.getActorGameProxy(), scoreChangeCallback);
        this.setGameUpdaters(level);
        this.stateTester = new GameStateTesterBasic(this);
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

    // GameLevelQueryable methods (only queries)

    @Override
    public GameState getState() {
        return this.state;
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

    // GameLevelPrivileged methods (access to all actor manager functions and updating the whole game).

    @Override
    public ActorManagerPrivileged getActorManager() {
        return this.actorManager;
    }

    @Override
    public void update() {
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
                    for (Player player : actorManager.getPlayerToPipeMap().keySet()) {
                        player.deallocate();
                    }
                    break;
            }
        }
    }
}
