package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.intrfc.misc.Queue;
import szakacs.kpi.fei.tuke.misc.ArrayQueue;

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



    Weapon(int capacity, PipeHead head, ActorGameInterface world){
        this.ammoQueue = new BulletQueue(capacity);
        this.head = head;
        this.world = world;
    }

    /**
     * <p>Gets the {@link Queue} of {@link Bullet}s that represents the ammunition store
     * of the weapon.</p>
     *
     * <p>Enqueueing and dequeing results in a bullet being loaded or fired, respectively.</p>
     *
     * @return the ammunition store of the weapon.
     */
    public Queue<Bullet> getBulletQueue(){
        return ammoQueue;
    }

    /**
     * Returns the array index of the element at the front of the queue.
     * Only used for rendering.
     *
     * @return the array index of the queue front.
     */
    public int getFrontIndex() {
        return ammoQueue.frontIndex;
    }

    /**
     * Returns the array index of the element at the rear of the queue.
     * Only used for rendering.
     *
     * @return the array index of the queue rear.
     */
    public int getRearIndex(){
        return ammoQueue.rearIndex;
    }
}
