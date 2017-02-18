package szakacs.kpi.fei.tuke.game;

import szakacs.kpi.fei.tuke.arena.pipe.Pipe;
import szakacs.kpi.fei.tuke.game.world.TreasureScooperWorld;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnItemBoughtCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnNuggetCollectedCallback;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;
import szakacs.kpi.fei.tuke.intrfc.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.misc.proxies.PlayerGameInterface;
import szakacs.kpi.fei.tuke.misc.proxies.PlayerGameProxy;
import szakacs.kpi.fei.tuke.intrfc.game.world.GameWorld;

import java.util.List;
import java.util.Set;

/**
 * Created by developer on 25.1.2017.
 */
public class TreasureScooper implements GameLevelPrivileged {

    private OnNuggetCollectedCallback gameCallback = new OnNuggetCollectedCallback() {
        @Override
        public void onNuggetCollected(int nuggetValue) {
            TreasureScooper.this.score += nuggetValue;
        }
    };

    private OnItemBoughtCallback scoreChangeCallback = new OnItemBoughtCallback() {
        @Override
        public void onItemBought(int price) {
            TreasureScooper.this.score -= price;
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

    TreasureScooper(GameWorldPrototype initializer){
        this.gameWorld = new TreasureScooperWorld(initializer, this.gameCallback);
        this.actorManager = new ActorManager(this);
        this.score = 0;
        this.gameShop = new GameShop(actorManager.getActorGameProxy(), scoreChangeCallback);
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

    @Override
    public void startNewGame(Set<GameUpdater> updaters, List<Player> players){
        this.players = players;
        PlayerGameInterface playerInterface = new PlayerGameProxy(this);
        for (Player player : players) {
            player.initialize(playerInterface);
        }
        this.state = GameState.PLAYING;
        this.gameUpdaters = updaters;
    }
}
