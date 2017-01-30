package com.szakacs.kpi.fei.tuke.game.intrfc.game;

import com.szakacs.kpi.fei.tuke.game.arena.world.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.world.TunnelCell;

import java.util.List;

/**
 * Created by developer on 25.1.2017.
 */
public interface GameWorld {

    int getWidth();

    int getHeight();

    int getOffsetX();

    int getOffsetY();

    int getNuggetCount();

    TunnelCell getRootCell();

    List<HorizontalTunnel> getTunnels();
}
