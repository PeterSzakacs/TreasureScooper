package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.enums.Direction;

import java.util.Map;
import java.util.Set;

/**
 * A value object containing some information used for rendering actors of a particular class.
 */
public interface ActorClassInfo {

    /**
     * Returns the set of directions this particular class of actor can face in.
     *
     * @return the set of directions this particular class of actor can face in.
     */
    Set<Direction> getActorDirections();

    /**
     * Returns a map of string identifiers to additional rendering properties
     * of this particular actor class.
     *
     * @return a map of string identifiers to additional rendering properties
     * of this particular actor class.
     */
    Map<String, Object> getProperties();
}
