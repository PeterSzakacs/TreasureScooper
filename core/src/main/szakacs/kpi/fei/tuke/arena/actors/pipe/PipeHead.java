package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.arena.actors.AbstractMoveableActor;
import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.GoldCollector;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.intrfc.misc.Queue;

/**
 * The collecting head of the pipe.
 */
public class PipeHead extends AbstractMoveableActor implements GoldCollector {

    private Weapon weapon;

    PipeHead(Direction direction, ActorGameInterface gameInterface, TunnelCell startPosition) {
        super(startPosition, ActorType.PIPE, direction, gameInterface);
        this.weapon = new Weapon(10, this, gameInterface);
        Queue<Bullet> weaponQueue = weapon.getBulletQueue();
        int capacity = weaponQueue.getCapacity();
        for (int i = 0; i < capacity; i++) {
            weaponQueue.enqueue(new Bullet(gameInterface));
        }
    }

    @Override
    public void act(Object authToken) {

    }

    /**
     * Gets the weapon to be used by the player.
     *
     * @return the weapon to use by the player.
     */
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
