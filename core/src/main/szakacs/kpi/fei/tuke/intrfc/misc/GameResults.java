package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.intrfc.Player;

import java.util.List;
import java.util.Map;

/**
 * Created by developer on 16.3.2017.
 */
public interface GameResults {

    List<Map<Class<? extends Player>, Integer>> getLevelScores();

    Map<Class<? extends Player>, Integer> getTotalGameScores();
}
