package szakacs.kpi.fei.tuke.intrfc.game;

import szakacs.kpi.fei.tuke.game.world.HorizontalTunnel;
import szakacs.kpi.fei.tuke.game.world.TunnelCell;

import java.util.List;

/**
 * Created by developer on 25.1.2017.
 */
public interface GameWorld {

    int getWidth();

    int getHeight();

    int getOffsetX();

    int getOffsetY();

    int getNuggetCount();

    TunnelCell getRootCell();

    List<HorizontalTunnel> getTunnels();
}
