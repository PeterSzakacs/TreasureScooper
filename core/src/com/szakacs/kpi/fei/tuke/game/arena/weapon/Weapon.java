package com.szakacs.kpi.fei.tuke.game.arena.weapon;

import com.szakacs.kpi.fei.tuke.game.arena.actors.Bullet;
import com.szakacs.kpi.fei.tuke.game.arena.pipe.PipeHead;
import com.szakacs.kpi.fei.tuke.game.intrfc.proxies.ActorGameInterface;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by developer on 29.1.2017.
 */
public class Weapon {
    private AmmoQueue ammoQueue;
    private ActorGameInterface world;
    private PipeHead head;

    public Weapon(int capacity, PipeHead head, ActorGameInterface world){
        this.ammoQueue = new AmmoQueue(capacity);
        this.head = head;
        this.world = world;
    }

    public void loadBullet(Bullet bullet){
        if (bullet != null)
            ammoQueue.enqueue(bullet);
    }

    public void fireBullet(){
        if (!ammoQueue.isEmpty()){
            Bullet fired = ammoQueue.dequeue();
            fired.launch(head.getCurrentPosition(), head.getDirection(), world);
        }
    }

    public AmmoQueue getAmmoQueue(){
        return this.ammoQueue;
    }
}
