package szakacs.kpi.fei.tuke.arena.actors;

import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

import java.util.Set;

/**
 * A dynamically generated wall. Upon creation disconnects
 * two adjacent cells by severing references between them.
 */
public class Wall extends AbstractActor {

    private TunnelCellUpdatable neighbouringCell;

    public Wall(TunnelCellUpdatable cell, ActorGameInterface gameInterface) {
        super(cell, ActorType.WALL, Direction.LEFT, gameInterface);
        this.actorRectangle = new ActorRectangleImpl(cell, 2*world.getOffsetX(), world.getOffsetY());
        this.neighbouringCell = cell.getCellAtDirection(Direction.RIGHT);
        this.disconnectCells(cell);
        //System.out.println("Wall<init>()");
    }

    /**
     * Check if a bullet intersects the wall at this position,
     * and if so, unregister it and this wall as well.
     *
     * @param authToken An authentication token to verify the caller
     */
    @Override
    public void act(Object authToken) {
        if (gameInterface.getAuthenticator().authenticate(authToken)) {
            Set<ActorBasic> intersecting = gameInterface.getActorsBySearchCriteria(actor ->
                    actor.getType() == ActorType.BULLET
                            && this.intersects(actor));
            if (!intersecting.isEmpty()) {
                gameInterface.unregisterActor(this);
                for (ActorBasic actor : intersecting)
                    gameInterface.unregisterActor(actor);
            }
        }
    }

    /**
     * Modified implementation of base method from {@link AbstractActor}.
     * First checks if an actor is located at the neighboring position
     * (left side of the wall) and only then checks using the base method.
     *
     * @param actor the actor to check if it intersects this actor.
     * @return boolean true if actor is at the left or right side of this wall | false otherwise.
     */
    @Override
    public boolean intersects(ActorBasic actor){
        return neighbouringCell.equals(actor.getCurrentPosition()) || super.intersects(actor);
    }

    private void disconnectCells(TunnelCellUpdatable currentPosition){
        this.neighbouringCell.setAtDirection(Direction.LEFT, null, gameInterface.getAuthenticator());
        currentPosition.setAtDirection(Direction.RIGHT, null, gameInterface.getAuthenticator());
    }

    public void reconnectCells(){
        //System.out.println("Wall.reconnectCells()");
        TunnelCellUpdatable cell = super.getCurrentPosition();
        cell.setAtDirection(Direction.RIGHT, this.neighbouringCell, gameInterface.getAuthenticator());
        this.neighbouringCell.setAtDirection(Direction.LEFT, cell, gameInterface.getAuthenticator());
    }
}
