package com.szakacs.kpi.fei.tuke.game.intrfc.game.world;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.GameState;
import com.szakacs.kpi.fei.tuke.game.intrfc.Player;

import java.util.Collection;
import java.util.List;

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

    List<HorizontalTunnel> getTunnels();

    GameState getState();

    Player getPlayer();
}
