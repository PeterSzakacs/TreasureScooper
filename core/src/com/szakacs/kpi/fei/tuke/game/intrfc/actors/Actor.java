package com.szakacs.kpi.fei.tuke.game.intrfc.actors;

import com.szakacs.kpi.fei.tuke.game.arena.world.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameWorld;
import com.szakacs.kpi.fei.tuke.game.intrfc.proxies.ActorGameInterface;

/**
 * Created by developer on 6.11.2016.
 */
public interface Actor {

    int getX();

    int getY();

    ActorType getType();

    Direction getDirection();

    void act(ActorGameInterface world);

    boolean intersects(Actor other);

    TunnelCell getCurrentPosition();
}
