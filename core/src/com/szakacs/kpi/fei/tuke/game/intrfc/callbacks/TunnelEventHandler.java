package com.szakacs.kpi.fei.tuke.game.intrfc.callbacks;

import com.szakacs.kpi.fei.tuke.game.arena.Enemy;
import com.szakacs.kpi.fei.tuke.game.intrfc.GoldCollector;

/**
 * Created by developer on 17.11.2016.
 */
public interface TunnelEventHandler {
    void onEnemyDestroyed(Enemy enemy);
    void onNuggetCollected(GoldCollector collector);
}