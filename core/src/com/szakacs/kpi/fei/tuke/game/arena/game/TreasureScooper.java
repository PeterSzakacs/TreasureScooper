package com.szakacs.kpi.fei.tuke.game.arena.game;

import com.szakacs.kpi.fei.tuke.game.arena.actors.ActorManagerImpl;
import com.szakacs.kpi.fei.tuke.game.arena.world.TreasureScooperWorld;
import com.szakacs.kpi.fei.tuke.game.enums.GameState;
import com.szakacs.kpi.fei.tuke.game.intrfc.callbacks.OnNuggetCollectedCallback;
import com.szakacs.kpi.fei.tuke.game.intrfc.Player;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.*;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.ActorManagerPrivileged;
import com.szakacs.kpi.fei.tuke.game.misc.proxies.PlayerGameProxy;

import java.util.Set;

/**
 * Created by developer on 25.1.2017.
 */
public class TreasureScooper implements GamePrivileged {

    private GameWorld gameWorld;
    private ActorManagerPrivileged actorManager;

    private Set<GameUpdater> gameUpdaters;
    private Player player;
    private GameState state;
    private int score;

    private OnNuggetCollectedCallback gameCallback = new OnNuggetCollectedCallback() {
        @Override
        public void onNuggetCollected(int nuggetValue) {
            TreasureScooper.this.score += nuggetValue;
        }
    };

    TreasureScooper(GameWorldInitializer initializer){
        this.gameWorld = new TreasureScooperWorld(initializer, this.gameCallback);
        this.actorManager = new ActorManagerImpl(this);
        this.score = 0;
    }

    @Override
    public Player getPlayer() {
        return this.player;
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
    public void setScore(int score) {
        this.score = score;
        if (score < 0)
            this.score = 0;
    }

    @Override
    public void update() {
        for (GameUpdater updater : this.gameUpdaters)
            updater.update();
        if (gameWorld.getNuggetCount() == 0)
            this.state = GameState.WON;
        if (actorManager.getPipe().getHealth() <= 0) {
            this.state = GameState.LOST;
            this.player.deallocate();
        }
    }

    @Override
    public void startNewGame(Set<GameUpdater> updaters, Player player){
        this.player = player;
        this.player.initialize(new PlayerGameProxy(this));
        this.state = GameState.PLAYING;
        this.gameUpdaters = updaters;
    }
}
