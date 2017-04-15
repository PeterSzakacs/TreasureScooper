package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by developer on 14.4.2017.
 */
public interface HorizontalTunnelUpdatable extends HorizontalTunnelBasic {

    Set<TunnelCellUpdatable> getUpdatableCells();

    Set<TunnelCellUpdatable> getUpdatableCellsBySearchCriteria(Predicate<TunnelCellUpdatable> criteria);
}
