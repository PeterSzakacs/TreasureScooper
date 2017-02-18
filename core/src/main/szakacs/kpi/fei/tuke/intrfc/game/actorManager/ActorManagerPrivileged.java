package szakacs.kpi.fei.tuke.intrfc.game.actorManager;

import szakacs.kpi.fei.tuke.arena.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.misc.proxies.ActorGameInterface;

import java.util.Map;

/**
 * Created by developer on 29.1.2017.
 *
 * Extension of the "updatable" interface for the actor manager object.
 *
 * This interface is designed to be exposed only to game backend classes
 * which need access to specific state and behavior of the actor manager.
 */
public interface ActorManagerPrivileged extends ActorManagerUpdatable, GameUpdater {

    /**
     * Updates all game actors (including unregistered actors,
     *
     * @see this#getUnregisteredActors()
     */
    void update();

    /**
     * Gets a map of all unregistered actors (those whose update() method is not called
     * anymore) and a value detailing for how many game loop iterations they have been
     * unregistered (only kept for N further iterations, where N is arbitrary).
     * Primarily, these values serve for effects rendering when an actor is removed.
     */
    Map<Actor, Integer> getUnregisteredActors();

    /**
     * Gets the actual object that serves as an interface to game functionality
     * for actors
     *
     * @see interface intrfc/misc/proxies/ActorGameProxy
     */
    ActorGameInterface getActorGameProxy();

    Map<Player, Pipe> getPlayerToPipeMap();
}
