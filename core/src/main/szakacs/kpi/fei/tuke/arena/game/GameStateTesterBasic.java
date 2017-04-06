package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameStateTester;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

/**
 * Created by developer on 4.3.2017.
 */
public class GameStateTesterBasic implements GameStateTester {

    private GameWorldBasic world;
    private PlayerManagerPrivileged playerManager;

    public GameStateTesterBasic(){}

    /*public GameStateTesterBasic(GameLevelPrivileged gameLevel){
        this.world = gameLevel.getGameWorld();
        this.playerManager = gameLevel.getPlayerManager();
    }*/

    @Override
    public GameState testGameState(){
        if (world.getNuggetCount() == 0)
            return GameState.WON;
        int healthCount = 0;
        for (PipeBasic pipe : playerManager.getPipes()) {
            healthCount += pipe.getHealth();
        }
        if (healthCount <= 0) {
            return GameState.LOST;
        }
        return GameState.PLAYING;
    }

    @Override
    public void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level) {
        this.world = gameLevel.getGameWorld();
        this.playerManager = gameLevel.getPlayerManager();
    }
}
