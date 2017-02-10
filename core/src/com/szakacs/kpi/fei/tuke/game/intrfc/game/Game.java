package com.szakacs.kpi.fei.tuke.game.intrfc.game;

import com.szakacs.kpi.fei.tuke.game.arena.game.GameShop;
import com.szakacs.kpi.fei.tuke.game.enums.GameState;
import com.szakacs.kpi.fei.tuke.game.intrfc.Player;
import com.szakacs.kpi.fei.tuke.game.intrfc.proxies.ActorGameInterface;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.ActorManagerChangeable;

/**
 * Created by developer on 25.1.2017.
 */
public interface Game {

    Player getPlayer();

    GameState getState();

    GameWorld getGameWorld();

    int getScore();

    GameShop getGameShop();
}
