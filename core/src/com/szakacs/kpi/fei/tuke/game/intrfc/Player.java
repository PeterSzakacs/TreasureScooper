package com.szakacs.kpi.fei.tuke.game.intrfc;

import com.szakacs.kpi.fei.tuke.game.intrfc.proxies.PlayerGameInterface;

/**
 * Created by developer on 2.12.2016.
 */
public interface Player {
    void act();

    void initialize(PlayerGameInterface world);

    void deallocate();
}
