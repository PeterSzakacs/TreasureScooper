package szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;

import java.util.Set;

/**
 * Created by developer on 6.4.2017.
 */
public interface PlayerManagerUpdatable extends PlayerManagerBasic {
    
    Set<Pipe> getPipesUpdatable();
}
