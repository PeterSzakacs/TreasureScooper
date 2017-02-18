package szakacs.kpi.fei.tuke.intrfc.game.world;

import szakacs.kpi.fei.tuke.game.world.TunnelCell;

import java.util.Map;

/**
 * Created by developer on 17.2.2017.
 */
public interface GameWorldPrivileged {

    Map<String, TunnelCell> getEntrancesWithIds();
}
