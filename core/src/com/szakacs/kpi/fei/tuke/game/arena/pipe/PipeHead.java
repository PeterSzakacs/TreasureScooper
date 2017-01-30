package com.szakacs.kpi.fei.tuke.game.arena.pipe;

import com.szakacs.kpi.fei.tuke.game.arena.actors.AbstractMoveableActor;
import com.szakacs.kpi.fei.tuke.game.arena.weapon.Weapon;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.GoldCollector;
import com.szakacs.kpi.fei.tuke.game.intrfc.proxies.ActorGameInterface;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameWorld;

/**
 * Created by developer on 4.11.2016.
 */
public class PipeHead extends AbstractMoveableActor implements GoldCollector {

    private Weapon weapon;

    PipeHead(Direction direction, ActorGameInterface world) {
        super(world.getRootCell(), ActorType.PIPEHEAD, direction, world);
        this.weapon = new Weapon(10, this, world);
    }

    @Override
    public void act(ActorGameInterface world) {

    }

    public Weapon getWeapon(){
        return this.weapon;
    }

    protected void setDirection(Direction direction) {
        super.setDirection(direction);
    }

    protected void move(int dxAbs, int dyAbs, Direction direction){
        super.move(dxAbs, dyAbs, direction);
    }
}
