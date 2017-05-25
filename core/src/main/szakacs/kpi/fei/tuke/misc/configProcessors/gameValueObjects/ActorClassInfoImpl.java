package szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.misc.ActorClassInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The implementation of the base {@link ActorClassInfo} interface.
 */
public class ActorClassInfoImpl implements ActorClassInfo {

    private Map<String, Object> properties;
    private Set<Direction> directions;

    public ActorClassInfoImpl(){
        this.properties = new HashMap<>(3);
        this.directions = new HashSet<>(Direction.values().length);
    }

    @Override
    public Set<Direction> getActorDirections() {
        return directions;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }
}
