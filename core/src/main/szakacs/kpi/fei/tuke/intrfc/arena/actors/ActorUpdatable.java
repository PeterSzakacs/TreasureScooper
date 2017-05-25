package szakacs.kpi.fei.tuke.intrfc.arena.actors;

import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;

/**
 * The interface of game actors class,
 * that is exposed to other game actors
 * and all privileged classes.
 */
public interface ActorUpdatable extends ActorBasic {

    /**
     * Gets the actor's current position as a TunnelCellUpdatable
     * type of object.
     *
     * @return the actor's current position.
     */
    TunnelCellUpdatable getCurrentPosition();
}
