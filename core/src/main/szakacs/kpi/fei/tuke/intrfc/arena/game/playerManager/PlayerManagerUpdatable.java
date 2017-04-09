package szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;

import java.util.Set;

/**
 *
 */
public interface PlayerManagerUpdatable extends PlayerManagerBasic {
    
    Set<Pipe> getPipesUpdatable();
}
