package com.szakacs.kpi.fei.tuke.game.arena.pipe;

import com.szakacs.kpi.fei.tuke.game.arena.AbstractActor;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.GoldCollector;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.ManipulableGameInterface;

/**
 * Created by developer on 4.11.2016.
 */
public class PipeHead extends AbstractActor implements GoldCollector {
    private ManipulableGameInterface world;

    PipeHead(Direction direction, ManipulableGameInterface world) {
        super(world.getRootCell(), ActorType.PIPEHEAD, direction, world);
        this.world = world;
    }

    @Override
    public void act(ManipulableGameInterface world) {

    }

    protected void setDirection(Direction direction) {
        super.setDirection(direction);
    }

    protected void move(int dxAbs, int dyAbs, Direction direction){
        super.move(dxAbs, dyAbs, direction);
    }
}
