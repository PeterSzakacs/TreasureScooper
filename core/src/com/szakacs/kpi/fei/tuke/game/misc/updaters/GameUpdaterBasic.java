package com.szakacs.kpi.fei.tuke.game.misc.updaters;

import com.szakacs.kpi.fei.tuke.game.enums.GameState;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GamePrivileged;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by developer on 17.1.2017.
 *
 * Updater class to manage the basic aspects of updating the game,
 * specifically calling the act() methods of the actors and player,
 * as well as determining for how long to keep actors that have been
 * removed from the game world (unregistered actors, they are kept
 * for a while afterwards, for possible rendering applied to them).
 */
public class GameUpdaterBasic extends AbstractGameUpdater {

    // Some variables cached to avoid calling getters on every iteration of the game loop
    private List<Actor> actors;
    private Map<Actor, Integer> unregisteredActors;

    public GameUpdaterBasic(GamePrivileged game){
        super(game);
        this.actors = super.actorManager.getActors();
        this.unregisteredActors = super.actorManager.getUnregisteredActors();
    }

    @Override
    public void update() {
        if (game.getState() == GameState.PLAYING) {
            game.getPlayer().act();
            actorManager.getPipe().allowMovement(actorManager.getActorGameProxy());
        }
        for (Actor actor : this.actors) {
            actor.act(actorManager.getActorGameProxy());
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
        /*if (world.getRemainingNuggetsCount() == 0)
            gameWorld.setState(GameState.WON);
        if (this.pipe.getHealth() <= 0) {
            gameWorld.setState(GameState.LOST);
            this.player.deallocate();
        }*/
    }
}
