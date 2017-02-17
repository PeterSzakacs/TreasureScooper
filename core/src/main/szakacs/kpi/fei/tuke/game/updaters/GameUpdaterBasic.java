package szakacs.kpi.fei.tuke.game.updaters;

import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.game.GameLevelPrivileged;

/**
 * Created by developer on 17.1.2017.
 *
 * Updater class to manage the basic aspects of updating the game,
 * specifically calling the update() methods of the arena and player,
 * as well as determining for how long to keep arena that have been
 * removed from the game world (unregistered arena, they are kept
 * for a while afterwards, for possible rendering applied to them).
 */
public class GameUpdaterBasic extends AbstractGameUpdater {

    public GameUpdaterBasic(GameLevelPrivileged game){
        super(game);
    }

    @Override
    public void update() {
        if (game.getState() == GameState.PLAYING) {
            game.getPlayer().act();
            actorManager.getPipe().allowMovement(actorManager.getActorGameProxy());
        }
        actorManager.update();
    }
}
