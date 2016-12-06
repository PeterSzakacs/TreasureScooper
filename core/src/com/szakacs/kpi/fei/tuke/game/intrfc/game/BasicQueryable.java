package com.szakacs.kpi.fei.tuke.game.intrfc.game;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.GameState;

import java.util.Collection;

/**
 * Created by developer on 1.12.2016.
 */
public interface BasicQueryable {
    Pipe getPipe();

    TunnelCell getRootCell();

    int getWidth();

    int getHeight();

    int getOffsetX();

    int getOffsetY();

    int getRemainingNuggetsCount();

    Collection<HorizontalTunnel> getTunnels();

    GameState getState();
}
