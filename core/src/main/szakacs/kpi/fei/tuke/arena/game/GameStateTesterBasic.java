package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameStateTester;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by developer on 4.3.2017.
 */
public class GameStateTesterBasic implements GameStateTester {

    private class PipeInfo {
        private int numSegments;
        // The number of turns during which a pipe remains stationary
        private int numTurns;
        private boolean isStuck = false;

        private PipeInfo(int numSegments, int numTurns){
            this.numSegments = numSegments; this.numTurns = numTurns;
        }
    }



    // 200 turns, during which a player's pipe is allowed
    // to not move at all, is probably gratuitous enough.
    private int maxUnchangedTurnCount = 200;

    private boolean allPipesStuck;
    private GameWorldBasic world;
    private PlayerManagerPrivileged playerManager;
    private Map<PipeBasic, PipeInfo> infoMap;



    public GameStateTesterBasic(){
        this.infoMap = new HashMap<>(3);
    }

    @Override
    public GameState testGameState(){
        if (world.getNuggetCount() == 0) {
            return GameState.WON;
        }
        int healthCount = 0;
        Map<PlayerToken, Pipe> pipeMap = playerManager.getPipesUpdatable();
        allPipesStuck = true;
        for (PlayerToken token : pipeMap.keySet()) {
            PipeBasic pipe = pipeMap.get(token);
            PipeInfo info = infoMap.get(pipe);
            int health = pipe.getHealth();

            if (health > 0){
                checkIfPipeIsStuck(info, pipe.getSegmentStack(token).getNumElements());
                allPipesStuck &= info.isStuck;
            }

            healthCount += pipe.getHealth();
        }
        if (healthCount <= 0 || allPipesStuck) {
            return GameState.LOST;
        }
        return GameState.PLAYING;
    }

    @Override
    public void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level) {
        this.world = gameLevel.getGameWorld();
        this.playerManager = gameLevel.getPlayerManager();
        this.allPipesStuck = false;
        infoMap.clear();
        for (PipeBasic pipe : playerManager.getPipesUpdatable().values()) {
            infoMap.put(pipe, new PipeInfo(
                    pipe.getSegmentStack(null).getNumElements(), 0
            ));
        }
    }


    // helper methods


    private void checkIfPipeIsStuck(PipeInfo info, int currentNumSegments){
        if (currentNumSegments == info.numSegments) {
            info.numTurns++;
            if (info.numTurns >= maxUnchangedTurnCount) {
                info.isStuck = true;
            }
        } else {
            info.isStuck = false;
            info.numTurns = 0;
            info.numSegments = currentNumSegments;
        }
    }
}
