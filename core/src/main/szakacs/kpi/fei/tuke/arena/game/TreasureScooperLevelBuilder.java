package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorld;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.misc.ConfigProcessingException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by developer on 31.12.2016.
 */
public class TreasureScooperLevelBuilder {

    public TreasureScooperLevelBuilder() {
    }

    public GameLevelPrivileged buildGameLevel(DummyLevel level) throws ConfigProcessingException {
        TreasureScooperLevel game = new TreasureScooperLevel(level);
        Set<GameUpdater> updaters = new HashSet<>(3);
        for (Class<? extends GameUpdater> updaterCls : level.getGameUpdaterClasses()){
            try {
                updaters.add(updaterCls.getConstructor(GameLevelPrivileged.class).newInstance(game));
            } catch (NoSuchMethodException
                    | InvocationTargetException
                    | InstantiationException
                    | IllegalAccessException e) {
                throw new ConfigProcessingException("Failed to initialize game", e);
            }
        }
        Map<DummyEntrance, Class<? extends Player>> entranceToPlayerMap = level.getEntranceToPlayerMap();
        List<Player> players = new ArrayList<>(entranceToPlayerMap.size());
        GameWorld gameWorld = game.getGameWorld();
        ActorManagerPrivileged actorManager = game.getActorManager();
        for (DummyEntrance de : entranceToPlayerMap.keySet()) {
            try {
                Player player = entranceToPlayerMap.get(de).newInstance();
                players.add(player);
                actorManager.getPlayerToPipeMap().put(
                        player,
                        new Pipe(
                                actorManager.getActorGameProxy(),
                                gameWorld.getEntrances().get(de.getId()),
                                player
                        )
                );
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        game.startNewGame(updaters, players);
        return game;
    }
}
