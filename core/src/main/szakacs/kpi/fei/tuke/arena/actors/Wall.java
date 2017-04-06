package szakacs.kpi.fei.tuke.arena.actors;

import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

import java.util.Set;

/**
 * Created by developer on 31.12.2016.
 */
public class Wall extends AbstractActor {
    private TunnelCell neighbouringCell;

    public Wall(TunnelCell cell, ActorGameInterface gameInterface) {
        super(cell, ActorType.WALL, Direction.LEFT, gameInterface);
        this.neighbouringCell = cell.getCellAtDirection(Direction.LEFT);
        this.disconnectCells(cell);
        System.out.println("Wall<init>()");
    }

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

    @Override
    public boolean intersects(ActorBasic actor){
        return super.intersects(actor)
                || neighbouringCell.equals(actor.getCurrentPosition());
    }

    private void disconnectCells(TunnelCell currentPosition){
        this.neighbouringCell.setAtDirection(Direction.RIGHT, null, gameInterface.getAuthenticator());
        currentPosition.setAtDirection(Direction.LEFT, null, gameInterface.getAuthenticator());
    }

    public void reconnectCells(){
        System.out.println("Wall.reconnectCells()");
        TunnelCell cell = super.getCurrentPosition();
        cell.setAtDirection(Direction.LEFT, this.neighbouringCell, gameInterface.getAuthenticator());
        this.neighbouringCell.setAtDirection(Direction.RIGHT, cell, gameInterface.getAuthenticator());
    }
}
