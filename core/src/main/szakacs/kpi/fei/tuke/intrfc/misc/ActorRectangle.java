package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;

/**
 * Created by developer on 6.4.2017.
 */
public interface ActorRectangle extends Rectangle {

    void translate(Direction dir, int dxAbs, int dyAbs);

    TunnelCellUpdatable getCurrentPosition();
}
