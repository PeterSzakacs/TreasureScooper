package szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.misc.ActorInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 29.4.2017.
 */
public class ActorInfoImpl implements ActorInfo {

    private Map<String, Object> properties;
    private Set<Direction> directions;

    public ActorInfoImpl(){
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
