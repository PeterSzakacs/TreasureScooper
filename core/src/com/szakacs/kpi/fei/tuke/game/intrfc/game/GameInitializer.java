package com.szakacs.kpi.fei.tuke.game.intrfc.game;

import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.misc.levels.DummyLevel;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 3.2.2017.
 */
public interface GameInitializer {

    List<DummyLevel> getLevels();

    Map<ActorType, Set<Direction>> getActorToDirectionsMap();
}
