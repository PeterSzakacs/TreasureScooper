package szakacs.kpi.fei.tuke.arena.actors;

import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

/**
 * Created by developer on 31.12.2016.
 */
public abstract class AbstractMoveableActor extends AbstractActor {

    private boolean boundReached;

    protected AbstractMoveableActor(ActorType at, ActorGameInterface gameInterface) {
        super(at, gameInterface);
    }

    protected AbstractMoveableActor(TunnelCell currentPosition, ActorType type, Direction dir, ActorGameInterface gameInterface) {
        super(currentPosition, type, dir, gameInterface);
    }

    @Override
    protected void setDirection(Direction direction){
        super.setDirection(direction);
        this.boundReached = false;
    }

    protected void move(int dxAbs, int dyAbs, Direction dir){
        if (getDirection() != dir) {
            this.boundReached = false;
            setDirection(dir);
        }
        int x = getX() + dir.getXStep() * dxAbs;
        int y = getY() + dir.getYStep() * dyAbs;
        TunnelCell prev, next;
        prev = next = getCurrentPosition();
        do {
            if (next.isWithinCell(x, y)) {
                // successfully found the new position
                setCurrentPosition(next, x, y);
                break;
            }
            prev = next;
            next = next.getCellAtDirection(dir);
        } while ( next != null );
        if (next == null) {
            //reached the end of a tunnel in a given direction
            super.setCurrentPosition(prev);
            this.boundReached = true;
        }
    }

    protected void move(int dx, int dy){
        this.move(Math.abs(dx), Math.abs(dy), Direction.getDirectionByDeltas(dx, dy));
    }

    protected boolean boundReached(){
        return this.boundReached;
    }
}
