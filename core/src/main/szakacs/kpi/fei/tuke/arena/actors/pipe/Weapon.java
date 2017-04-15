package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.intrfc.misc.Queue;
import szakacs.kpi.fei.tuke.misc.ArrayQueue;

/**
 *
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
     * Gets the {@link Queue} of {@link Bullet}s that represents the ammunition store of the weapon.
     * Enqueueing and dequeing results in a bullet being loaded or fired, respectively.
     *
     * @return the ammunition store of the weapon.
     */
    public Queue<Bullet> getBulletQueue(){
        return ammoQueue;
    }

    public int getFrontIndex() {
        return ammoQueue.frontIndex;
    }

    public int getRearIndex(){
        return ammoQueue.rearIndex;
    }
}
