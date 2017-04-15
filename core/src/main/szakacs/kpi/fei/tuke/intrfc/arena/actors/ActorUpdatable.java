package szakacs.kpi.fei.tuke.intrfc.arena.actors;

import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;

/**
 * Created by developer on 14.4.2017.
 */
public interface ActorUpdatable extends ActorBasic {

    TunnelCellUpdatable getCurrentPosition();

    /**
     * Returns an actorRectangle object which contains actor properties related to position.
     *
     * @return an unmodifiable view of the actor's rectangle object.
     */
    //ActorRectangle getActorRectangle();
}
