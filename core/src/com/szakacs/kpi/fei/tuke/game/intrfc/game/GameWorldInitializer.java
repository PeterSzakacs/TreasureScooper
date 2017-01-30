package com.szakacs.kpi.fei.tuke.game.intrfc.game;

import com.szakacs.kpi.fei.tuke.game.misc.DummyTunnel;

import java.util.Map;

/**
 * Created by developer on 29.1.2017.
 */
public interface GameWorldInitializer {

    int getWidth();

    int getHeight();

    int getOffsetX();

    int getOffsetY();

    int getInitX();

    int getInitY();

    String getRootTunnelId();

    Map<String, DummyTunnel> getDummyTunnels();
}
