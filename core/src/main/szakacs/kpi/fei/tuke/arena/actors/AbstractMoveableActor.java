package szakacs.kpi.fei.tuke.arena.actors;

import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

/**
 * An extension to the base {@link AbstractActor} class
 * providing helper functionality for moving actors.
 */
public abstract class AbstractMoveableActor extends AbstractActor {

    private boolean boundReached = false;

    protected AbstractMoveableActor(ActorType at, ActorGameInterface gameInterface) {
        super(at, gameInterface);
    }

    protected AbstractMoveableActor(TunnelCellUpdatable currentPosition, ActorType type, Direction dir, ActorGameInterface gameInterface) {
        super(currentPosition, type, dir, gameInterface);
    }

    /**
     * After updating the actor's orientation, resets the result
     * returned by {@code boundReached()} to false, as it is assumed
     * the actor has not reached a boundary in the new direction.
     *
     * @param direction the new direction the actor should be facing in.
     */
    @Override
    protected void setDirection(Direction direction){
        super.setDirection(direction);
        this.boundReached = false;
    }

    /**
     * <p>Moves the actor and updates the tunnel cell it is located in
     * by calling ActorRectangle.overlaps().</p>
     *
     * <p>If the direction value passed is different from the current
     * value returned by {@code getDirection()}, it also sets it and
     * checks to see if a bound has been reached in the given direction.</p>
     *
     * @param dxAbs the absolute value of the shift in horizontal coordinates
     * @param dyAbs the absolute value of the shift in vertical coordinates
     * @param dir the direction of movement.
     */
    protected void move(int dxAbs, int dyAbs, Direction dir){
        if (getDirection() != dir) {
            setDirection(dir);
        }
        actorRectangle.rectangle.translate(dir, dxAbs, dyAbs);
        if (actorRectangle.rectangle.getCurrentPosition().getCellAtDirection(dir) == null)
            this.boundReached = true;
    }

    /**
     * Same as {@link AbstractMoveableActor#move(int, int, Direction)} except
     * that the direction of movement is calculated from the values of dx and
     * dy. Note that unlike the former, this method throws an exception if both
     * values are nonzero.
     *
     * @param dx the signed change of the horizontal coordinate.
     * @param dy the signed change of the vertical coordinate.
     */
    protected void move(int dx, int dy){
        this.move(Math.abs(dx), Math.abs(dy), Direction.getDirectionByDeltas(dx, dy));
    }

    /**
     * Checks if the end of a tunnel has been reached in the given direction.
     *
     * @return boolean true if the end of a tunnel in the current direction
     *         was reached | false otherwise.
     */
    protected boolean boundReached(){
        return this.boundReached;
    }
}
