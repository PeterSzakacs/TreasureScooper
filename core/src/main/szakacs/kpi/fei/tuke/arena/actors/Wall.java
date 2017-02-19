package szakacs.kpi.fei.tuke.arena.actors;

import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

import java.util.List;

/**
 * Created by developer on 31.12.2016.
 */
public class Wall extends AbstractActor {
    private TunnelCell neighbouringCell;

    public Wall(TunnelCell cell, ActorGameInterface gameInterface) {
        super(cell, ActorType.WALL, Direction.LEFT, gameInterface);
        this.neighbouringCell = cell.getCellAtDirection(Direction.RIGHT);
        this.disconnectCells(cell);
        System.out.println("Wall<init>()");
    }

    @Override
    public void act(ActorGameInterface gameInterface) {
        if (gameInterface != null && gameInterface.equals(super.gameInterface)) {
            List<Actor> intersecting = super.gameInterface.getActorsBySearchCriteria(actor ->
                    actor.getType() == ActorType.BULLET
                            && this.intersects(actor));
            if (!intersecting.isEmpty()) {
                super.gameInterface.unregisterActor(this);
                for (Actor actor : intersecting)
                    super.gameInterface.unregisterActor(actor);
            }
        }
    }

    @Override
    public boolean intersects(Actor actor){
        return super.intersects(actor)
                || neighbouringCell.equals(actor.getCurrentPosition());
    }

    private void disconnectCells(TunnelCell currentPosition){
        this.neighbouringCell.setAtDirection(Direction.LEFT, null, super.gameInterface.getGameWorld());
        currentPosition.setAtDirection(Direction.RIGHT, null, super.gameInterface.getGameWorld());
    }

    public void reconnectCells(){
        System.out.println("Wall.reconnectCells()");
        TunnelCell cell = super.getCurrentPosition();
        cell.setAtDirection(Direction.RIGHT, this.neighbouringCell, super.gameInterface.getGameWorld());
        this.neighbouringCell.setAtDirection(Direction.LEFT, cell, super.gameInterface.getGameWorld());
    }
}
