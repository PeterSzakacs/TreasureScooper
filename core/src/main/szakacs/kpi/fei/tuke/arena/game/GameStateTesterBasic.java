package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameStateTester;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorld;

/**
 * Created by developer on 4.3.2017.
 */
public class GameStateTesterBasic implements GameStateTester {

    private GameWorld world;
    private ActorManagerPrivileged actorManager;

    public GameStateTesterBasic(GameLevelPrivileged gameLevel){
        this.world = gameLevel.getGameWorld();
        this.actorManager = gameLevel.getActorManager();
    }

    @Override
    public GameState testGameState(){
        if (world.getNuggetCount() == 0)
            return GameState.WON;
        int healthCount = 0;
        for (Pipe pipe : actorManager.getPlayerToPipeMap().values()) {
            healthCount += pipe.getHealth();
        }
        if (healthCount <= 0) {
            return GameState.LOST;
        }
        return GameState.PLAYING;
    }
}
