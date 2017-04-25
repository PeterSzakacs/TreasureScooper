package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.intrfc.misc.Queue;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.misc.ArrayQueue;
import szakacs.kpi.fei.tuke.misc.CollectionsCustom;

/**
 * A class representing the weapon wielded by the player.
 */
public class Weapon {

    private class BulletQueue extends ArrayQueue<Bullet> {

        int frontIndex;
        int rearIndex;

        BulletQueue(int capacity) {
            super(capacity, false);
            frontIndex = 0;
            rearIndex = -1;
        }

        @Override
        public void enqueue(Bullet bullet){
            if ( ! isFull() ) {
                super.enqueue(bullet);
                rearIndex++;
                if (rearIndex >= super.getCapacity())
                    rearIndex = 0;
            }
        }

        @Override
        public Bullet dequeue() {
            Bullet fired = null;
            if (!ammoQueue.isEmpty()) {
                fired = super.dequeue();
                frontIndex++;
                if (frontIndex >= super.getCapacity())
                    frontIndex = 0;
                fired.launch(head.getCurrentPosition(), head.getDirection(), world);
            }
            return fired;
        }
    }



    private BulletQueue ammoQueue;
    private ActorGameInterface world;
    private PipeHead head;
    private PlayerToken token;



    Weapon(int capacity, PipeHead head, ActorGameInterface world, PlayerToken token){
        this.ammoQueue = new BulletQueue(capacity);
        this.head = head;
        this.world = world;
        this.token = token;
    }

    /**
     * <p>Gets the {@link Queue} of {@link Bullet}s that represents the ammunition store
     * of the weapon.</p>
     *
     * <p>Enqueueing and dequeing results in a bullet being loaded or fired, respectively.</p>
     *
     * @param token the token of the player owning this weapon.
     *              If a value of null or an invalid token is
     *              passed, this method returns an unmodifiable
     *              view of the actual queue, meaning queuing
     *              and dequeuing results in an exception being
     *              thrown.
     *
     * @return the ammunition store of the weapon.
     */
    public Queue<Bullet> getBulletQueue(PlayerToken token){
        if (this.token.validate(token)) {
            return ammoQueue;
        } else {
            return CollectionsCustom.unmodifiableQueue(ammoQueue);
        }
    }

    /**
     * <p>Fires a bullet, if there was one in the ammunition store of this weapon
     * at the time this method was called and if the token passed to it belongs
     * to the player owning this weapon. The result in such case is the same as
     * getBulletQueue(token).dequeue(), otherwise null is returned.</p>
     *
     * @param token the token of the player owning this weapon.
     * @return the bullet fired if conditions hold or null otherwise.
     */
    public Bullet fire(PlayerToken token) {
        if (this.token.validate(token)){
            return ammoQueue.dequeue();
        } else {
            return null;
        }
    }

    /**
     * <p>Loads a bullet to the ammunition store if it is not null,
     * the token passed belongs to the player owning this weapon
     * and the queue is not full yet. The result in such case is
     * the same as calling getBulletQueue(token).enqueue();
     * otherwise nothing happens</p>
     *
     * @param bullet the bullet to load to the ammunition store.
     * @param token the token of the player owning this weapon.
     */
    public void load(Bullet bullet, PlayerToken token){
        if (this.token.validate(token)){
            ammoQueue.enqueue(bullet);
        }
    }

    /**
     * <p>Returns the array index of the element at the front of the queue.
     * Only used for rendering.</p>
     *
     * <p><b>If you are the player, just ignore this method.</b></p>
     *
     * @return the array index of the queue front.
     */
    public int getFrontIndex() {
        return ammoQueue.frontIndex;
    }

    /**
     * <p>Returns the array index of the element at the rear of the queue.
     * Only used for rendering.</p>
     *
     * <p><b>If you are the player, just ignore this method.</b></p>
     *
     * @return the array index of the queue rear.
     */
    public int getRearIndex(){
        return ammoQueue.rearIndex;
    }
}
