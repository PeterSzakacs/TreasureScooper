package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 15.4.2017.
 */
public interface GameWorldUpdatable extends GameWorldBasic {

    /**
     * Gets a set of all {@link HorizontalTunnelUpdatable} in the game world (no sorting order).
     *
     * @return all horizontal tunnels of the game world as an unmodifiable set.
     */
    Set<HorizontalTunnelUpdatable> getTunnelsUpdatable();

    Map<String, TunnelCellUpdatable> getEntrancesUpdatable();
}
