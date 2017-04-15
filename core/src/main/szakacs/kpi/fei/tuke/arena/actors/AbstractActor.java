package szakacs.kpi.fei.tuke.arena.actors;

import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.intrfc.misc.ActorRectangle;
import szakacs.kpi.fei.tuke.intrfc.misc.Rectangle;
import szakacs.kpi.fei.tuke.misc.GDXActorRectangle;

/**
 * Created by developer on 7.12.2016.
 */
public abstract class AbstractActor implements ActorPrivileged {

    // A view of the actor rectangle to display to other classes that are
    // not subclasses of AbstractActor (this view disables translate()).
    protected class ActorRectangleImpl implements Rectangle {
        protected final ActorRectangle rectangle;

        protected ActorRectangleImpl(TunnelCellUpdatable currentPosition, int width, int height){
            this.rectangle = new GDXActorRectangle(currentPosition, width, height);
        }

        public int getRectangleX() { return rectangle.getRectangleX(); }
        public int getRectangleY() { return rectangle.getRectangleY(); }
        public int getWidth() { return rectangle.getWidth(); }
        public int getHeight() { return rectangle.getHeight(); }
        public int getCenterX() { return rectangle.getCenterX(); }
        public int getCenterY() { return rectangle.getCenterY(); }
        public boolean overlaps(Rectangle other) { return rectangle.overlaps(other); }
    }

    private Direction dir;
    protected ActorRectangleImpl actorRectangle;
    protected final ActorType actorType;
    protected final ActorGameInterface gameInterface;
    protected final GameWorldBasic world;

    protected AbstractActor(ActorType at, ActorGameInterface gameInterface){
        if (gameInterface == null)
            throw new IllegalArgumentException("No valid game interface passed");
        this.gameInterface = gameInterface;
        this.world = gameInterface.getGameWorld();
        this.actorType = at;
    }

    protected AbstractActor(TunnelCellUpdatable currentPosition, ActorType type, Direction dir, ActorGameInterface gameInterface){
        this(type, gameInterface);
        this.dir = dir;
        this.setCurrentPosition(currentPosition);
    }

    @Override
    public int getX(){
        return actorRectangle.rectangle.getCenterX();
    }

    @Override
    public int getY(){
        return actorRectangle.rectangle.getCenterY();
    }

    @Override
    public TunnelCellUpdatable getCurrentPosition(){
        return actorRectangle.rectangle.getCurrentPosition();
    }

    @Override
    public Rectangle getActorRectangle() {
        return actorRectangle;
    }

    @Override
    public ActorType getType() {
        return this.actorType;
    }

    @Override
    public Direction getDirection() {
        return this.dir;
    }

    @Override
    public boolean intersects(ActorBasic other){
        return actorRectangle.overlaps(other.getActorRectangle());
    }

    protected void setDirection(Direction direction) {
        this.dir = direction;
    }

    protected void setCurrentPosition(TunnelCellUpdatable currentPosition){
        this.actorRectangle = new ActorRectangleImpl(currentPosition, world.getOffsetX(), world.getOffsetY());
    }

    protected void setCurrentPosition(TunnelCellUpdatable currentPosition, int x, int y){
        if (currentPosition.isWithinCell(x, y)){
            this.setCurrentPosition(currentPosition);
            int dx = x - actorRectangle.getRectangleX();
            int dy = y - actorRectangle.getRectangleY();
            actorRectangle.rectangle.translate(Direction.getDirectionByDeltas(dx, dy), dx, dx);
        }
    }
}
