package szakacs.kpi.fei.tuke.arena.pipe;

import szakacs.kpi.fei.tuke.arena.actors.AbstractMoveableActor;
import szakacs.kpi.fei.tuke.arena.weapon.Weapon;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.GoldCollector;
import szakacs.kpi.fei.tuke.intrfc.misc.proxies.ActorGameInterface;

/**
 * Created by developer on 4.11.2016.
 */
public class PipeHead extends AbstractMoveableActor implements GoldCollector {

    private Weapon weapon;

    PipeHead(Direction direction, ActorGameInterface world) {
        super(world.getRootCell(), ActorType.PIPEHEAD, direction, world);
        this.weapon = new Weapon(10, this, world);
    }

    @Override
    public void act(ActorGameInterface world) {

    }

    public Weapon getWeapon(){
        return this.weapon;
    }

    protected void setDirection(Direction direction) {
        super.setDirection(direction);
    }

    protected void move(int dxAbs, int dyAbs, Direction direction){
        super.move(dxAbs, dyAbs, direction);
    }
}
