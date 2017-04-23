package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnScoreEventCallback;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerInfo;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

import java.util.Map;

/**
 * A class responsible for any purchases by the player during the game level.
 */
public class GameShop {

    private OnScoreEventCallback callback;
    private ActorGameInterface gameInterface;
    private Map<PlayerToken, PlayerInfo> playerInfoMap;
    private Map<PlayerToken, Pipe> pipeMap;

    public GameShop(GameLevelPrivileged gameLevel, OnScoreEventCallback callback) {
        this.callback = callback;
        this.gameInterface = gameLevel.getActorInterface();
        this.playerInfoMap = gameInterface.getPlayerTokenMap();
        this.pipeMap = gameInterface.getPipesUpdatable();
    }

    /**
     * <p>Creates a new bullet for the player
     * calling this method.</p>
     *
     * <p>Buying a bullet currently subtracts
     * 10 points from the calling players score.
     * If the players current score is too low,
     * no bullet (null) is returned</p>
     *
     * @param token the identifier of the player calling this method
     *
     * @return a new Bullet for the caller.
     */
    public Bullet buyBullet(PlayerToken token){
        PlayerInfo info = playerInfoMap.get(token);
        if (info.getScore() > 10) {
            callback.onScoreEvent(info.getScore() - 10, token);
            return new Bullet(gameInterface);
        } else {
            return null;
        }
    }

    /**
     * <p>Repairs the pipe of a player</p>
     *
     * <p>The price of repair depends on how much the pipe should
     * be repaired. Currently all repairs will subtract 5 points
     * for each percentage point of repaired damage.</p>
     *
     * <p>If the price of repair is higher than players current
     * score, then nothing is repaired, obviously</p>
     *
     * @param token the token (identifier) of the player owning the pipe
     *              to be repaired. Must be a token assigned by the game
     *              to a player, otherwise no pipe is repaired.
     * @param byHowMuch the amount of percentage points by which to repair
     *                  the pipe.
     */
    public void repairPipeOfPlayer(PlayerToken token, int byHowMuch){
        if (byHowMuch < 0)
            return;
        Pipe pipe = pipeMap.get(token);
        PlayerInfo info = playerInfoMap.get(token);
        int price = byHowMuch * 5;
        if (info != null && info.getScore() > price) {
            callback.onScoreEvent(info.getScore() - price, token);
            pipe.setHealth(pipe.getHealth() + byHowMuch,
                    gameInterface.getAuthenticator()
            );
        }
    }
}
