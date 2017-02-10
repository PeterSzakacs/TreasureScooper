package com.szakacs.kpi.fei.tuke.game.arena.game;

import com.szakacs.kpi.fei.tuke.game.arena.actors.Bullet;
import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.weapon.Weapon;
import com.szakacs.kpi.fei.tuke.game.intrfc.callbacks.OnItemBoughtCallback;
import com.szakacs.kpi.fei.tuke.game.intrfc.proxies.ActorGameInterface;

/**
 * Created by developer on 6.2.2017.
 */
public class GameShop {

    private ActorGameInterface gameInterface;
    private OnItemBoughtCallback callback;

    GameShop(ActorGameInterface gameInterface, OnItemBoughtCallback callback){
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
