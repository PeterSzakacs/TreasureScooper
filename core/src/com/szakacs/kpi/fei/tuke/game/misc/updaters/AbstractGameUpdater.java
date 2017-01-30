package com.szakacs.kpi.fei.tuke.game.misc.updaters;

import com.szakacs.kpi.fei.tuke.game.intrfc.actors.ActorManager;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.ActorManagerChangeable;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.ActorManagerPrivileged;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.Game;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GamePrivileged;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameUpdater;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameWorld;

/**
 * Created by developer on 28.1.2017.
 */
public abstract class AbstractGameUpdater implements GameUpdater {

    protected GamePrivileged game;
    protected GameWorld gameWorld;
    protected ActorManagerPrivileged actorManager;

    protected AbstractGameUpdater(GamePrivileged game){
        this.game = game;
        this.gameWorld = game.getGameWorld();
        this.actorManager = game.getActorManager();
    }
}
