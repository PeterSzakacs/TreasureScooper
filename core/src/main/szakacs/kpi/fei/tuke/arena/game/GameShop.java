package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnScoreEventCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

import java.util.Map;
import java.util.Set;

/**
 * A class responsible for any purchases by the player during the game level.
 */
public class GameShop {

    private OnScoreEventCallback callback;
    private ActorGameInterface gameInterface;
    private Map<Player, Integer> playerScores;

    public GameShop(GameLevelPrivileged gameLevel, OnScoreEventCallback callback) {
        this.callback = callback;
        this.gameInterface = gameLevel.getActorInterface();
        this.playerScores = gameInterface.getPlayersAndScores();
    }

    /**
     * <p>Creates a new bullet for the player calling this method.</p>
     *
     * <p>Buying a bullet currently subtracts 10 points from the calling players score.</p>
     *
     * @param caller the player calling this method
     *               (must put "this" - no quote marks - as the actual argument).
     * @return a new Bullet for the caller.
     */
    public Bullet buyBullet(Player caller){
        callback.onScoreEvent(playerScores.get(caller) - 10, caller);
        return new Bullet(gameInterface);
    }

    /**
     * <p>Repairs the pipe of the player calling this method.</p>
     *
     * <p>The price of repair depends on how much the pipe
     * should be repaired. Currently a repair subtracts
     * 5 points for each percentage point of repaired
     * damage.</p>
     *
     * @param player the player calling this method
     *               (must put "this" - no quote marks - as the actual argument).
     * @param byHowMuch the amount of percentage points by which to repair the pipe
     *                  of the player owning it (the caller of this method).
     */
    public void repairPipeOfPlayer(Player player, int byHowMuch){
        Set<Pipe> allPipes = gameInterface.getPipesUpdatable();
        Pipe pipe = null;
        for (Pipe potentialPipe : allPipes){
            if (potentialPipe.getController().equals(player)){
                pipe = potentialPipe;
                break;
            }
        }
        if (pipe != null) {
            callback.onScoreEvent(playerScores.get(pipe.getController()) - byHowMuch * 5, pipe.getController());
            pipe.setHealth(pipe.getHealth() + byHowMuch, gameInterface.getAuthenticator());
        }
    }
}
