package szakacs.kpi.fei.tuke.arena.actors;

import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

/**
 * Created by developer on 31.12.2016.
 */
public abstract class AbstractMoveableActor extends AbstractActor {

    private boolean boundReached = false;

    protected AbstractMoveableActor(ActorType at, ActorGameInterface gameInterface) {
        super(at, gameInterface);
    }

    protected AbstractMoveableActor(TunnelCellUpdatable currentPosition, ActorType type, Direction dir, ActorGameInterface gameInterface) {
        super(currentPosition, type, dir, gameInterface);
    }

    @Override
    protected void setDirection(Direction direction){
        super.setDirection(direction);
        this.boundReached = false;
    }

    protected void move(int dxAbs, int dyAbs, Direction dir){
        if (getDirection() != dir) {
            setDirection(dir);
        }
        actorRectangle.rectangle.translate(dir, dxAbs, dyAbs);
        if (actorRectangle.rectangle.getCurrentPosition().getCellAtDirection(dir) == null)
            this.boundReached = true;
    }

    protected void move(int dx, int dy){
        this.move(Math.abs(dx), Math.abs(dy), Direction.getDirectionByDeltas(dx, dy));
    }

    protected boolean boundReached(){
        return this.boundReached;
    }
}
