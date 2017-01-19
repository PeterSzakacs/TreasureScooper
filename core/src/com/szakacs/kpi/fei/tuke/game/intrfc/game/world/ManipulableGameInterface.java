package com.szakacs.kpi.fei.tuke.game.intrfc.game.world;

import com.szakacs.kpi.fei.tuke.game.arena.tunnels.Wall;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;

/**
 * Created by developer on 2.12.2016.
 */
public interface ManipulableGameInterface extends QueryableGameInterface {

    void update();

    void onNuggetCollected();

    void setOnDestroy(Actor actor, Runnable action);

    void registerActor(Actor actor);

    void unregisterActor(Actor actor);
}
