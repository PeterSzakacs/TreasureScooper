package com.szakacs.kpi.fei.tuke.game.intrfc.game;

import com.szakacs.kpi.fei.tuke.game.arena.Enemy;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by developer on 1.12.2016.
 */
public interface EnemyQueryable {

    List<Actor> getActors();

    List<Actor> getActorsBySearchCriteria(Predicate<Actor> predicate);

    boolean intersects(Actor actorA, Actor actorB);
}
