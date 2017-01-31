package com.szakacs.kpi.fei.tuke.game.arena.weapon;

import com.szakacs.kpi.fei.tuke.game.arena.actors.Bullet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by developer on 1.12.2016.
 */
public class AmmoQueue {

    private Bullet[] bullets;
    private int front;
    private int rear;
    private int capacity;
    private int numBullets;

    AmmoQueue(int capacity){
        this.capacity = capacity;
        this.front = 0;
        this.rear = -1;
        this.numBullets = 0;
        this.bullets = new Bullet[this.capacity];
    }

    boolean enqueue(Bullet bullet){
        if (isFull())
            return false;
        rear++; numBullets++;
        if (rear == capacity)
            rear = 0;
        this.bullets[rear] = bullet;
        return true;
    }

    Bullet dequeue(){
        Bullet toReturn = this.bullets[front];
        this.bullets[front] = null;
        numBullets--; front++;
        if (front == capacity)
            front = 0;
        return toReturn;
    }

    public boolean isEmpty(){
        return numBullets == 0;
    }

    public boolean isFull(){
        return numBullets == capacity;
    }

    public int getFrontIndex() {
        return front;
    }

    public int getRearIndex() {
        return rear;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getNumBullets(){
        return this.numBullets;
    }

    public List<Bullet> getBullets(){
        return Collections.unmodifiableList(Arrays.asList(bullets));
    }
}
