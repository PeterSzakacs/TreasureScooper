package com.szakacs.kpi.fei.tuke.game.intrfc.game.world;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.GameState;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.Player;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by developer on 1.12.2016.
 */
public interface QueryableGameInterface {

    int getWidth();

    int getHeight();

    int getOffsetX();

    int getOffsetY();

    int getRemainingNuggetsCount();

    List<HorizontalTunnel> getTunnels();

    TunnelCell getRootCell();

    GameState getState();

    Player getPlayer();

    Pipe getPipe();

    List<Actor> getActors();

    List<Actor> getActorsBySearchCriteria(Predicate<Actor> predicate);
}
