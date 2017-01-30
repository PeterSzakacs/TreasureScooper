package com.szakacs.kpi.fei.tuke.game.intrfc.game;

import com.szakacs.kpi.fei.tuke.game.intrfc.Player;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.ActorManagerPrivileged;
import com.szakacs.kpi.fei.tuke.game.intrfc.proxies.ActorGameInterface;

import java.util.Set;

/**
 * Created by developer on 29.1.2017.
 */
public interface GamePrivileged extends Game {

    ActorManagerPrivileged getActorManager();

    void update();

    void startNewGame(Set<GameUpdater> updaters, Player player);
}
