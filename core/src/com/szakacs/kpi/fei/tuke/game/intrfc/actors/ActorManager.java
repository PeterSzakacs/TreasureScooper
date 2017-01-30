package com.szakacs.kpi.fei.tuke.game.intrfc.actors;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by developer on 25.1.2017.
 */
public interface ActorManager {

    Pipe getPipe();

    List<Actor> getActors();

    List<Actor> getActorsBySearchCriteria(Predicate<Actor> predicate);
}
