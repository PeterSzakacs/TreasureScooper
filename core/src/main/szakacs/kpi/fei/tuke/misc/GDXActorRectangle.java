package szakacs.kpi.fei.tuke.misc;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;
import szakacs.kpi.fei.tuke.intrfc.misc.ActorRectangle;

/**
 * An extension to the base {@link GDXRectangle} descriptor of a game object
 * with added functionality for use by actors.
 */
public class GDXActorRectangle extends GDXRectangle implements ActorRectangle {

    private TunnelCellUpdatable currentPosition;

    public GDXActorRectangle(TunnelCellUpdatable currentPosition, int width, int height){
        super(currentPosition.getX(), currentPosition.getY(), width, height);
        this.currentPosition = currentPosition;
    }

    @Override
    public void translate(Direction dir, int dxAbs, int dyAbs) {
        int dx = dir.getXStep() * dxAbs;
        int dy = dir.getYStep() * dyAbs;
        int x = this.x + dx;
        int y = this.y + dy;
        int centerX = this.centerX + dx;
        int centerY = this.centerY + dy;
        TunnelCellUpdatable prev, next;
        prev = next = getCurrentPosition();
        do {
            if (next.isWithinCell(x, y)) {
                // successfully found the new position
                this.x = x;
                this.y = y;
                this.centerX = centerX;
                this.centerY = centerY;
                this.currentPosition = next;
                break;
            }
            prev = next;
            next = next.getCellAtDirection(dir);
        } while ( next != null );
        if (next == null){
            //reached the end of a tunnel in a given direction
            this.x = prev.getX();
            this.y = prev.getY();
            this.centerX = prev.getX() + width/2;
            this.centerY = prev.getY() + height/2;
            this.currentPosition = prev;
        }
    }

    @Override
    public TunnelCellUpdatable getCurrentPosition() {
        return currentPosition;
    }
}
