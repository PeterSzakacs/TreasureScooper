package com.szakacs.kpi.fei.tuke.game.intrfc.eventHandlers;

import com.szakacs.kpi.fei.tuke.game.arena.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.TunnelCell;

import java.util.Map;

/**
 * Created by developer on 17.11.2016.
 */
public interface GameInitializer {
    void setDimensions(int width, int height);
    void setOffsets(int offsetX, int offsetY);
    void setInitialPosition(int initX, int initY);
    void setTunnels(Map<String, HorizontalTunnel> tunnels, TunnelCell startCell);
}
