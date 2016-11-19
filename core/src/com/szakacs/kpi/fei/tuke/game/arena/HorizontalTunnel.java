package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.enums.Direction;

import java.util.*;

/**
 * Created by developer on 5.11.2016.
 */
public class HorizontalTunnel {
    private int y;
    private int turnCounter;
    private int turnBound;
    private int nuggetCount;
    private GoldNugget[] nuggets;
    private List<Enemy> enemies;
    private final List<Integer> entrances;
    private HorizontalTunnel previous;
    private HorizontalTunnel next;
    private TreasureScooper world;


    HorizontalTunnel(int y, List<Integer> entrances, TreasureScooper world) {
        Random rand = new Random();
        do {
            this.turnBound = rand.nextInt(150);
        } while (this.turnBound < 120);
        this.y = y;
        this.turnCounter = 0;
        this.entrances = entrances;
        this.enemies = new ArrayList<Enemy>(3);
        int capacity = world.getWidth()/world.getOffsetX();
        this.nuggets = new GoldNugget[capacity];
        for (int i = 0; i < capacity; i++){
            this.nuggets[i] = new GoldNugget(50);
        }
        this.nuggetCount = this.nuggets.length;
        this.previous = null;
        this.next = null;
        this.world = world;
    }

    public int getY() {
        return y;
    }

    List<Enemy> getEnemies() {
        return enemies;
    }

    GoldNugget[] getNuggets() {
        return nuggets;
    }

    public List<Integer> getEntrances() {
        return entrances;
    }

    void setNextTunnel(HorizontalTunnel tunnel){
        this.next = tunnel;
    }

    void setPreviousTunnel(HorizontalTunnel tunnel){
        this.previous = tunnel;
    }

    public HorizontalTunnel getNextTunnel() {
        return next;
    }

    public HorizontalTunnel getPreviousTunnel(){
        return previous;
    }

    /**
     * @param x, the x coordinate of a nugget on the screen,
     *           if a nugget is located between x/STD_OFFSET
     *           and x/STD_OFFSET + STD_OFFSET, its representation
     *           in the list at index x/STD_OFFSET is removed
     *           from the list and the value of the removed nugget
     *           is returned
     * @return value of the returned nugget or 0 if no nugget is found.
     */
    int collectNugget(int x){
        int idx = x/world.getOffsetX();
        GoldNugget nugget = this.nuggets[idx];
        if (nugget == null)
            return 0;
        else {
            this.nuggets[idx] = null;
            this.nuggetCount--;
            return nugget.getValue();
        }
    }

    public int getNuggetCount(){
        return this.nuggetCount;
    }

    public int getNearestEntrance(int x){
        int nearest = entrances.get(0), nearest_diff = Math.abs(nearest - x), diff_x;
        for (int entrance : entrances) {
            diff_x = Math.abs(x - entrance);
            if (diff_x < nearest_diff)
                nearest = entrance;
        }
        return nearest;
    }

    void act(){
        Enemy currentEnemy;
        for (Iterator<Enemy> enemyIt = enemies.iterator(); enemyIt.hasNext(); ) {
            currentEnemy = enemyIt.next();
            currentEnemy.act();
            if ((currentEnemy.getX() > world.getWidth() && currentEnemy.getDirection() == Direction.RIGHT)
                    || (currentEnemy.getX() < -world.getOffsetX() && currentEnemy.getDirection() == Direction.LEFT))
                enemyIt.remove();
        }
        turnCounter++;
        if (turnCounter > turnBound) {
            turnCounter = 0;
            if (this.enemies.size() < 2)
                createNewEnemy();
        }
    }

    Enemy createNewEnemy(){
        Date current_date = new Date();
        Direction dir = current_date.getTime() % 2 == 0 ? Direction.LEFT : Direction.RIGHT;
        Enemy added = new Enemy(dir, this.y, this.world);
        this.enemies.add(added);
        return added;
    }

    void destroyEnemy(Enemy enemy){
        this.enemies.remove(enemy);
    }
}
