package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.misc.configProcessors.levels.DummyLevel;

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
