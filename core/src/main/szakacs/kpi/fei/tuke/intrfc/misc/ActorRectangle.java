package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;

/**
 * An extension of the base {@link Rectangle} interface
 * providing added actor-specific functionality.
 */
public interface ActorRectangle extends Rectangle {

    /**
     * <p>Changes this rectangle's centerX, centerY, rectangleX and rectangleY
     * positions and the tunnel cell if necessary, by changing these positions
     * according to the following rules:</p>
     *
     * {@code
     * int dx += dxAbs*dir.getXStep();
     * int dy += dyAbs*dir.getYStep();
     * centerX += dx; centerY += dy;
     * rectangleX += dx, rectangleY += dy;
     * }
     *
     * <p>Note that even if both coordinate shifts passed are nonzero,
     * constraints placed upon movement by the xStep and yStep attributes
     * of each {@link Direction} value will cause the shift in coordinates
     * to be restricted to only the vertical or horizontal coordinates.</p>
     *
     * <p>If the new position is outside any tunnel cell, making it impossible
     * to move there, the current tunnel cell is set to the last tunnel cell
     * in the given direction, with coordinates set appropriately.</p>
     *
     * @param dxAbs the absolute value of the shift in horizontal coordinates
     * @param dyAbs the absolute value of the shift in vertical coordinates
     * @param dir the direction of movement.
     */
    void translate(Direction dir, int dxAbs, int dyAbs);

    /**
     * Returns the tunnel cell where this rectangle's centerX
     * and centerY coordinates are located in.
     *
     * @return the tunnel cell containing the centerX and centerY
     *         coordinates of this rectangle.
     */
    TunnelCellUpdatable getCurrentPosition();
}
