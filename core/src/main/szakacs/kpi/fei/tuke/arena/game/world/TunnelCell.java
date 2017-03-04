package szakacs.kpi.fei.tuke.arena.game.world;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.GoldCollector;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorld;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by developer on 5.11.2016.
 */
public class TunnelCell {
    private int x;
    private int y;
    private HorizontalTunnel tunnel;
    private GameWorld world;
    private TunnelCellType tcType;
    private int NuggetValue;
    private Map<Direction, TunnelCell> fourDirections;
    private MethodCallAuthenticator authenticator;

    public TunnelCell(int x, int y, TunnelCellType tcType, HorizontalTunnel tunnel, GameWorld world, MethodCallAuthenticator authenticator) {
        this.x = x;
        this.y = y;
        this.tcType = tcType;
        this.tunnel = tunnel;
        this.world = world;
        if (tcType != TunnelCellType.INTERCONNECT)
            this.NuggetValue = 50;
        else
            this.NuggetValue = 0;
        this.fourDirections = new EnumMap<>(Direction.class);
        this.authenticator = authenticator;
    }

    @Override
    public String toString(){
        return super.toString() + ": " + tcType.name()
                + "\nX: " + this.x + " Y: " + this.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public HorizontalTunnel getTunnel(){
        return this.tunnel;
    }

    public void setAtDirection(Direction dir, TunnelCell pos, Object authToken) {
        if (authenticator.authenticate(authToken)) {
            fourDirections.put(dir, pos);
        }
    }

    public TunnelCell getCellAtDirection(Direction dir){
        return fourDirections.get(dir);
    }

    public TunnelCellType getCellType(){
        return this.tcType;
    }

    public boolean hasNugget(){
        return this.NuggetValue != 0;
    }

    public int collectNugget(GoldCollector collector) {
        if (collector.getCurrentPosition().equals(this)){
            int nuggetVal = this.NuggetValue;
            this.NuggetValue = 0;
            if (this.tunnel != null && nuggetVal != 0)
                this.tunnel.onNuggetCollected(nuggetVal);
            return nuggetVal;
        }
        return 0;
    }

    public boolean isWithinCell(int x, int y) {
        int absDx = Math.abs(x - this.x);
        int absDy = Math.abs(y - this.y);
        if (absDx >= world.getOffsetX() || absDy >= world.getOffsetY())
            return false;
        else
            return x + absDx * Direction.LEFT.getXStep() == this.x
                    && y + absDy * Direction.DOWN.getYStep() == this.y;
    }

    void setNuggetValue(int value){

    }
}