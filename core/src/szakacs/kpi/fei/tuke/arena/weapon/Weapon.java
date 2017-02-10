package szakacs.kpi.fei.tuke.arena.weapon;

import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.arena.pipe.PipeHead;
import szakacs.kpi.fei.tuke.intrfc.misc.proxies.ActorGameInterface;

/**
 * Created by developer on 29.1.2017.
 */
public class Weapon {
    private AmmoQueue ammoQueue;
    private ActorGameInterface world;
    private PipeHead head;

    public Weapon(int capacity, PipeHead head, ActorGameInterface world){
        this.ammoQueue = new AmmoQueue(capacity);
        this.head = head;
        this.world = world;
    }

    public void loadBullet(Bullet bullet){
        if (bullet != null)
            ammoQueue.enqueue(bullet);
    }

    public void fire(){
        if (!ammoQueue.isEmpty()){
            Bullet fired = ammoQueue.dequeue();
            fired.launch(head.getCurrentPosition(), head.getDirection(), world);
        }
    }

    public AmmoQueue getAmmoQueue(){
        return this.ammoQueue;
    }
}
