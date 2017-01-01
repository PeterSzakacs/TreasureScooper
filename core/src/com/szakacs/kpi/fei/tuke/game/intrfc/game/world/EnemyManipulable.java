package com.szakacs.kpi.fei.tuke.game.intrfc.game.world;

import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;

/**
 * Created by developer on 2.12.2016.
 */
public interface EnemyManipulable {

    void registerActor(Actor actor);

    void unregisterActor(Actor actor);
}
