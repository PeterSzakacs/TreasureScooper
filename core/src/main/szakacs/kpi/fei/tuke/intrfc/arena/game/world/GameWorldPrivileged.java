package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.ResettableGameClass;

/**
 *
 */
public interface GameWorldPrivileged extends GameWorldBasic, ResettableGameClass {

    MethodCallAuthenticator getAuthenticator();

    void onNuggetCollected(Pipe pipe, int val);
}
