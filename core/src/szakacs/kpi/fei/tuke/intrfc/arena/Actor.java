package szakacs.kpi.fei.tuke.intrfc.arena;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.intrfc.misc.proxies.ActorGameInterface;

/**
 * Created by developer on 6.11.2016.
 */
public interface Actor {

    int getX();

    int getY();

    ActorType getType();

    Direction getDirection();

    void act(ActorGameInterface world);

    boolean intersects(Actor other);

    TunnelCell getCurrentPosition();
}
