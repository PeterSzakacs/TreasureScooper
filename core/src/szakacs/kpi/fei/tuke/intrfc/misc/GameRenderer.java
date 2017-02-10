package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.intrfc.game.GamePrivileged;

/**
 * Created by developer on 24.1.2017.
 */
public interface GameRenderer {
    void render();

    void dispose();

    void reset(GamePrivileged game);
}
