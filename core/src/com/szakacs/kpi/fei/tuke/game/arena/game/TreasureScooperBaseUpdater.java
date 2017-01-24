package com.szakacs.kpi.fei.tuke.game.arena.game;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.enums.GameState;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.Player;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameUpdater;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by developer on 17.1.2017.
 */
public class TreasureScooperBaseUpdater implements GameUpdater {

    private TreasureScooper gameWorld;

    // Some variables cached to avoid calling getters on every iteration of the game loop
    private Player player;
    private Pipe pipe;
    private List<Actor> actors;
    private Map<Actor, Runnable> onDestroyActions;
    private Map<Actor, Integer> unregisteredActors;

    TreasureScooperBaseUpdater(TreasureScooper world){
        this.gameWorld = world;
        this.player = world.getPlayer();
        this.pipe = world.getPipe();
        this.actors = world.getActorsList();
        this.onDestroyActions = world.getOnDestroyActions();
        this.unregisteredActors = world.getUnregisteredActors();
    }

    @Override
    public void update(ManipulableGameInterface world) {
        if (world.getState() == GameState.PLAYING) {
            this.player.act();
            this.pipe.allowMovement(world);
        }
        for (Actor actor : this.actors) {
            actor.act(world);
        }
        for (Iterator<Actor> actorIt = this.unregisteredActors.keySet().iterator(); actorIt.hasNext(); ) {
            Actor unregistered = actorIt.next();
            Integer counter = this.unregisteredActors.get(unregistered);
            if (counter == 0) {
                this.actors.remove(unregistered);
            } else if (counter > 3)
                actorIt.remove();
            else
                this.unregisteredActors.put(unregistered, ++counter);
        }
        //this.unregisteredActors.clear();
        if (world.getRemainingNuggetsCount() == 0)
            gameWorld.setState(GameState.WON);
        if (this.pipe.getHealth() <= 0) {
            gameWorld.setState(GameState.LOST);
            this.player.deallocate();
        }
    }
}
