package szakacs.kpi.fei.tuke.misc;

import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.misc.ActorRectangle;

/**
 * Created by developer on 6.4.2017.
 */
public class GDXActorRectangle extends GDXRectangle implements ActorRectangle {

    private TunnelCell currentPosition;

    public GDXActorRectangle(TunnelCell currentPosition, int width, int height){
        super(currentPosition.getX(), currentPosition.getY(), width, height);
        this.currentPosition = currentPosition;
    }

    @Override
    public boolean overlaps(ActorRectangle other) {
        if (other == null)
            return false;
        if (other.getCurrentPosition() == currentPosition)
            return true;
        return x <= other.getRectangleX() + width && x + width >= other.getRectangleX()
                && y <= other.getRectangleY() + height && y + height >= other.getRectangleY();
    }

    @Override
    public void translate(Direction dir, int dxAbs, int dyAbs) {
        int dx = dir.getXStep() * dxAbs;
        int dy = dir.getYStep() * dyAbs;
        int x = this.x + dx;
        int y = this.y + dy;
        int centerX = this.centerX + dx;
        int centerY = this.centerY + dy;
        TunnelCell prev, next;
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
    public TunnelCell getCurrentPosition() {
        return currentPosition;
    }
}
