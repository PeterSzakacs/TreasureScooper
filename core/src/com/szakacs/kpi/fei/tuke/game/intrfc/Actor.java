package com.szakacs.kpi.fei.tuke.game.intrfc;

import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

/**
 * Created by developer on 6.11.2016.
 */
public interface Actor {
    int getX();
    int getY();
    ActorType getType();
    Direction getDirection();
    void act(ManipulableGameInterface world);
    boolean intersects(Actor other);
    TunnelCell getCurrentPosition();
}
