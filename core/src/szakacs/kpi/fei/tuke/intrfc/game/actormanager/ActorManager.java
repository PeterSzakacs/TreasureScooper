package szakacs.kpi.fei.tuke.intrfc.game.actormanager;

import szakacs.kpi.fei.tuke.arena.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.arena.Actor;

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
