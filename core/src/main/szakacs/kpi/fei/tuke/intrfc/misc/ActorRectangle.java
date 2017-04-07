package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.Direction;

/**
 * Created by developer on 6.4.2017.
 */
public interface ActorRectangle extends Rectangle {

    boolean overlaps(ActorRectangle other);

    void translate(Direction dir, int dxAbs, int dyAbs);

    TunnelCell getCurrentPosition();
}
