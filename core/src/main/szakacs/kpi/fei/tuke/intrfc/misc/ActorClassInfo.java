package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.enums.Direction;

import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 29.4.2017.
 */
public interface ActorClassInfo {

    Set<Direction> getActorDirections();

    Map<String, Object> getProperties();
}
