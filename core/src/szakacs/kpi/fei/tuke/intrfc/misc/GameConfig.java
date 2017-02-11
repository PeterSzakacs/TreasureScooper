package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 11.2.2017.
 */
public interface GameConfig {

    List<DummyLevel> getLevels();

    Map<ActorType, Set<Direction>> getActorToDirectionsMap();
}
