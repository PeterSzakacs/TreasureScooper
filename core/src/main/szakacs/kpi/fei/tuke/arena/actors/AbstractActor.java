package szakacs.kpi.fei.tuke.arena.actors;

import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

/**
 * Created by developer on 7.12.2016.
 */
public abstract class AbstractActor implements ActorPrivileged {

    private int x;
    private int y;
    private TunnelCell currentPosition;
    private Direction dir;
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

    protected AbstractActor(TunnelCell currentPosition, ActorType type, Direction dir, ActorGameInterface gameInterface){
        this(type, gameInterface);
        this.dir = dir;
        this.setCurrentPosition(currentPosition);
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
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
        return other != null && other.getCurrentPosition() == this.currentPosition;
    }

    @Override
    public TunnelCell getCurrentPosition(){
        return this.currentPosition;
    }

    protected void setDirection(Direction direction) {
        this.dir = direction;
    }

    protected void setCurrentPosition(TunnelCell currentPosition){
        this.currentPosition = currentPosition;
        this.x = currentPosition.getX();
        this.y = currentPosition.getY();
    }

    protected void setCurrentPosition(TunnelCell currentPosition, int x, int y){
        if (currentPosition.isWithinCell(x, y)){
            this.currentPosition = currentPosition;
            this.x = x;
            this.y = y;
        }
    }
}
