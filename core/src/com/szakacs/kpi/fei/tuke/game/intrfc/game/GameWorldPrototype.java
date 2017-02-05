package com.szakacs.kpi.fei.tuke.game.intrfc.game;

import com.szakacs.kpi.fei.tuke.game.misc.world.DummyEntrance;
import com.szakacs.kpi.fei.tuke.game.misc.world.DummyTunnel;

import java.util.List;
import java.util.Map;

/**
 * Created by developer on 29.1.2017.
 */
public interface GameWorldPrototype {

    int getWidth();

    int getHeight();

    int getOffsetX();

    int getOffsetY();

    List<DummyEntrance> getDummyEntrances();

    Map<String, DummyTunnel> getDummyTunnels();
}
