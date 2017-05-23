package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.arena.actors.AbstractMoveableActor;
import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

import java.util.Set;

/**
 * The collecting head of the pipe.
 */
public class PipeHead extends AbstractMoveableActor {

    private Weapon weapon;
    private PlayerToken token;
    //private int collectedNuggetsCounter = 0;

    PipeHead(Direction direction, ActorGameInterface gameInterface, TunnelCellUpdatable startPosition, PlayerToken token) {
        super(startPosition, ActorType.PIPEHEAD, direction, gameInterface);
        this.weapon = new Weapon(10, this, gameInterface, token);
        this.token = token;
        /*Queue<Bullet> weaponQueue = weapon.getBulletQueue(token);
        int capacity = weaponQueue.getCapacity();
        for (int i = 0; i < capacity/2; i++) {
            weaponQueue.enqueue(new Bullet(gameInterface));
        }*/
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

    public PipeBasic getPipe(){
        return gameInterface.getPlayerTokenMap().get(token).getPipe();
    }

    protected void setDirection(Direction direction) {
        super.setDirection(direction);
    }

    protected void move(int dxAbs, int dyAbs, Direction direction){
        super.move(dxAbs, dyAbs, direction);
    }

    void onPush(Pipe pipe){
        TunnelCellUpdatable currentPosition = getCurrentPosition();
        //int prevNuggetCount = gameInterface.getGameWorld().getNuggetCount();
        Set<ActorBasic> enemies = gameInterface.getActorsByType(ActorType.ENEMY);

        // collect nugget, and if a threshold is reached,
        // load a bonus bullet into the weapon.
        currentPosition.collectNugget(pipe);
/*        if (gameInterface.getGameWorld().getNuggetCount() < prevNuggetCount){
            collectedNuggetsCounter++;
            if (collectedNuggetsCounter >= 20) {
                weapon.load(new Bullet(gameInterface), token);
                collectedNuggetsCounter = 0;
            }
        }*/
        // remove all enemy actors at current position
        for (ActorBasic actor : enemies){
            if (actor.intersects(this)) {
                gameInterface.unregisterActor(actor);
            }
        }
    }
}
