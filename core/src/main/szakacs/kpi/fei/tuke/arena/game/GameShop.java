package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnScoreEventCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.Map;

/**
 * Created by developer on 6.2.2017.
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

    public void repairPipe(Pipe pipe, int byHowMuch){
        callback.onScoreEvent(playerScores.get(pipe.getController()) - byHowMuch * 5, pipe.getController());
        pipe.setHealth(pipe.getHealth() + byHowMuch, gameInterface.getAuthenticator());
    }
}
