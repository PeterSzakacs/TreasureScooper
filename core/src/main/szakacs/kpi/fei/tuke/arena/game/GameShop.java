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

    public Bullet buyBullet(Player caller){
        callback.onScoreEvent(playerScores.get(caller) - 10, caller);
        return new Bullet(gameInterface);
    }

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
