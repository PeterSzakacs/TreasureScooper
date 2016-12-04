package com.szakacs.kpi.fei.tuke.game.intrfc.game;

import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.ManipulableActor;

/**
 * Created by developer on 2.12.2016.
 */
public interface EnemyManipulable {

    void registerActor(ManipulableActor actor);

    void unregisterActor(ManipulableActor actor);
}
