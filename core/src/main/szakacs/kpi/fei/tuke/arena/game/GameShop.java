package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnItemBoughtCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

/**
 * Created by developer on 6.2.2017.
 */
public class GameShop {

    private ActorGameInterface gameInterface;
    private OnItemBoughtCallback callback;

    public GameShop(ActorGameInterface gameInterface, OnItemBoughtCallback callback){
        this.gameInterface = gameInterface;
        this.callback = callback;
    }

    public Bullet buyBullet(){
        callback.onItemBought(10);
        return new Bullet(gameInterface);
    }

    public void repairPipe(Pipe pipe, int byHowMuch){
        pipe.setHealth(pipe.getHealth() + byHowMuch, gameInterface);
        callback.onItemBought(byHowMuch * 5);
    }
}
