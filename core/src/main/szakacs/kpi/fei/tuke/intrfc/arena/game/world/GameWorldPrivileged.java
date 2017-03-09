package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.ResettableGameClass;

/**
 * Created by developer on 17.2.2017.
 */
public interface GameWorldPrivileged extends GameWorldQueryable, ResettableGameClass {

    MethodCallAuthenticator getAuthenticator();

    void onNuggetCollected(Pipe pipe, int val);
}
