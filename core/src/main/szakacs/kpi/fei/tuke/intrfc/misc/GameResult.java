package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.intrfc.player.Player;

/**
 * Created by developer on 6.5.2017.
 */
public interface GameResult {

    Class<? extends Player> getPlayerClass();

    int getScore();

    boolean hasFailed();
}
