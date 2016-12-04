package com.szakacs.kpi.fei.tuke.game.intrfc.game;

import com.szakacs.kpi.fei.tuke.game.arena.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.TunnelCell;
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
