package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnScoreEventCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.ResettableGameClass;

import java.util.Map;

/**
 * Created by developer on 17.2.2017.
 */
public interface GameWorldPrivileged extends GameWorld, ResettableGameClass {

    MethodCallAuthenticator getAuthenticator();

    void onNuggetCollected(Pipe pipe, int val);
}
