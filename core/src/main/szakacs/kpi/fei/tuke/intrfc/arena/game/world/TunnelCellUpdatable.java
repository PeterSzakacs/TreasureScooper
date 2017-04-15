package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.enums.Direction;

/**
 * Created by developer on 14.4.2017.
 */
public interface TunnelCellUpdatable extends TunnelCellBasic {

    void setAtDirection(Direction dir, TunnelCellUpdatable pos, Object authToken);

    void collectNugget(Pipe pipe);

    TunnelCellUpdatable getCellAtDirection(Direction dir);

    HorizontalTunnelUpdatable getTunnel();
}
